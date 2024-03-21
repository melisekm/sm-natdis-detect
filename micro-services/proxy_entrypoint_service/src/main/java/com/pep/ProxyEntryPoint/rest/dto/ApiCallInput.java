package com.pep.ProxyEntryPoint.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpMethod;

import java.util.Map;

@Data
@NoArgsConstructor
@SuperBuilder
public class ApiCallInput {
    private String urlPath;
    private HttpMethod method;
    private Map<String, Object> bodyRequest;
    private Map<String, Object> queryParams;
}
