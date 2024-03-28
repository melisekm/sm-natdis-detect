package com.pep.ProxyEntryPoint.helpers;

import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;

import java.time.LocalDate;

public class PredictionHelper {

    private static final Double CONFIDENCE = 0.8;
    private static final String PREDICTION_TEXT = "This is a prediction";
    private static final Boolean RATING = true;
    private static final Boolean INFORMATIVE = true;

    public static PredictionInput createPredictionInput() {
        return PredictionInput.builder()
                .informative(INFORMATIVE)
                .confidence(CONFIDENCE)
                .predictionText(PREDICTION_TEXT)
                .rating(RATING)
                .build();
    }

    public static Prediction createPrediction() {
        return Prediction.builder()
                .informative(INFORMATIVE)
                .confidence(CONFIDENCE)
                .predictionText(PREDICTION_TEXT)
                .rating(RATING)
                .build();
    }
}
