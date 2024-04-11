package com.pep.ProxyEntryPoint.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.internal.LinkedTreeMap;
import com.pep.ProxyEntryPoint.apiClient.MSApiClient;
import com.pep.ProxyEntryPoint.converter.DbEntityConverter;
import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.DbEntityRepository;
import com.pep.ProxyEntryPoint.model.repository.EntityTypeEnumRepository;
import com.pep.ProxyEntryPoint.model.repository.LinkRepository;
import com.pep.ProxyEntryPoint.rest.NotUsed;
import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityGetFromNerOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.util.ApiClientUtils;
import com.pep.ProxyEntryPoint.util.Base64Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DbEntityService extends AbstractService<NotUsed, DbEntityOutput, DbEntity, Long>{

    private final DbEntityRepository dbEntityRepository;
    private final DbEntityConverter dbEntityConverter;
    private final PredictionService predictionService;
    private final MSApiClient MSApiClient;
    private final EntityTypeEnumRepository entityTypeEnumRepository;
    private final LinkRepository linkRepository;
    private final CamundaService camundaService;

    @Autowired
    protected DbEntityService(DbEntityRepository dbEntityRepository,
                              DbEntityConverter dbEntityConverter,
                              @Lazy PredictionService predictionService,
                              MSApiClient MSApiClient,
                              EntityTypeEnumRepository entityTypeEnumRepository,
                              LinkRepository linkRepository,
                              CamundaService camundaService) {
        super(dbEntityRepository, dbEntityConverter);
        this.dbEntityRepository = dbEntityRepository;
        this.dbEntityConverter = dbEntityConverter;
        this.predictionService = predictionService;
        this.MSApiClient = MSApiClient;
        this.entityTypeEnumRepository = entityTypeEnumRepository;
        this.linkRepository = linkRepository;
        this.camundaService = camundaService;
    }

    @Value("${ner.service.url}")
    private String nerServiceUrl;

    @Override
    protected void postConvert(NotUsed input, DbEntity entity) {

    }

    @Override
    protected void checkCreate(NotUsed input) {

    }

    @Override
    protected void checkUpdate(Long aLong, NotUsed input) {

    }

    @Override
    protected void checkDelete(Long aLong) {

    }

    @Override
    protected void checkGet(Long aLong) {

    }

    @Transactional
    public List<DbEntityOutput> saveEntities(Long predictionId, DbEntitySaveEntitiesInputList input) throws Exception {
        List<DbEntityOutput> outputList = new ArrayList<>();
        for (DbEntitySaveEntitiesInput entity : input.getInputList()) {
            Prediction prediction = predictionService.findEntityById(predictionId);

            DbEntity dbEntity = new DbEntity();
            dbEntity.setName(entity.getName());
            dbEntity.setEntityTypeEnum(entityTypeEnumRepository.findByKey(entity.getEntityTypeEnumKey()));
            dbEntity.setLink(entity.getLinkId() != null ? linkRepository.findEntityById(entity.getLinkId()) : null);
            dbEntity.setPrediction(prediction);
            dbEntity.setCreatedAt(LocalDate.now());

            DbEntity savedEntity = dbEntityRepository.save(dbEntity);
            outputList.add(dbEntityConverter.convertToOutput(savedEntity));
        }
        return outputList;
    }

    @Transactional
    public DbEntityGetFromNerOutput getEntitiesFromNER(DataInput input) throws Exception {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(nerServiceUrl + "/analyze");

        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        List<LinkedHashMap<String, Object>> linkedHashMapList = MSApiClient.invokeApi(apiCallInput, List.class).getBody();
        return ApiClientUtils.convertLinkedHashMapToObject(linkedHashMapList, DbEntityGetFromNerOutput.class);
    }

    @Transactional
    public void getEntitiesFromNERCamunda(String input, String processInstanceId) throws Exception {
        List<LinkedTreeMap> decodedList = Base64Utils.decodeFromBase64(input, List.class);

        List<String> linkInputList = new ArrayList<>();
        for (LinkedTreeMap map : decodedList) {
            linkInputList.add((String) map.get("text"));
        }

        DataInput dataInput = new DataInput();
        dataInput.setData(linkInputList);

        DbEntityGetFromNerOutput output = getEntitiesFromNER(dataInput);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("value", Base64Utils.encodeToBase64(output));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = objectMapper.writeValueAsString(jsonMap);

        camundaService.setProcessInstanceVariable(processInstanceId, "dbEntityGetFromNerOutput", jsonValue, "Json");
    }
}
