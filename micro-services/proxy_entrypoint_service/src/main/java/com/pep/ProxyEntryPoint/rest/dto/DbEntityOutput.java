package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
public class DbEntityOutput {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String entityTypeEnumKey;

    @JsonProperty
    private Long predictionId;

    @JsonProperty
    private Long linkId;

    @JsonProperty
    private LocalDate createdAt;
}
