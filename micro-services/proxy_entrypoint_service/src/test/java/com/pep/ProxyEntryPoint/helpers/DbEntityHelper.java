package com.pep.ProxyEntryPoint.helpers;

import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.model.entity.Link;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DbEntityHelper {

    private static final String NAME = "name";
    private static final LocalDate DATE = LocalDate.now();

    public static DbEntity createDbEntity(Prediction prediction, Link link) {
        DbEntity dbEntity = new DbEntity();
        dbEntity.setName(NAME);
        dbEntity.setPrediction(prediction);
        dbEntity.setLink(link);
        dbEntity.setCreatedAt(DATE);
        return dbEntity;
    }

    public static DbEntitySaveEntitiesInputList createDbEntitySaveEntitiesInputList() {
        DbEntitySaveEntitiesInputList dbEntitySaveEntitiesInputList = new DbEntitySaveEntitiesInputList();
        DbEntitySaveEntitiesInput dbEntitySaveEntitiesInput = new DbEntitySaveEntitiesInput();
        dbEntitySaveEntitiesInput.setName(NAME);
//        dbEntitySaveEntitiesInput.setEntityTypeEnumKey(NAME);

        List<DbEntitySaveEntitiesInput> inputList = new ArrayList<>();
        inputList.add(dbEntitySaveEntitiesInput);
        dbEntitySaveEntitiesInputList.setInputList(inputList);

        return dbEntitySaveEntitiesInputList;
    }
}
