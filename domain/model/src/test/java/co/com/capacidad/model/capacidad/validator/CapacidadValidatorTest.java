package co.com.capacidad.model.capacidad.validator;

import co.com.capacidad.model.capacidad.exception.TecnologiasDuplicadasException;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class CapacidadValidatorTest {

    @Test
    void validarTecnologiasNoDuplicadas_CuandoListaVacia_DeberiaRetornarMonoVacio() {
        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(Collections.emptyList()))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoListaNula_DeberiaRetornarMonoVacio() {
        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(null))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoNoHayDuplicados_DeberiaRetornarMonoVacio() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 3L);

        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(tecnologiasIds))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoHayDuplicados_DeberiaLanzarTecnologiasDuplicadasException() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 1L);

        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(tecnologiasIds))
                .expectErrorMatches(throwable -> throwable instanceof TecnologiasDuplicadasException
                        && throwable.getMessage().equals("La lista de tecnologías contiene IDs duplicados"))
                .verify();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoTodosSonDuplicados_DeberiaLanzarExcepcion() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 1L, 1L);

        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(tecnologiasIds))
                .expectError(TecnologiasDuplicadasException.class)
                .verify();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoMultiplesDuplicados_DeberiaLanzarExcepcionConTodosLosDuplicados() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 1L, 3L, 2L, 4L);

        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(tecnologiasIds))
                .expectErrorMatches(throwable -> {
                    if (throwable instanceof TecnologiasDuplicadasException) {
                        TecnologiasDuplicadasException ex = (TecnologiasDuplicadasException) throwable;
                        List<Long> duplicados = ex.getErrors().stream()
                                .map(error -> error.replaceAll(".*\\[(.*)\\].*", "$1"))
                                .flatMap(s -> Arrays.stream(s.split(", ")))
                                .map(Long::parseLong)
                                .toList();
                        return duplicados.contains(1L) && duplicados.contains(2L);
                    }
                    return false;
                })
                .verify();
    }

    @Test
    void validarTecnologiasNoDuplicadas_CuandoUnSoloElemento_DeberiaRetornarMonoVacio() {
        List<Long> tecnologiasIds = Collections.singletonList(1L);

        StepVerifier.create(CapacidadValidator.validarTecnologiasNoDuplicadas(tecnologiasIds))
                .verifyComplete();
    }
}

