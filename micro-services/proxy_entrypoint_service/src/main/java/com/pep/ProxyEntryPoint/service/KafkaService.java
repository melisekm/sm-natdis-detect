package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.PredictionConverter;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityGetFromNerGroups;
import com.pep.ProxyEntryPoint.rest.dto.KafkaDto;
import com.pep.ProxyEntryPoint.rest.dto.LinkCountOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionPredictServiceOutput;
import com.pep.ProxyEntryPoint.util.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class KafkaService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final PredictionService predictionService;
    private final PredictionConverter predictionConverter;
    private final PredictionRepository predictionRepository;
    private final DbEntityService dbEntityService;
    private final LinkService linkService;

    @Autowired
    public KafkaService(KafkaTemplate<String, String> kafkaTemplate,
                        @Lazy PredictionService predictionService,
                        PredictionConverter predictionConverter,
                        PredictionRepository predictionRepository,
                        DbEntityService dbEntityService,
                        LinkService linkService) {
        this.kafkaTemplate = kafkaTemplate;
        this.predictionService = predictionService;
        this.predictionConverter = predictionConverter;
        this.predictionRepository = predictionRepository;
        this.dbEntityService = dbEntityService;
        this.linkService = linkService;
    }

    private void sendMessageToKafka(String topic, String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message +
                        "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" +
                        message + "] due to : " + ex.getMessage());
            }
        });
    }

    public void sendPredictionToKafka(String message) {
        sendMessageToKafka("predictionInitTopic", message);
    }

    @KafkaListener(topics = "predictionInitTopic", groupId = "group")
    public void savePredictionAndSend(String message) throws Exception {
        System.out.println("Received Message in group: " + message);

        DataInput dataInput = new DataInput();
        dataInput.setData(List.of(message));
        PredictionPredictServiceOutput output = predictionService.getPredictionFromPredictService(dataInput);

        PredictionInput predictionInput = new PredictionInput();
        predictionInput.setPredictionText(message);
        predictionInput.setConfidence(output.getConfidence());
        predictionInput.setInformative(output.getBinaryLabel() == 1);

        Prediction predictionEntityInput = predictionConverter.convertToEntity(predictionInput);
        predictionEntityInput.setCreatedAt(LocalDateTime.now());
        Prediction prediction = predictionRepository.save(predictionEntityInput);

        KafkaDto kafkaDto = KafkaDto.builder()
                .predictionId(prediction.getId())
                .dataInput(dataInput)
                .build();

        // topic pre FE aby vedel ziskat data z id
        sendMessageToKafka("basePredictionTopic", prediction.getId().toString());
        sendMessageToKafka("predictionNERTopic", Base64Utils.encodeToBase64(kafkaDto));
    }

    @KafkaListener(topics = "predictionNERTopic", groupId = "group")
    public void listenNER(String message) throws Exception {
        KafkaDto kafkaDto = Base64Utils.decodeFromBase64(message, KafkaDto.class);
        DbEntityGetFromNerGroups groups = dbEntityService.getEntitiesFromNER(kafkaDto.getDataInput()).getGroups();
        dbEntityService.saveEntities(kafkaDto.getPredictionId(), predictionService.setDbEntitySaveEntitiesInputList(groups, null));
        sendMessageToKafka("linkTopic", Base64Utils.encodeToBase64(kafkaDto));
    }

    @KafkaListener(topics = "linkTopic", groupId = "group")
    public void listenLink(String message) throws Exception {
        KafkaDto kafkaDto = Base64Utils.decodeFromBase64(message, KafkaDto.class);
        DataInput dataInput = kafkaDto.getDataInput();

        LinkCountOutput linkCountOutput = linkService.getLinkCount(dataInput);
        if (linkCountOutput.getUrls().isEmpty() || linkCountOutput.getUrls() == null) {
            sendMessageToKafka("basePredictionTopic", kafkaDto.getPredictionId().toString());
        }

        // download links
        DataInput dataInputLinks = new DataInput();
        dataInputLinks.setData(linkCountOutput.getUrls());
        LinkDownloadOutputList linkDownloadOutputList = linkService.downloadFromLinks(dataInputLinks);

        // save links to prediction
        List<LinkInput> linkInputList = new ArrayList<>();
        for (LinkDownloadOutput linkDownloadOutput : linkDownloadOutputList.getOutputList()) {
            LinkInput linkInput = LinkInput.builder()
                    .originUrl(linkDownloadOutput.getOriginUrl())
                    .finalUrl(linkDownloadOutput.getFinalUrl())
                    .text(linkDownloadOutput.getText())
                    .html(linkDownloadOutput.getHtml())
                    .title(linkDownloadOutput.getTitle())
                    .otherInfo(linkDownloadOutput.getOtherInfo())
                    .domain(linkDownloadOutput.getDomain())
                    .publishedAt(linkDownloadOutput.getPublishedAt())
                    .extractedAt(linkDownloadOutput.getExtractedAt())
                    .build();
            linkInputList.add(linkInput);
        }
        List<LinkOutput> savedLinks = linkService.saveLinksToPrediction(linkInputList, kafkaDto.getPredictionId());

        // get entities for each link text and save
        List<DbEntityGetFromNerGroups> dbEntityGetFromNerGroupsList = new ArrayList<>();
        for (LinkOutput linkOutput : savedLinks) {
            DataInput dataLinkEntitiesInput = new DataInput();
            dataLinkEntitiesInput.setData(List.of(linkOutput.getText()));
            DbEntityGetFromNerGroups dataLinkEntitiesOutput = dbEntityService.getEntitiesFromNER(dataLinkEntitiesInput).getGroups();
            dbEntityGetFromNerGroupsList.add(dataLinkEntitiesOutput);

            dbEntityService.saveEntities(kafkaDto.getPredictionId(), predictionService.setDbEntitySaveEntitiesInputList(dataLinkEntitiesOutput, linkOutput.getId()));
        }

        Prediction prediction = predictionRepository.findById(kafkaDto.getPredictionId()).orElseThrow();
        sendMessageToKafka("finalPredictionTopic", prediction.getId().toString());
    }
}
