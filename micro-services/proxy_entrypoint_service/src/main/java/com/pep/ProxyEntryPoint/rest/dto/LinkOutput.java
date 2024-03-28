package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class LinkOutput {

    @JsonProperty
    private Long id;

    @JsonProperty
    private String originUrl;

    @JsonProperty
    private String finalUrl;

    @JsonProperty
    private String text;

    @JsonProperty
    private String html;

    @JsonProperty
    private String title;

    @JsonProperty
    private Object otherInfo;

    @JsonProperty
    private String domain;

    @JsonProperty
    private LocalDateTime publishedAt;

    @JsonProperty
    private LocalDateTime extractedAt;
}
