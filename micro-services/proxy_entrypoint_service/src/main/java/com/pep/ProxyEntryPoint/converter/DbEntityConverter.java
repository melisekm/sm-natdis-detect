package com.pep.ProxyEntryPoint.converter;

import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.rest.NotUsed;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import org.springframework.stereotype.Component;

@Component
public class DbEntityConverter extends AbstractConverter<NotUsed, DbEntityOutput, DbEntity>{
    @Override
    public DbEntity convertToEntity(NotUsed input) {
        return null;
    }

    @Override
    public DbEntityOutput convertToOutput(DbEntity entity) {
        DbEntityOutput dbEntityOutput = new DbEntityOutput();
        dbEntityOutput.setId(entity.getId());
        dbEntityOutput.setName(entity.getName());
        dbEntityOutput.setEntityTypeEnumKey(entity.getEntityTypeEnum().getKey());
        dbEntityOutput.setEntityTypeEnumValue(entity.getEntityTypeEnum().getValue());
        dbEntityOutput.setPredictionId(entity.getPrediction().getId());
        dbEntityOutput.setLinkId(entity.getLink() != null ? entity.getLink().getId() : null);
        dbEntityOutput.setCreatedAt(entity.getCreatedAt());
        return dbEntityOutput;
    }
}
