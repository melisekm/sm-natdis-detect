package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@SuperBuilder
public class LinkOutput {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String link;

    @JsonProperty
    private String body;

    @JsonProperty
    private String rawBody;

    @JsonProperty
    private String title;

    @JsonProperty
    private String otherInfo;

    @JsonProperty
    private LocalDate publishedAt;

    @JsonProperty
    private LocalDate createdAt;
}
