package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class DbEntitySaveEntitiesInput {

    @JsonProperty
    private String name;

    @JsonProperty
    private String entityTypeEnumKey;

}
