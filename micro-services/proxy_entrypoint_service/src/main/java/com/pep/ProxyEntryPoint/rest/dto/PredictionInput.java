package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class PredictionInput {

    @JsonProperty
    private Boolean informative;

    @JsonProperty
    private Double confidence;

    @JsonProperty
    private String predictionText;

    @JsonProperty
    private Boolean rating;
}
