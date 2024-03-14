package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.DbEntityConverter;
import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.DbEntityRepository;
import com.pep.ProxyEntryPoint.rest.NotUsed;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DbEntityService extends AbstractService<NotUsed, DbEntityOutput, DbEntity, Long>{

    private final DbEntityRepository dbEntityRepository;
    private final DbEntityConverter dbEntityConverter;
    private final PredictionService predictionService;

    @Autowired
    protected DbEntityService(DbEntityRepository dbEntityRepository,
                              DbEntityConverter dbEntityConverter,
                              PredictionService predictionService) {
        super(dbEntityRepository, dbEntityConverter);
        this.dbEntityRepository = dbEntityRepository;
        this.dbEntityConverter = dbEntityConverter;
        this.predictionService = predictionService;
    }

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
//            dbEntity.setEntityTypeEnum
            dbEntity.setPrediction(prediction);
            dbEntity.setCreatedAt(LocalDate.now());

            DbEntity savedEntity = dbEntityRepository.save(dbEntity);
            outputList.add(dbEntityConverter.convertToOutput(savedEntity));
        }
        return outputList;
    }
}
