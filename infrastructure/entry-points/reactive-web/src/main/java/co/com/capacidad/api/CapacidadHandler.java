package co.com.capacidad.api;

import co.com.capacidad.usecase.capacidad.CapacidadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapacidadHandler {
private final CapacidadUseCase capacidadUseCase;
}
