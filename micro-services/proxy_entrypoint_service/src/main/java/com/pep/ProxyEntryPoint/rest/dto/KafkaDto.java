package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class KafkaDto {

    private Long predictionId;

    private DataInput dataInput;

}
