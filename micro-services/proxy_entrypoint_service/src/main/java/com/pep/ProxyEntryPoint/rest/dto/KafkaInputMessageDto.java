package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class KafkaInputMessageDto {
    private String objectType;
    private Long predictionId;
    private Long linkId;
    private String objectBase64;
}
