package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.apiClient.ApiClient;
import com.pep.ProxyEntryPoint.converter.PredictionConverter;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityGetFromNerGroups;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkCountOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionPredictServiceOutput;
import com.pep.ProxyEntryPoint.util.ApiClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PredictionService extends AbstractService<PredictionInput, PredictionOutput, Prediction, Long>{

    private final PredictionRepository predictionRepository;
    private final PredictionConverter predictionConverter;
    private final ApiClient apiClient;
    private final JmsTemplate jmsTemplate;
    private final DbEntityService dbEntityService;
    private final LinkService linkService;

    @Value("${predict.service.url}")
    private String predictServiceUrl;

    @Autowired
    protected PredictionService(PredictionRepository predictionRepository,
                                PredictionConverter predictionConverter,
                                ApiClient apiClient,
                                JmsTemplate jmsTemplate,
                                DbEntityService dbEntityService,
                                LinkService linkService) {
        super(predictionRepository, predictionConverter);
        this.predictionRepository = predictionRepository;
        this.predictionConverter = predictionConverter;
        this.apiClient = apiClient;
        this.jmsTemplate = jmsTemplate;
        this.dbEntityService = dbEntityService;
        this.linkService = linkService;
    }

    @Override
    protected void postConvert(PredictionInput input, Prediction entity) {
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDateTime.now());
        } else {
            entity.setUpdatedAt(LocalDateTime.now());
        }
    }

    @Override
    protected void checkCreate(PredictionInput input) {

    }

    @Override
    protected void checkUpdate(Long aLong, PredictionInput input) {

    }

    @Override
    protected void checkDelete(Long aLong) {

    }

    @Override
    protected void checkGet(Long aLong) {

    }

    @Transactional
    public PredictionOutput ratePrediction(Long id, String rating) throws Exception {
        Prediction prediction = predictionRepository.findEntityById(id);
        prediction.setRating(Boolean.valueOf(rating));
        prediction.setUpdatedAt(LocalDateTime.now());
        predictionRepository.save(prediction);
        return predictionConverter.convertToOutput(prediction);
    }

    @Transactional
    public PredictionPredictServiceOutput getPredictionFromPredictService(DataInput input) throws Exception {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(predictServiceUrl + "/predict");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        List<LinkedHashMap<String, Object>> linkedHashMapList = apiClient.invokeApi(apiCallInput, List.class).getBody();
        return ApiClientUtils.convertLinkedHashMapToObject(linkedHashMapList, PredictionPredictServiceOutput.class);
    }

    @Transactional
    public Long getFlow(String input) throws Exception {
        // get prediction
        DataInput dataInput = new DataInput();
        dataInput.setData(List.of(input));

        PredictionPredictServiceOutput predictionPredictServiceOutput = getPredictionFromPredictService(dataInput);

        PredictionInput predictionInput = new PredictionInput();
        predictionInput.setPredictionText(input);
        predictionInput.setConfidence(predictionPredictServiceOutput.getConfidence());
        predictionInput.setInformative(predictionPredictServiceOutput.getBinaryLabel() == 1);

        Prediction predictionEntityInput = predictionConverter.convertToEntity(predictionInput);
        predictionEntityInput.setCreatedAt(LocalDateTime.now());
        Prediction prediction = predictionRepository.save(predictionEntityInput);

        if (Boolean.FALSE.equals(prediction.getInformative())) {
            return prediction.getId();
        }
        // get entities from NER
        DbEntitySaveEntitiesInputList dbEntitySaveEntitiesInputList = new DbEntitySaveEntitiesInputList();
        List<DbEntitySaveEntitiesInput> inputList = new ArrayList<>();
        dbEntitySaveEntitiesInputList.setInputList(inputList);

        DbEntityGetFromNerGroups groups = dbEntityService.getEntitiesFromNER(dataInput).getGroups();

        // save entities to prediction
        if (groups.getPLACE() != null) {
            for (String place : groups.getPLACE()) {
                dbEntitySaveEntitiesInputList.getInputList().add(DbEntitySaveEntitiesInput.builder().name(place).entityTypeEnumKey("PLACE").build());
            }
        }
        if (groups.getOTHER() != null) {
            for (String other : groups.getOTHER()) {
                dbEntitySaveEntitiesInputList.getInputList().add(DbEntitySaveEntitiesInput.builder().name(other).entityTypeEnumKey("OTHER").build());
            }
        }
        if (groups.getENTITY() != null) {
            for (String entity : groups.getENTITY()) {
                dbEntitySaveEntitiesInputList.getInputList().add(DbEntitySaveEntitiesInput.builder().name(entity).entityTypeEnumKey("ENTITY").build());
            }
        }
        if (groups.getTIME() != null) {
            for (String time : groups.getTIME()) {
                dbEntitySaveEntitiesInputList.getInputList().add(DbEntitySaveEntitiesInput.builder().name(time).entityTypeEnumKey("TIME").build());
            }
        }
        if (groups.getDATE() != null) {
            for (String date : groups.getDATE()) {
                dbEntitySaveEntitiesInputList.getInputList().add(DbEntitySaveEntitiesInput.builder().name(date).entityTypeEnumKey("DATE").build());
            }
        }
        dbEntityService.saveEntities(prediction.getId(), dbEntitySaveEntitiesInputList);

        // get link count
        LinkCountOutput linkCountOutput = linkService.getLinkCount(dataInput);
        if (linkCountOutput.getUrls().isEmpty() || linkCountOutput.getUrls() == null) {
            return prediction.getId();
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
        linkService.saveLinksToPrediction(linkInputList, prediction.getId());

        return prediction.getId();
    }
}
