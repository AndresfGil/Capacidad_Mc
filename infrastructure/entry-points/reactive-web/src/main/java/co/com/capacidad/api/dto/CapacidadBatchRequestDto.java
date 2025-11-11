package co.com.capacidad.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Schema(description = "DTO para consultar capacidades por lista de IDs")
public record CapacidadBatchRequestDto(
        @Schema(description = "Lista de IDs de capacidades a consultar", example = "[1, 2, 3, 4, 5]", required = true)
        @NotNull(message = "La lista de IDs no puede ser nula")
        @NotEmpty(message = "La lista de IDs no puede estar vacía")
        List<Long> ids
) {
}
