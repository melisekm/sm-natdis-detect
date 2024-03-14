package com.pep.ProxyEntryPoint;

import com.pep.ProxyEntryPoint.controller.PredictionController;
import com.pep.ProxyEntryPoint.helpers.PredictionHelper;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class PredictionTest {

    @Autowired
    private PredictionController predictionController;
    @Autowired
    private PredictionRepository predictionRepository;

    private Prediction savedPrediction;

    @BeforeEach
    @Transactional
    public void setUp() {
        savedPrediction = PredictionHelper.createPrediction();
        predictionRepository.save(savedPrediction);
    }

    @Test
    @Transactional
    void createPrediction() {
        PredictionInput predictionInput = PredictionHelper.createPredictionInput();

        PredictionOutput predictionOutput = predictionController.createPrediction(predictionInput);
        assertNotNull(predictionOutput);

        assertEquals(savedPrediction.getInformative(), predictionOutput.getInformative());
        assertEquals(savedPrediction.getConfidence(), predictionOutput.getConfidence());
        assertEquals(savedPrediction.getPredictionText(), predictionOutput.getPredictionText());
        assertEquals(savedPrediction.getRating(), predictionOutput.getRating());
    }
}
