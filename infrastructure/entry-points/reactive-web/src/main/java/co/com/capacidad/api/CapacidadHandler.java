package co.com.capacidad.api;

import co.com.capacidad.api.dto.CapacidadListRequestDto;
import co.com.capacidad.api.dto.CapacidadRequestDto;
import co.com.capacidad.api.helpers.CapacidadMapper;
import co.com.capacidad.api.helpers.DtoValidator;
import co.com.capacidad.usecase.capacidad.CapacidadUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CapacidadHandler {

    private final CapacidadUseCase capacidadUseCase;
    private final DtoValidator dtoValidator;
    private final CapacidadMapper capacidadMapper;

    public Mono<ServerResponse> listenGuardarCapacidad(ServerRequest req) {
        return req.bodyToMono(CapacidadRequestDto.class)
                .flatMap(dto -> dtoValidator.validate(dto)
                        .map(capacidadMapper::toDomain)
                        .flatMap(capacidadUseCase::guardarCapacidad)
                        .map(capacidadMapper::toResponseDto)
                        .flatMap(capacidadResponseDto -> ServerResponse
                                .status(HttpStatus.CREATED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(capacidadResponseDto)));
    }

    public Mono<ServerResponse> listenListarCapacidades(ServerRequest req) {
        return Mono.fromCallable(() -> {
                    String pageParam = req.queryParam("page").orElse("0");
                    String sizeParam = req.queryParam("size").orElse("10");
                    String sortByParam = req.queryParam("sortBy").orElse("nombre");
                    String sortDirectionParam = req.queryParam("sortDirection").orElse("ASC");

                    return new CapacidadListRequestDto(
                            Integer.parseInt(pageParam),
                            Integer.parseInt(sizeParam),
                            sortByParam,
                            sortDirectionParam
                    );
                })
                .flatMap(dto -> dtoValidator.validate(dto)
                        .flatMap(dtoValidado -> capacidadUseCase.listarCapacidades(
                                dtoValidado.page(),
                                dtoValidado.size(),
                                dtoValidado.sortBy(),
                                dtoValidado.sortDirection()
                        ))
                        .map(capacidadMapper::toPageResponseDto)
                        .flatMap(response -> ServerResponse
                                .status(HttpStatus.OK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(response)));
    }
}
