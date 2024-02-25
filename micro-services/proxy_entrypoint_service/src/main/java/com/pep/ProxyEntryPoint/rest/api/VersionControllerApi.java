package com.pep.ProxyEntryPoint.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;

public interface VersionControllerApi {

    @Operation(
            operationId = "getVersion",
            tags = {"Version"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne vratila verziu", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Nesprávny vstup.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Žiadateľ služby nie je autorizovaný.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    }),
                    @ApiResponse(responseCode = "403", description = "Operácia zamietnutá.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
                    })
            }
    )
    @GetMapping(
            value = "/version",
            produces = {"application/json"}
    )
    default String getVersion() {
        return "";
    }
}
