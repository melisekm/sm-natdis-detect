package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;

    @JsonProperty
    private LocalDateTime updatedAt;

    @JsonProperty
    private Boolean rating;

    @JsonProperty
    private List<LinkOutput> links;

    @JsonProperty
    private List<DbEntityOutput> entities;
}
