package com.pep.ProxyEntryPoint.converter;

import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PredictionConverter extends AbstractConverter<PredictionInput, PredictionOutput, Prediction>{

    private final LinkConverter linkConverter;
    private final DbEntityConverter dbEntityConverter;

    @Autowired
    public PredictionConverter(LinkConverter linkConverter,
                               DbEntityConverter dbEntityConverter) {
        this.linkConverter = linkConverter;
        this.dbEntityConverter = dbEntityConverter;
    }

    @Override
    public Prediction convertToEntity(PredictionInput input) {
        Prediction prediction = new Prediction();
        prediction.setInformative(input.getInformative());
        prediction.setConfidence(input.getConfidence());
        prediction.setPredictionText(input.getPredictionText());
        prediction.setRating(input.getRating());
        return prediction;
    }

    @Override
    public PredictionOutput convertToOutput(Prediction entity) {
        PredictionOutput predictionOutput = new PredictionOutput();
        predictionOutput.setId(entity.getId());
        predictionOutput.setInformative(entity.getInformative());
        predictionOutput.setConfidence(entity.getConfidence());
        predictionOutput.setPredictionText(entity.getPredictionText());
        predictionOutput.setCreatedAt(entity.getCreatedAt());
        predictionOutput.setUpdatedAt(entity.getUpdatedAt());
        predictionOutput.setRating(entity.getRating());
        predictionOutput.setLinks(linkConverter.convertToOutputList(entity.getLinks()));
        predictionOutput.setEntities(dbEntityConverter.convertToOutputList(entity.getEntities()));
        return predictionOutput;
    }
}
