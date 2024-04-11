package com.pep.ProxyEntryPoint.config;

import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.api.ProcessInstanceApi;
import org.camunda.community.rest.client.api.TaskApi;
import org.camunda.community.rest.client.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CamundaConfig {

    ApiClient apiClient = new ApiClient();

    @Value("${camunda.uri}")
    private String camundaBaseUri;

    @Bean("localVarApiClient")
    public ApiClient apiClient() {
        return apiClient.setBasePath(camundaBaseUri);
    }

    @Bean
    public ProcessDefinitionApi processDefinitionApi() {
        return new ProcessDefinitionApi(apiClient);
    }

    @Bean
    public ProcessInstanceApi processInstanceApi() {
        return new ProcessInstanceApi(apiClient);
    }

    @Bean
    public TaskApi taskApi() {
        return new TaskApi(apiClient);
    }
}
