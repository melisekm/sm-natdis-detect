package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class PredictionPredicServiceOutputList {
    private List<PredictionPredictServiceOutput> predictions;
}
