package co.com.capacidad.r2dbc;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Repository
public class CapacidadRepositoryAdapter extends ReactiveAdapterOperations<
        Capacidad,
        CapacidadEntity,
        Long,
        CapacidadReactiveRepository
> {
    public CapacidadRepositoryAdapter(CapacidadReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Capacidad.class));
    }

}
