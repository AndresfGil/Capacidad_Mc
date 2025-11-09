package co.com.capacidad.consumer;

import co.com.capacidad.consumer.dto.TecnologiaBatchRequestDto;
import co.com.capacidad.consumer.dto.TecnologiaBatchResponseDto;
import co.com.capacidad.model.capacidad.gateways.TecnologiaGateway;
import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TecnologiaRestAdapter implements TecnologiaGateway {

    private final WebClient client;

    @Override
    @CircuitBreaker(name = "obtenerTecnologiasPorIds")
    public Flux<TecnologiaInfo> obtenerTecnologiasPorIds(List<Long> ids) {
        TecnologiaBatchRequestDto request = new TecnologiaBatchRequestDto(ids);

        return client.post()
                .uri("/api/tecnologia/batch")
                .bodyValue(request)
                .retrieve()
                .bodyToFlux(TecnologiaBatchResponseDto.class)
                .map(dto -> TecnologiaInfo.builder()
                        .id(dto.id())
                        .nombre(dto.nombre())
                        .build())
                .onErrorMap(throwable -> new RuntimeException("Error al consultar tecnologías: " + throwable.getMessage(), throwable));
    }
}

