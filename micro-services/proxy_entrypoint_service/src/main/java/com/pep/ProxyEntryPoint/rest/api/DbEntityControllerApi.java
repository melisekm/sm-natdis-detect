package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.DbEntityGetFromNerOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntityOutput;
import com.pep.ProxyEntryPoint.rest.dto.DbEntitySaveEntitiesInputList;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public interface DbEntityControllerApi {

    String TAG = "Entity";

    @Operation(
            operationId = "saveEntities",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
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
            value = "/entity/saveEntities/{predictionId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default List<DbEntityOutput> saveEntities(
            @PathVariable("predictionId") Long predictionId,
            @RequestBody @Valid DbEntitySaveEntitiesInputList input
    ) {
        return new ArrayList<>();
    }

    @Operation(
            operationId = "getEntitiesFromNER",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = DbEntityGetFromNerOutput.class))
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
            value = "/entity/getFromNER",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default DbEntityGetFromNerOutput getEntitiesFromNER(
            @RequestBody @Valid DataInput input
            ) {
        return new DbEntityGetFromNerOutput();
    }

    @Operation(
            operationId = "getEntitiesFromNERCamunda",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))
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
            value = "/entity/getFromNER/{processInstanceId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default void getEntitiesFromNERCamunda(
            @RequestBody String input,
            @PathVariable String processInstanceId
    ) {
    }
}
