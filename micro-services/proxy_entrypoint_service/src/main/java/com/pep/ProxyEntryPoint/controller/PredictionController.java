package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.rest.api.PredictionControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import com.pep.ProxyEntryPoint.service.PredictionService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
public class PredictionController extends AbstractController<PredictionInput, PredictionOutput, Prediction, Long> implements PredictionControllerApi {

    @Autowired
    protected PredictionController(PredictionService predictionService) {
        super(predictionService);
    }

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
        return null;
    }

    @Override
    protected String getErrorEntityAll() {
        return null;
    }

    @Override
    public PredictionOutput createPrediction(PredictionInput input) {
        return super.create(input).getBody();
    }
}
