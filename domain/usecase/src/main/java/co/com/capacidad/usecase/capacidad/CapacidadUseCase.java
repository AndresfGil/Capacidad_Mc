package co.com.capacidad.usecase.capacidad;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.model.capacidad.page.CustomPage;
import co.com.capacidad.model.capacidad.validator.CapacidadValidator;
import co.com.capacidad.usecase.enrichment.CapacidadEnrichmentService;
import co.com.capacidad.usecase.validator.TecnologiaValidatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
                .flatMap(pageCapacidades -> {
                    var capacidadesActivas = pageCapacidades.getData().stream()
                            .filter(capacidad -> Boolean.TRUE.equals(capacidad.getActiva()))
                            .toList();
                    
                    var pageFiltrada = CustomPage.<Capacidad>builder()
                            .data(capacidadesActivas)
                            .totalRows(pageCapacidades.getTotalRows())
                            .pageSize(pageCapacidades.getPageSize())
                            .pageNum(pageCapacidades.getPageNum())
                            .hasNext(pageCapacidades.getHasNext())
                            .sort(pageCapacidades.getSort())
                            .build();
                    
                    return Mono.just(pageFiltrada);
                })
                .flatMap(capacidadEnrichmentService::enriquecerCapacidadesConTecnologias);
    }

    public Flux<CapacidadConTecnologias> obtenerCapacidadesPorIds(List<Long> ids) {
        return capacidadRepository.obtenerCapacidadesPorIds(ids)
                .filter(capacidad -> Boolean.TRUE.equals(capacidad.getActiva()))
                .transform(capacidadEnrichmentService::enriquecerCapacidadesConTecnologias);
    }

    public Mono<Void> activarCapacidades(List<Long> ids) {
        return capacidadRepository.activarCapacidades(ids);
    }

    public Mono<Void> desactivarCapacidades(List<Long> ids) {
        return capacidadRepository.desactivarCapacidades(ids);
    }
}
