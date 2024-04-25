package com.pep.ProxyEntryPoint.controller;

import com.pep.ProxyEntryPoint.exception.DefaultException;
import com.pep.ProxyEntryPoint.rest.api.CamundaControllerApi;
import com.pep.ProxyEntryPoint.rest.dto.CamundaStartProcessInputList;
import com.pep.ProxyEntryPoint.service.CamundaService;
import org.camunda.community.rest.client.dto.ProcessInstanceWithVariablesDto;
import org.camunda.community.rest.client.dto.VariableValueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CamundaController implements CamundaControllerApi {

    private final CamundaService camundaService;

    @Autowired
    public CamundaController(CamundaService camundaService) {
        this.camundaService = camundaService;
    }

    @Override
    public ProcessInstanceWithVariablesDto startProcessByKey(CamundaStartProcessInputList body, String key) {
        try {
            return camundaService.startProcessByKey(body, key);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public Map<String, VariableValueDto> getProcessVariables(String processInstanceId) {
        try {
            return camundaService.getProcessInstanceVariables(processInstanceId);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public void submitUserTask(String taskId, CamundaStartProcessInputList body) {
        try {
            camundaService.submitUserTask(taskId, body);
        } catch (Exception e) {
            throw new DefaultException(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }
}
