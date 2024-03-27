package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PredictionPredictServiceOutput {

    private String label;
    private int binaryLabel;
    private double confidence;
}
