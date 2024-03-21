package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.apiClient.ApiClient;
import com.pep.ProxyEntryPoint.converter.PredictionConverter;
import com.pep.ProxyEntryPoint.model.entity.Prediction;
import com.pep.ProxyEntryPoint.model.repository.PredictionRepository;
import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionGetPredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class PredictionService extends AbstractService<PredictionInput, PredictionOutput, Prediction, Long>{

    private final PredictionRepository predictionRepository;
    private final PredictionConverter predictionConverter;
    private final ApiClient apiClient;

    @Value("${predict.service.url}")
    private String predictServiceUrl;

    @Autowired
    protected PredictionService(PredictionRepository predictionRepository,
                                PredictionConverter predictionConverter,
                                ApiClient apiClient) {
        super(predictionRepository, predictionConverter);
        this.predictionRepository = predictionRepository;
        this.predictionConverter = predictionConverter;
        this.apiClient = apiClient;
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

    @Transactional
    public PredictionOutput ratePrediction(Long id, Boolean rating) throws Exception {
        Prediction prediction = predictionRepository.findEntityById(id);
        prediction.setRating(rating);
        predictionRepository.save(prediction);
        return predictionConverter.convertToOutput(prediction);
    }

    @Transactional
    public PredictionOutput getPredictionFromPredictService(PredictionGetPredictionInput input) {
        ApiCallInput apiCallInput = new ApiCallInput();
        apiCallInput.setMethod(HttpMethod.POST);
        apiCallInput.setUrlPath(predictServiceUrl + "/predict");
        
        Map<String, Object> bodyRequest = new HashMap<>();
        bodyRequest.put("data", input.getData());
        apiCallInput.setBodyRequest(bodyRequest);

        return apiClient.invokeApi(apiCallInput, PredictionOutput.class).getBody();
    }
}
