package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PredictionControllerApi {

    String TAG = "Prediction";

    @Operation(
            operationId = "createPrediction",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PredictionOutput.class))
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
            value = "/prediction",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default PredictionOutput createPrediction(@RequestBody @Valid PredictionInput predictionInput) {
        return new PredictionOutput();
    }

    @Operation(
            operationId = "ratePrediction",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PredictionOutput.class))
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
            value = "/prediction/{id}/rate/{rating}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default PredictionOutput ratePrediction(
            @PathVariable("id") Long id,
            @PathVariable Boolean rating
    ) {
        return new PredictionOutput();
    }
}
