package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionInput;
import com.pep.ProxyEntryPoint.rest.dto.PredictionOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface PredictionControllerApi {

    String TAG = "Prediction";

    /**
     * POST /prediction
     * Služba vytvorí záznam v prediction tabulke.
     *
     * @param predictionInput (required)
     * @return Služba úspešne vytvorila záznam v prediction tabulke a vrátila detail. (status code 200)
     * or Nesprávny vstup. (status code 400)
     * or Žiadateľ služby nie je autorizovaný. (status code 401)
     * or Operácia zamietnutá. (status code 403)
     * or Neočakavaná chyba aplikácie. (status code 200)
     */
    @Operation(
            operationId = "createLink",
            tags = {TAG},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne vytvorila záznam v prediction tabulke.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = PredictionOutput.class))
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
    @PostMapping(
            value = "/prediction",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default PredictionOutput createPrediction(@RequestBody @Valid PredictionInput predictionInput) {
        return new PredictionOutput();
    }
}
