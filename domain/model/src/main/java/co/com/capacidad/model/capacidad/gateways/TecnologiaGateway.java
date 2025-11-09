package co.com.capacidad.model.capacidad.gateways;

import reactor.core.publisher.Flux;

import java.util.List;

public interface TecnologiaGateway {
    Flux<TecnologiaInfo> obtenerTecnologiasPorIds(List<Long> ids);
}

