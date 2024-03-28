package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class DbEntityGetFromNerGroups {

    @JsonProperty("PLACE")
    private List<String> PLACE;

    @JsonProperty("OTHER")
    private List<String> OTHER;

    @JsonProperty("ENTITY")
    private List<String> ENTITY;

    @JsonProperty("TIME")
    private List<String> TIME;

    @JsonProperty("DATE")
    private List<String> DATE;

}
