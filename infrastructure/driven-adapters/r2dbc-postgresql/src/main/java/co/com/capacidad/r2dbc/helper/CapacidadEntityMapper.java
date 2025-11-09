package co.com.capacidad.r2dbc.helper;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CapacidadEntityMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper jsonMapper;

    public CapacidadEntityMapper(com.fasterxml.jackson.databind.ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public CapacidadEntity toEntity(Capacidad capacidad) {
        if (capacidad == null) {
            return null;
        }

        CapacidadEntity entity = new CapacidadEntity();
        entity.setId(capacidad.getId());
        entity.setNombre(capacidad.getNombre());
        entity.setDescripcion(capacidad.getDescripcion());


        try {
            String tecnologiasJson = jsonMapper.writeValueAsString(
                    capacidad.getTecnologiasIds() != null ? capacidad.getTecnologiasIds() : Collections.emptyList()
            );
            entity.setTecnologiasIds(tecnologiasJson);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir tecnologiasIds a JSON", e);
        }

        return entity;
    }

    public Capacidad toDomain(CapacidadEntity entity) {
        if (entity == null) {
            return null;
        }

        Capacidad capacidad = new Capacidad();
        capacidad.setId(entity.getId());
        capacidad.setNombre(entity.getNombre());
        capacidad.setDescripcion(entity.getDescripcion());


        try {
            List<Long> tecnologiasIds = entity.getTecnologiasIds() != null && !entity.getTecnologiasIds().isEmpty()
                    ? jsonMapper.readValue(entity.getTecnologiasIds(), new TypeReference<List<Long>>() {})
                    : Collections.emptyList();
            capacidad.setTecnologiasIds(tecnologiasIds);
        } catch (Exception e) {
            throw new RuntimeException("Error al convertir JSON a tecnologiasIds", e);
        }

        return capacidad;
    }
}

