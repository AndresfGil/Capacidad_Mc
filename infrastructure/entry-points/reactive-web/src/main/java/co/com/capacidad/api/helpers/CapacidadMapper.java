package co.com.capacidad.api.helpers;

import co.com.capacidad.api.dto.CapacidadRequestDto;
import co.com.capacidad.api.dto.CapacidadResponseDto;
import co.com.capacidad.model.capacidad.Capacidad;
import org.springframework.stereotype.Component;

@Component
public class CapacidadMapper {

    public Capacidad toDomain(CapacidadRequestDto dto) {
        return Capacidad.builder()
                .nombre(dto.nombre())
                .descripcion(dto.descripcion())
                .tecnologiasIds(dto.tecnologiasIds())
                .build();
    }

    public CapacidadResponseDto toResponseDto(Capacidad capacidad) {
        return new CapacidadResponseDto(
                capacidad.getId(),
                capacidad.getNombre(),
                capacidad.getDescripcion(),
                capacidad.getTecnologiasIds()
        );
    }
}
