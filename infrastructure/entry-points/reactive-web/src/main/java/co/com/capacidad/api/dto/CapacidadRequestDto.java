package co.com.capacidad.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Schema(description = "DTO para crear una nueva capacidad")
public record CapacidadRequestDto(

    @Schema(description = "Nombre de la capacidad", example = "Desarrollo Backend Java", required = true, maxLength = 50)
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    String nombre,

    @Schema(description = "Descripción de la capacidad", example = "Creación de la lógica del servidor, APIs y microservicios con Java.", required = true, maxLength = 90)
    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 90, message = "La descripción no puede exceder 90 caracteres")
    String descripcion,

    @Schema(description = "Lista de IDs de las tecnologías asociadas.",
            example = "[1,2,3,4]", required = true)
    @NotNull(message = "La lista de tecnologías no puede ser nula")
    @Size(min = 3, message = "Una capacidad debe tener al menos 3 tecnologías")
    @Size(max = 20, message = "Una capacidad no puede tener más de 20 tecnologías")
    List<Long> tecnologiasIds

) {

}
