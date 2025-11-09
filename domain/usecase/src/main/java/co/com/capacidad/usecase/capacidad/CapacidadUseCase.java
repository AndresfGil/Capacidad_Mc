package co.com.capacidad.usecase.capacidad;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.model.capacidad.validator.CapacidadValidator;
import co.com.capacidad.usecase.validator.TecnologiaValidatorService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CapacidadUseCase {

    private final CapacidadRepository capacidadRepository;
    private final TecnologiaValidatorService tecnologiaValidatorService;

    public Mono<Capacidad> guardarCapacidad(Capacidad capacidad) {
        return CapacidadValidator.validarTecnologiasNoDuplicadas(capacidad.getTecnologiasIds())
                .then(tecnologiaValidatorService.validarTecnologiasExisten(capacidad.getTecnologiasIds()))
                .then(capacidadRepository.guardarCapacidad(capacidad));
    }
}
