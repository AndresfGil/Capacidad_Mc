package co.com.capacidad.api.helpers;

import co.com.capacidad.api.dto.CapacidadListResponseDto;
import co.com.capacidad.api.dto.CapacidadPageResponseDto;
import co.com.capacidad.api.dto.CapacidadRequestDto;
import co.com.capacidad.api.dto.CapacidadResponseDto;
import co.com.capacidad.api.dto.TecnologiaInfoDto;
import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.page.CustomPage;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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

    public CapacidadPageResponseDto toPageResponseDto(CustomPage<CapacidadConTecnologias> page) {
        return new CapacidadPageResponseDto(
                page.getData().stream()
                        .map(this::toListResponseDto)
                        .collect(Collectors.toList()),
                page.getTotalRows(),
                page.getPageSize(),
                page.getPageNum(),
                page.getHasNext(),
                page.getSort()
        );
    }

    private CapacidadListResponseDto toListResponseDto(CapacidadConTecnologias capacidad) {
        return new CapacidadListResponseDto(
                capacidad.getId(),
                capacidad.getNombre(),
                capacidad.getDescripcion(),
                capacidad.getTecnologias().stream()
                        .map(tecnologia -> new TecnologiaInfoDto(tecnologia.getId(), tecnologia.getNombre()))
                        .collect(Collectors.toList())
        );
    }
}
