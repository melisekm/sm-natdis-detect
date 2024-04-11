package com.pep.ProxyEntryPoint.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class LinkInput {

    @NotNull
//    @JsonProperty("orig_url")
    private String originUrl;

//    @JsonProperty("final_url")
    private String finalUrl;

    @JsonProperty
    private String text;

    @JsonProperty
    private String html;

    @JsonProperty
    private String title;

//    @JsonProperty("other_info")
    private Map<String, Object> otherInfo;

    @JsonProperty
    private String domain;

//    @JsonProperty("published_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime publishedAt;

//    @JsonProperty("extracted_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime extractedAt;

}
