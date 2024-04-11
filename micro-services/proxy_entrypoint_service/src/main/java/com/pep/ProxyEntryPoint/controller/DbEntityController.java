package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.exception.DefaultException;
import com.pep.ProxyEntryPoint.model.entity.DbEntity;
import com.pep.ProxyEntryPoint.rest.NotUsed;
import com.pep.ProxyEntryPoint.rest.api.DbEntityControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityGetFromNerOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.service.DbEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DbEntityController extends AbstractController<NotUsed, DbEntityOutput, DbEntity, Long> implements DbEntityControllerApi {

    private final DbEntityService dbEntityService;

    @Autowired
    public DbEntityController(DbEntityService dbEntityService) {
        super(dbEntityService);
        this.dbEntityService = dbEntityService;
    }

    private static final String ERROR_ENTITY_CREATE = "Error creating entity.";

    @Override
    protected String getErrorEntityCreate() {
        return ERROR_ENTITY_CREATE;
    }

    @Override
    protected String getErrorEntityGet() {
        return null;
    }

    @Override
    protected String getErrorEntityDelete() {
        return null;
    }

    @Override
    protected String getErrorEntityUpdate() {
        return null;
    }

    @Override
    protected String getErrorEntityAll() {
        return null;
    }

    @Override
    public List<DbEntityOutput> saveEntities(Long predictionId, DbEntitySaveEntitiesInputList input) {
        try {
            return dbEntityService.saveEntities(predictionId, input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityCreate(), e);
        }
    }

    @Override
    public DbEntityGetFromNerOutput getEntitiesFromNER(DataInput input) {
        try {
            return dbEntityService.getEntitiesFromNER(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityCreate(), e);
        }
    }

    @Override
    public void getEntitiesFromNERCamunda(String input, String processInstanceId) {
        try {
            dbEntityService.getEntitiesFromNERCamunda(input, processInstanceId);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityCreate(), e);
        }
    }
}
