package co.com.capacidad.usecase.capacidad;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.model.capacidad.page.CustomPage;
import co.com.capacidad.model.capacidad.validator.CapacidadValidator;
import co.com.capacidad.usecase.enrichment.CapacidadEnrichmentService;
import co.com.capacidad.usecase.validator.TecnologiaValidatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacidadUseCase {

    private final CapacidadRepository capacidadRepository;
    private final TecnologiaValidatorService tecnologiaValidatorService;
    private final CapacidadEnrichmentService capacidadEnrichmentService;

    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return CapacidadValidator.validarTecnologiasNoDuplicadas(capacidad.getTecnologiasIds())
                .then(tecnologiaValidatorService.validarTecnologiasExisten(capacidad.getTecnologiasIds()))
                .then(Mono.just(capacidad))
                .flatMap(c -> capacidadRepository.guardarCapacidad(c));
    }

    public Mono<CustomPage<CapacidadConTecnologias>> listarCapacidades(
            Integer page,
            Integer size,
            String sortBy,
            String sortDirection
    ) {
        return capacidadRepository.listarCapacidades(page, size, sortBy, sortDirection)
                .flatMap(capacidadEnrichmentService::enriquecerCapacidadesConTecnologias);
    }
}
