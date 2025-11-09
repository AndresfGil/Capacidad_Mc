package co.com.capacidad.model.capacidad.gateways;

import co.com.capacidad.model.capacidad.Capacidad;
import reactor.core.publisher.Mono;

public interface CapacidadRepository {

    Mono<Capacidad> guardarCapacidad(Capacidad capacidad);

}
