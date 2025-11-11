package co.com.capacidad.r2dbc;

import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface CapacidadReactiveRepository extends ReactiveCrudRepository<CapacidadEntity, Long>, ReactiveQueryByExampleExecutor<CapacidadEntity> {

    Flux<CapacidadEntity> findByIdIn(Collection<Long> ids);

}
