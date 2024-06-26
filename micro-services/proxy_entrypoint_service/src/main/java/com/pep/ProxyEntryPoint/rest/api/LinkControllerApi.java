package com.pep.ProxyEntryPoint.rest.api;

import com.pep.ProxyEntryPoint.rest.dto.LinkCountOutput;
import com.pep.ProxyEntryPoint.rest.dto.LinkDownloadOutputList;
import com.pep.ProxyEntryPoint.rest.dto.LinkInput;
import com.pep.ProxyEntryPoint.rest.dto.LinkOutput;
import com.pep.ProxyEntryPoint.rest.dto.DataInput;
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

    @Operation(
            operationId = "createLink",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkOutput.class))
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
            value = "/links",
            produces = {"application/json"}
    )
    default ResponseEntity<List<LinkOutput>> findAllLinks() {
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @Operation(
            operationId = "getLinkCount",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkCountOutput.class))
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
            value = "/link/getCount",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default LinkCountOutput getLinkCount(
            @RequestBody DataInput dataInput)
    {
        return new LinkCountOutput();
    }

    @Operation(
            operationId = "downloadFromLinks",
            tags = {"Link"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Operation successful.", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = LinkDownloadOutputList.class))
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
            value = "/link/download",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default LinkDownloadOutputList downloadFromLinks(
            @RequestBody DataInput dataInput)
    {
        return new LinkDownloadOutputList();
    }

    @Operation(
            operationId = "downloadFromLinksCamunda",
            tags = {"Link"},
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
            value = "/link/download/{processInstanceId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default void downloadFromLinksCamunda(
            @PathVariable String processInstanceId,
            @RequestBody DataInput dataInput)
    {
    }

    @Operation(
            operationId = "saveLinksToPrediction",
            tags = {"Link"},
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
            value = "/link/saveToPrediction/{predictionId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default List<LinkOutput> saveLinksToPrediction(
            @RequestBody List<LinkInput> linkInputs,
            @PathVariable Long predictionId)
    {
        return List.of();
    }

    @Operation(
            operationId = "saveLinksToPredictionCamunda",
            tags = {"Link"},
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
            value = "/link/saveToPredictionCamunda/{predictionId}",
            produces = {"application/json"},
            consumes = {"application/json"}
    )
    default void saveLinksToPredictionCamunda(
            @RequestBody String linkInputs,
            @PathVariable Long predictionId)
    {
    }

}
