package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashMap;

@Data
@NoArgsConstructor
@SuperBuilder
public class PredictionPredictServiceOutput {

    private String label;

    @JsonProperty("binary_label")
    private int binaryLabel;
    private double confidence;

}
