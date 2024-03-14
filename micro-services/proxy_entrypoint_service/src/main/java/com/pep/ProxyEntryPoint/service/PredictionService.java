package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.converter.PredictionConverter;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PredictionService extends AbstractService<PredictionInput, PredictionOutput, Prediction, Long>{

    protected PredictionService(PredictionRepository predictionRepository, PredictionConverter predictionConverter) {
        super(predictionRepository, predictionConverter);
    }

    @Override
    protected void postConvert(PredictionInput input, Prediction entity) {
        if (entity.getId() == null) {
            entity.setCreatedAt(LocalDate.now());
        } else {
            entity.setUpdatedAt(LocalDate.now());
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
}
