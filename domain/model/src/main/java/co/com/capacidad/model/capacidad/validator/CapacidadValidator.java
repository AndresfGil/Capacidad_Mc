package co.com.capacidad.model.capacidad.validator;

import co.com.capacidad.model.capacidad.exception.TecnologiasDuplicadasException;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CapacidadValidator {

    public static Mono<Void> validarTecnologiasNoDuplicadas(List<Long> tecnologiasIds) {
        if (tecnologiasIds == null || tecnologiasIds.isEmpty()) {
            return Mono.empty();
        }

        Set<Long> tecnologiasUnicas = new HashSet<>();
        List<Long> tecnologiasDuplicadas = tecnologiasIds.stream()
                .filter(id -> !tecnologiasUnicas.add(id))
                .distinct()
                .toList();

        if (!tecnologiasDuplicadas.isEmpty()) {
            return Mono.error(new TecnologiasDuplicadasException(tecnologiasDuplicadas));
        }

        return Mono.empty();
    }
}

