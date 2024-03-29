package com.pep.ProxyEntryPoint;

import com.pep.ProxyEntryPoint.controller.DbEntityController;
import com.pep.ProxyEntryPoint.helpers.DbEntityHelper;
import com.pep.ProxyEntryPoint.helpers.LinkHelper;
import com.pep.ProxyEntryPoint.helpers.PredictionHelper;
import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.DbEntityRepository;
import com.pep.ProxyEntryPoint.model.repository.LinkRepository;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DbEntityTest {

    @Autowired
    private DbEntityController dbEntityController;
    @Autowired
    private DbEntityRepository dbEntityRepository;
    @Autowired
    private PredictionRepository predictionRepository;
    @Autowired
    private LinkRepository linkRepository;

    private DbEntity savedDbEntity;
    private Prediction savedPrediction;
    private Link savedLink;

    @BeforeEach
    @Transactional
    public void setUp() {
//        savedPrediction = PredictionHelper.createPrediction();
//        predictionRepository.save(savedPrediction);
//
//        savedLink = LinkHelper.createLink();
//        linkRepository.save(savedLink);
//
//        savedDbEntity = DbEntityHelper.createDbEntity(savedPrediction, savedLink);
//        dbEntityRepository.save(savedDbEntity);
    }

    @Test
    @Transactional
    void saveEntitiesTest() {
//        DbEntitySaveEntitiesInputList inputList = DbEntityHelper.createDbEntitySaveEntitiesInputList();
//
//        List<DbEntityOutput> outputList = dbEntityController.saveEntities(savedPrediction.getId(), inputList);
//        assertNotNull(outputList);
//        assertNotEquals(0, outputList.size());
//        DbEntityOutput output = outputList.get(0);
//        assertNotNull(output);
//        assertEquals(savedDbEntity.getName(), output.getName());
//        assertEquals(savedDbEntity.getPrediction().getId(), output.getPredictionId());
    }
}
