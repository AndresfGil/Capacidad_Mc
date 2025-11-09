package co.com.capacidad.r2dbc;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import co.com.capacidad.r2dbc.helper.CapacidadEntityMapper;
import co.com.capacidad.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CapacidadRepositoryAdapter extends ReactiveAdapterOperations<
        Capacidad,
        CapacidadEntity,
        Long,
        CapacidadReactiveRepository
> implements CapacidadRepository {
    
    private final CapacidadEntityMapper entityMapper;
    
    public CapacidadRepositoryAdapter(CapacidadReactiveRepository repository, 
                                     ObjectMapper reactiveMapper,
                                     CapacidadEntityMapper entityMapper) {
        super(repository, reactiveMapper, entityMapper::toDomain);
        this.entityMapper = entityMapper;
    }

    @Override
    protected CapacidadEntity toData(Capacidad entity) {
        return entityMapper.toEntity(entity);
    }

    @Override
    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return super.save(capacidad);
    }
}
