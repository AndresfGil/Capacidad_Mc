package co.com.capacidad.usecase.enrichment;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.gateways.TecnologiaGateway;
import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import co.com.capacidad.model.capacidad.page.CustomPage;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CapacidadEnrichmentService {

    private final TecnologiaGateway tecnologiaGateway;

    public Mono<CustomPage<CapacidadConTecnologias>> enriquecerCapacidadesConTecnologias(
            CustomPage<Capacidad> pageCapacidades
    ) {
        if (pageCapacidades.getData().isEmpty()) {
            return Mono.just(buildEmptyPage(pageCapacidades));
        }

        List<Long> allTecnologiasIds = extractAllTecnologiasIds(pageCapacidades.getData());

        return tecnologiaGateway.obtenerTecnologiasPorIds(allTecnologiasIds)
                .collectMap(TecnologiaInfo::getId, tecnologia -> tecnologia)
                .map(tecnologiasMap -> buildEnrichedPage(pageCapacidades, tecnologiasMap));
    }

    private List<Long> extractAllTecnologiasIds(List<Capacidad> capacidades) {
        return capacidades.stream()
                .flatMap(capacidad -> capacidad.getTecnologiasIds() != null
                        ? capacidad.getTecnologiasIds().stream()
                        : java.util.stream.Stream.empty())
                .distinct()
                .toList();
    }

    private CustomPage<CapacidadConTecnologias> buildEmptyPage(CustomPage<Capacidad> pageCapacidades) {
        return CustomPage.<CapacidadConTecnologias>builder()
                .data(new ArrayList<>())
                .totalRows(pageCapacidades.getTotalRows())
                .pageSize(pageCapacidades.getPageSize())
                .pageNum(pageCapacidades.getPageNum())
                .hasNext(pageCapacidades.getHasNext())
                .sort(pageCapacidades.getSort())
                .build();
    }

    private CustomPage<CapacidadConTecnologias> buildEnrichedPage(
            CustomPage<Capacidad> pageCapacidades,
            java.util.Map<Long, TecnologiaInfo> tecnologiasMap
    ) {
        List<CapacidadConTecnologias> capacidadesConTecnologias = pageCapacidades.getData().stream()
                .map(capacidad -> buildCapacidadConTecnologias(capacidad, tecnologiasMap))
                .toList();

        return CustomPage.<CapacidadConTecnologias>builder()
                .data(capacidadesConTecnologias)
                .totalRows(pageCapacidades.getTotalRows())
                .pageSize(pageCapacidades.getPageSize())
                .pageNum(pageCapacidades.getPageNum())
                .hasNext(pageCapacidades.getHasNext())
                .sort(pageCapacidades.getSort())
                .build();
    }

    private CapacidadConTecnologias buildCapacidadConTecnologias(
            Capacidad capacidad,
            java.util.Map<Long, TecnologiaInfo> tecnologiasMap
    ) {
        List<TecnologiaInfo> tecnologias = capacidad.getTecnologiasIds() != null
                ? capacidad.getTecnologiasIds().stream()
                        .map(tecnologiasMap::get)
                        .filter(Objects::nonNull)
                        .toList()
                : new ArrayList<>();

        return CapacidadConTecnologias.builder()
                .id(capacidad.getId())
                .nombre(capacidad.getNombre())
                .descripcion(capacidad.getDescripcion())
                .tecnologias(tecnologias)
                .build();
    }

    public Flux<CapacidadConTecnologias> enriquecerCapacidadesConTecnologias(Flux<Capacidad> capacidadesFlux) {
        return capacidadesFlux
                .collectList()
                .flatMapMany(capacidades -> {
                    if (capacidades.isEmpty()) {
                        return Flux.empty();
                    }
                    List<Long> allTecnologiasIds = extractAllTecnologiasIds(capacidades);
                    return tecnologiaGateway.obtenerTecnologiasPorIds(allTecnologiasIds)
                            .collectMap(TecnologiaInfo::getId, tecnologia -> tecnologia)
                            .flatMapMany(tecnologiasMap -> Flux.fromIterable(capacidades)
                                    .map(capacidad -> buildCapacidadConTecnologias(capacidad, tecnologiasMap)));
                });
    }
}

