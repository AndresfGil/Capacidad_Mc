package co.com.capacidad.r2dbc;

import co.com.capacidad.r2dbc.entities.CapacidadEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CapacidadReactiveRepository extends ReactiveCrudRepository<CapacidadEntity, Long>, ReactiveQueryByExampleExecutor<CapacidadEntity> {

}
