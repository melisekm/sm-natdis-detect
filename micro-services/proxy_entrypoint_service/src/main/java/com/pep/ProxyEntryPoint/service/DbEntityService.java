package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.apiClient.ApiClient;
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
    private final ApiClient apiClient;
    private final EntityTypeEnumRepository entityTypeEnumRepository;
    private final LinkRepository linkRepository;

    @Autowired
    protected DbEntityService(DbEntityRepository dbEntityRepository,
                              DbEntityConverter dbEntityConverter,
                              @Lazy PredictionService predictionService,
                              ApiClient apiClient,
                              EntityTypeEnumRepository entityTypeEnumRepository,
                              LinkRepository linkRepository) {
        super(dbEntityRepository, dbEntityConverter);
        this.dbEntityRepository = dbEntityRepository;
        this.dbEntityConverter = dbEntityConverter;
        this.predictionService = predictionService;
        this.apiClient = apiClient;
        this.entityTypeEnumRepository = entityTypeEnumRepository;
        this.linkRepository = linkRepository;
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

        List<LinkedHashMap<String, Object>> linkedHashMapList = apiClient.invokeApi(apiCallInput, List.class).getBody();
        return ApiClientUtils.convertLinkedHashMapToObject(linkedHashMapList, DbEntityGetFromNerOutput.class);
    }
}
