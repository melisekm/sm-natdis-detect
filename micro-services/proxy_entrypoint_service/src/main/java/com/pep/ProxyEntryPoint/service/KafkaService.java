package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.PredictionConverter;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.*;
import com.pep.ProxyEntryPoint.util.ApiClientUtils;
import com.pep.ProxyEntryPoint.util.Base64Utils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
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

    @Value("${ner.topic}")
    private String nerTopic;

    @Value("${link.topic}")
    private String linkTopic;

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
                System.out.println("Message acknowledged by Kafka.");
            } else {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = "${db.topic}", groupId = "group")
    @Transactional
    public void listenDb(String message) throws Exception {
        System.out.println("DB TOPIC: Received Message.");
        KafkaInputMessageDto kafkaInputMessageDto = Base64Utils.decodeFromBase64(
                message,
                KafkaInputMessageDto.class
        );

        System.out.println("Decoded message.");
        LinkedHashMap<String, Object> linkedHashMap = Base64Utils.decodeFromBase64(
                kafkaInputMessageDto.getObjectBase64(),
                LinkedHashMap.class
        );
        switch (kafkaInputMessageDto.getObjectType()) {
            case "prediction":
                this.handlePrediction(kafkaInputMessageDto, linkedHashMap);
                break;
            case "ner":
                this.handleNER(kafkaInputMessageDto, linkedHashMap);
                break;
            case "link":
                this.handleLink(kafkaInputMessageDto, linkedHashMap);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + kafkaInputMessageDto.getObjectType());
        }

    }

    private void handlePrediction(KafkaInputMessageDto kafkaInputMessageDto,
                                  LinkedHashMap<String, Object> linkedHashMap) throws Exception {
        PredictionPredictServiceOutput output = ApiClientUtils.convertSingleLinkedHashMapToObject(
                linkedHashMap,
                PredictionPredictServiceOutput.class
        );
        System.out.println("Received prediction in predict handler: " + output);
        PredictionInput predictionInput = new PredictionInput();
        predictionInput.setPredictionText(kafkaInputMessageDto.getMessage());
        predictionInput.setConfidence(output.getConfidence());
        predictionInput.setInformative(output.getBinaryLabel() == 1);
        Prediction predictionEntityInput = predictionConverter.convertToEntity(predictionInput);
        predictionEntityInput.setCreatedAt(LocalDateTime.now());
        Prediction prediction = predictionRepository.save(predictionEntityInput);

        System.out.println("Saved prediction to DB: " + prediction);
        if (Boolean.FALSE.equals(prediction.getInformative())) {
            System.out.println("Prediction is not informative. Skipping NER and Link");
            return;
        }

        DataInput dataInput = new DataInput();
        dataInput.setData(List.of(prediction.getPredictionText()));
        KafkaOutputMessageDto kafkaOutputMessageDto = KafkaOutputMessageDto.builder()
                .predictionId(prediction.getId())
                .dataInput(dataInput)
                .build();
        System.out.println("Sending message to NER topic.");
        sendMessageToKafka(this.nerTopic, Base64Utils.encodeToBase64(kafkaOutputMessageDto));
        System.out.println("Sent message to NER topic.");
    }

    private void handleNER(KafkaInputMessageDto kafkaInputMessageDto, LinkedHashMap<String, Object> linkedHashMap) throws Exception {
        System.out.println("Received linkedhashmap in NER handler.");
        DbEntityGetFromNerOutput output = ApiClientUtils.convertSingleLinkedHashMapToObject(
                linkedHashMap,
                DbEntityGetFromNerOutput.class
        );
        System.out.println("Received entities in  NER handler.");
        dbEntityService.saveEntities(
                kafkaInputMessageDto.getPredictionId(),
                predictionService.setDbEntitySaveEntitiesInputList(
                        output.getGroups(),
                        kafkaInputMessageDto.getLinkId()
                )
        );

        System.out.println("Saved entities to DB");
        if (kafkaInputMessageDto.getLinkId() != null) {
            System.out.println("LinkId is not null. Skipping recursive NER and Link retrieval.");
            return;
        }
        System.out.println(kafkaInputMessageDto.getPredictionId());
        Prediction prediction = predictionService.findEntityById(kafkaInputMessageDto.getPredictionId());
        System.out.println("Found associated prediction: " + prediction);

        DataInput dataInput = new DataInput();
        dataInput.setData(List.of(prediction.getPredictionText()));
        KafkaOutputMessageDto kafkaOutputMessageDto = KafkaOutputMessageDto.builder()
                .predictionId(kafkaInputMessageDto.getPredictionId())
                .dataInput(dataInput)
                .build();
        System.out.println("Sending message to Link topic.");
        sendMessageToKafka(this.linkTopic, Base64Utils.encodeToBase64(kafkaOutputMessageDto));
        System.out.println("Sent message to Link topic.");
    }

    private void handleLink(KafkaInputMessageDto kafkaInputMessageDto, LinkedHashMap<String, Object> linkedHashMap) throws Exception {
        System.out.println("Received linkedhashmap in Link handler.");
        LinkDownloadOutput linkDownloadOutput = linkService.convertSingleLinkDownloadOutput(linkedHashMap);
        System.out.println("Received link in Link handler.");

        List<LinkInput> linkInputList = new ArrayList<>();
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
        System.out.println("Saving links to DB. PredictionId: " + kafkaInputMessageDto.getPredictionId());
        List<LinkOutput> savedLinks = linkService.saveLinksToPrediction(linkInputList, kafkaInputMessageDto.getPredictionId());
        System.out.println("Saved links to DB. Sending NER request for each link text.");

        for (LinkOutput linkOutput : savedLinks) {
            DataInput dataLinkEntitiesInput = new DataInput();
            dataLinkEntitiesInput.setData(List.of(linkOutput.getText()));
            KafkaOutputMessageDto kafkaOutputMessageDto = KafkaOutputMessageDto.builder()
                    .predictionId(kafkaInputMessageDto.getPredictionId())
                    .linkId(linkOutput.getId())
                    .dataInput(dataLinkEntitiesInput)
                    .build();
            System.out.println("Sending message to NER topic.");
            sendMessageToKafka(this.nerTopic, Base64Utils.encodeToBase64(kafkaOutputMessageDto));
            System.out.println("Sent message to NER topic.");
        }
    }
}
