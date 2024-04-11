package com.pep.ProxyEntryPoint.service;

import com.pep.ProxyEntryPoint.rest.dto.CamundaStartProcessInput;
import com.pep.ProxyEntryPoint.rest.dto.CamundaStartProcessInputList;
import org.camunda.community.rest.client.api.ProcessDefinitionApi;
import org.camunda.community.rest.client.api.ProcessInstanceApi;
import org.camunda.community.rest.client.api.TaskApi;
import org.camunda.community.rest.client.dto.CompleteTaskDto;
import org.camunda.community.rest.client.dto.ProcessInstanceWithVariablesDto;
import org.camunda.community.rest.client.dto.StartProcessInstanceDto;
import org.camunda.community.rest.client.dto.TaskDto;
import org.camunda.community.rest.client.dto.VariableValueDto;
import org.camunda.community.rest.client.invoker.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CamundaService {

    private final ProcessDefinitionApi processDefinitionApi;
    private final ProcessInstanceApi processInstanceApi;
    private final TaskApi taskApi;

    @Value("${pep.service.url}")
    private String pepUrl;

    @Autowired
    public CamundaService(ProcessDefinitionApi processDefinitionApi,
                          ProcessInstanceApi processInstanceApi,
                          TaskApi taskApi) {
        this.processDefinitionApi = processDefinitionApi;
        this.processInstanceApi = processInstanceApi;
        this.taskApi = taskApi;
    }

    public ProcessInstanceWithVariablesDto startProcessByKey(String key, Map<String, VariableValueDto> variables) throws ApiException {
        return processDefinitionApi.startProcessInstanceByKey(key, new StartProcessInstanceDto().variables(variables));
    }

    public ProcessInstanceWithVariablesDto startProcessByKey(CamundaStartProcessInputList body, String key) throws ApiException {
        Map<String, VariableValueDto> variables = new HashMap<>();
        for (CamundaStartProcessInput input : body.getVariableList()) {
            variables.put(input.getName(), new VariableValueDto().value(input.getValue()));
        }
        variables.put("pepUrl", new VariableValueDto().value(pepUrl).type("String"));
        return startProcessByKey(key, variables);
    }

    public void submitUserTask(String taskId, Map<String, VariableValueDto> variables) throws ApiException {
        CompleteTaskDto completeTaskDto = new CompleteTaskDto().variables(variables);
        taskApi.complete(taskId, completeTaskDto);
    }

    // get user task by task id and process instance id
    public TaskDto getUserTask(String taskId) throws ApiException {
        return taskApi.getTask(taskId);
    }

    public void setProcessInstanceVariable(String processInstanceId, String varName, Object varValue, String type) throws ApiException {
        processInstanceApi.setProcessInstanceVariable(processInstanceId, varName, createVariableValue(varValue, type));
    }

    public VariableValueDto createVariableValue(Object value, String type) {
        return new VariableValueDto().value(value).type(type);
    }
}
