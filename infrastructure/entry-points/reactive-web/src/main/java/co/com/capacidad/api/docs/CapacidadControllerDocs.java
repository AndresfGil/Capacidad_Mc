package co.com.capacidad.api.docs;

import co.com.capacidad.api.CapacidadHandler;
import co.com.capacidad.api.dto.CapacidadRequestDto;
import co.com.capacidad.api.dto.CapacidadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

public interface CapacidadControllerDocs {

    @RouterOperations({
            @RouterOperation(
                    path = "/api/capacidad",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.POST,
                    beanClass = CapacidadHandler.class,
                    beanMethod = "listenGuardarCapacidad",
                    operation = @Operation(
                            operationId = "createCapacidad",
                            summary = "Guardar capacidad",
                            description = "Crea una nueva capacidad en el sistema",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = CapacidadRequestDto.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Capacidad creada exitosamente",
                                            content = @Content(schema = @Schema(implementation = CapacidadResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en los datos enviados"
                                    ),
                                    @ApiResponse(
                                            responseCode = "500",
                                            description = "Error interno del servidor"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(CapacidadHandler handler);
}

