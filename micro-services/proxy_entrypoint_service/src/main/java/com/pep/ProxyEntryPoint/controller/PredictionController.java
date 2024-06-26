package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.exception.DefaultException;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.rest.api.PredictionControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionPredictServiceOutput;
import com.pep.ProxyEntryPoint.service.PredictionService;
import lombok.extern.log4j.Log4j2;
import org.camunda.community.rest.client.dto.ProcessInstanceWithVariablesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
public class PredictionController extends AbstractController<PredictionInput, PredictionOutput, Prediction, Long> implements PredictionControllerApi {

    private final PredictionService predictionService;

    @Autowired
    protected PredictionController(PredictionService predictionService) {
        super(predictionService);
        this.predictionService = predictionService;
    }

    private static final String ERROR_ENTITY_UPDATE = "Error updating prediction.";

    @Override
    protected String getErrorEntityCreate() {
        return null;
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
        return ERROR_ENTITY_UPDATE;
    }

    @Override
    protected String getErrorEntityAll() {
        return null;
    }

    @Override
    public PredictionOutput createPrediction(PredictionInput input) {
        return super.create(input).getBody();
    }

    @Override
    public PredictionOutput getPrediction(Long id) {
        return super.get(id).getBody();
    }

    @Override
    public ResponseEntity<List<PredictionOutput>> findAllPredictions() {
        return super.findAll();
    }

    @Override
    public PredictionOutput ratePrediction(Long id, String rating) {
        try {
            return predictionService.ratePrediction(id, rating);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    @Override
    public PredictionPredictServiceOutput getPredictionFromPredictService(DataInput input) {
        try {
            return predictionService.getPredictionFromPredictService(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    @Override
    public Long getFlow(String input) {
        try {
            return predictionService.getFlow(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }

    @Override
    public void startPredictionProcess(String input) {
        try {
            predictionService.startPredictionProcess(input);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, getErrorEntityUpdate(), e);
        }
    }
}
