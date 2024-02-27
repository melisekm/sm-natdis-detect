package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

public interface LinkControllerApi {

    default Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }

    /**
     * POST /link
     * Služba vytvorí záznam v link tabulke.
     *
     * @param linkInput (required)
     * @return Služba úspešne vytvorila záznam v číslach podnikov a vrátila detail. (status code 200)
     * or Nesprávny vstup. (status code 400)
     * or Žiadateľ služby nie je autorizovaný. (status code 401)
     * or Operácia zamietnutá. (status code 403)
     * or Neočakavaná chyba aplikácie. (status code 200)
     */
    @Operation(
            operationId = "createLink",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne vytvorila záznam v čislovacom plane.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
            value = "/link",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default ResponseEntity<LinkOutput> createLink(@RequestBody @Valid LinkInput linkInput) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "getLink",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne vratila link", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
            value = "/link/{id}",
            produces = {"application/json"}
    )
    default ResponseEntity<LinkOutput> getLink(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "updateLink",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne updatla link", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
    @PutMapping(
            value = "/link/{id}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default ResponseEntity<LinkOutput> updateLink(@PathVariable Long id, @RequestBody @Valid LinkInput linkInput) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "deleteLink",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba úspešne vymazala link", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = void.class))
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
    @DeleteMapping(
            value = "/link/{id}",
            produces = {"application/json"}
    )
    default ResponseEntity<Void> deleteLink(@PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "findAllLinks",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Služba vrátila všetky linky", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))
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
            value = "/links",
            produces = {"application/json"}
    )
    default ResponseEntity<List<LinkOutput>> findAllLinks() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}
