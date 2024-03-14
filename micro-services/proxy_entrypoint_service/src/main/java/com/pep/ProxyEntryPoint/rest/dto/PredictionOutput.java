package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
public class PredictionOutput {

    @JsonProperty
    private Long id;

    @JsonProperty
    private Boolean informative;

    @JsonProperty
    private Double confidence;

    @JsonProperty
    private String predictionText;

    @JsonProperty
    private LocalDate createdAt;

    @JsonProperty
    private LocalDate updatedAt;

    @JsonProperty
    private Boolean rating;
}
