package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.CamundaStartProcessInputList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.camunda.community.rest.client.dto.ProcessInstanceWithVariablesDto;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CamundaControllerApi {

    String TAG = "Camunda";

    @Operation(
            operationId = "startProcessByKey",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceWithVariablesDto.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad request.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Forbidden.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @PostMapping(
            value = "/camunda/startProcessByKey/{key}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default ProcessInstanceWithVariablesDto startProcessByKey(
            @RequestBody CamundaStartProcessInputList body,
            @PathVariable String key
            ) {
        return new ProcessInstanceWithVariablesDto();
    }
}
