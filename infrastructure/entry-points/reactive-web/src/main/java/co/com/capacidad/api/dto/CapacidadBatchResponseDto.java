package co.com.capacidad.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO de respuesta para una capacidad consultada por batch con sus tecnologías")
public record CapacidadBatchResponseDto(
        @Schema(description = "Identificador único de la capacidad", example = "1")
        Long id,

        @Schema(description = "Nombre de la capacidad", example = "Desarrollo Backend Java")
        String nombre,

        @Schema(description = "Descripción de la capacidad", example = "Creación de la lógica del servidor, APIs y microservicios con Java")
        String descripcion,

        @Schema(description = "Lista de tecnologías asociadas con id y nombre")
        List<TecnologiaInfoDto> tecnologias
) {
}

