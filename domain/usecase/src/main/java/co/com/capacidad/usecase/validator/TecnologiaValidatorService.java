package co.com.capacidad.usecase.validator;

import co.com.capacidad.model.capacidad.exception.TecnologiaNoEncontradaException;
import co.com.capacidad.model.capacidad.gateways.TecnologiaGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class TecnologiaValidatorService {

    private final TecnologiaGateway tecnologiaGateway;

    public Mono<Void> validarTecnologiasExisten(List<Long> tecnologiasIds) {
        if (tecnologiasIds == null || tecnologiasIds.isEmpty()) {
            return Mono.empty();
        }

        Set<Long> idsSolicitados = new HashSet<>(tecnologiasIds);

        return tecnologiaGateway.obtenerTecnologiasPorIds(tecnologiasIds)
                .map(tecnologia -> tecnologia.getId())
                .collectList()
                .flatMap(idsEncontrados -> {
                    Set<Long> idsEncontradosSet = new HashSet<>(idsEncontrados);
                    List<Long> idsNoEncontrados = idsSolicitados.stream()
                            .filter(id -> !idsEncontradosSet.contains(id))
                            .toList();

                    if (!idsNoEncontrados.isEmpty()) {
                        return Mono.error(new TecnologiaNoEncontradaException(idsNoEncontrados));
                    }

                    return Mono.empty();
                });
    }
}

