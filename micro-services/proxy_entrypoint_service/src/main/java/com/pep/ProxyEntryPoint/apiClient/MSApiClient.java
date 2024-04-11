package com.pep.ProxyEntryPoint.apiClient;

import com.pep.ProxyEntryPoint.rest.dto.ApiCallInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.Map;

@Component
public class MSApiClient {

    private final RestTemplate restTemplate;

    @Autowired
    public MSApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public <T> ResponseEntity<T> invokeApi(ApiCallInput input, Class<T> responseType) {
        HttpMethod httpMethod = input.getMethod();

        if (httpMethod == null) {
            throw new IllegalArgumentException("Invalid HTTP method");
        }

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(input.getUrlPath());

        if (input.getQueryParams() != null) {
            MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
            for (Map.Entry<String, Object> entry : input.getQueryParams().entrySet()) {
                String key = entry.getKey();
                String value = null;
                if (entry.getValue() != null) {
                    value = entry.getValue().toString();
                }
                queryParams.add(key, value);
            }
            uriBuilder.queryParams(queryParams);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<?> requestEntity = new HttpEntity<>(input.getBodyRequest(), headers);
        final String url = uriBuilder.toUriString();

        return restTemplate.exchange(url, httpMethod, requestEntity, responseType);
    }
}
