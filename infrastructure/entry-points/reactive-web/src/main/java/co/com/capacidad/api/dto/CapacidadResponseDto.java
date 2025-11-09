package co.com.capacidad.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "DTO para la respuesta de una Capacidad, incluyendo sus tecnologías asociadas")
public record CapacidadResponseDto(
        @Schema(description = "ID único de la capacidad generado por el sistema", example = "1")
        Long id,

        @Schema(description = "Nombre de la capacidad", example = "Desarrollo Backend Java")
        String nombre,

        @Schema(description = "Descripción de la capacidad", example = "Creación de la lógica del servidor, APIs y microservicios con Java.")
        String descripcion,

        @Schema(description = "Listado de IDs de las tecnologías asociadas a la capacidad", example = "[1, 2, 3]")
        List<Long> tecnologiasIds
) {
}
