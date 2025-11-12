package co.com.capacidad.model.capacidad.gateways;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.page.CustomPage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

public interface CapacidadRepository {

    Mono<Capacidad> guardarCapacidad(Capacidad capacidad);

    Mono<CustomPage<Capacidad>> listarCapacidades(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    );

    Flux<Capacidad> obtenerCapacidadesPorIds(List<Long> ids);
    
    Mono<Void> activarCapacidades(List<Long> ids);
    
    Mono<Void> desactivarCapacidades(List<Long> ids);
    
    Mono<Void> eliminarInactivasAntiguas(LocalDateTime fechaLimite);

}
