package co.com.capacidad.api.docs;

import co.com.capacidad.api.CapacidadHandler;
import co.com.capacidad.api.dto.CapacidadPageResponseDto;
import co.com.capacidad.api.dto.CapacidadRequestDto;
import co.com.capacidad.api.dto.CapacidadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
            ),
            @RouterOperation(
                    path = "/api/capacidad",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET,
                    beanClass = CapacidadHandler.class,
                    beanMethod = "listenListarCapacidades",
                    operation = @Operation(
                            operationId = "listarCapacidades",
                            summary = "Listar capacidades",
                            description = "Lista las capacidades con paginación y ordenamiento. Permite ordenar por nombre o cantidad de tecnologías.",
                            parameters = {
                                    @Parameter(
                                            name = "page",
                                            description = "Número de página (comienza en 0)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "0", example = "0")
                                    ),
                                    @Parameter(
                                            name = "size",
                                            description = "Tamaño de página (número de elementos por página)",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "integer", defaultValue = "10", example = "10")
                                    ),
                                    @Parameter(
                                            name = "sortBy",
                                            description = "Campo por el cual ordenar: 'nombre' o 'cantidadTecnologias'",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", allowableValues = {"nombre", "cantidadTecnologias"}, defaultValue = "nombre", example = "nombre")
                                    ),
                                    @Parameter(
                                            name = "sortDirection",
                                            description = "Dirección del ordenamiento: 'ASC' o 'DESC'",
                                            in = ParameterIn.QUERY,
                                            schema = @Schema(type = "string", allowableValues = {"ASC", "DESC"}, defaultValue = "ASC", example = "ASC")
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Lista de capacidades obtenida exitosamente",
                                            content = @Content(schema = @Schema(implementation = CapacidadPageResponseDto.class))
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Error de validación en los parámetros"
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

