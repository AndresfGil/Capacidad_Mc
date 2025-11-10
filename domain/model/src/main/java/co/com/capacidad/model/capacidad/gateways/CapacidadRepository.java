package co.com.capacidad.model.capacidad.gateways;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.page.CustomPage;
import reactor.core.publisher.Mono;

public interface CapacidadRepository {

    Mono<Capacidad> guardarCapacidad(Capacidad capacidad);

    Mono<CustomPage<Capacidad>> listarCapacidades(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    );

}
