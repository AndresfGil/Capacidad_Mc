package co.com.capacidad.usecase.validator;

import co.com.capacidad.model.capacidad.exception.TecnologiaNoEncontradaException;
import co.com.capacidad.model.capacidad.gateways.TecnologiaGateway;
import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TecnologiaValidatorServiceTest {

    @Mock
    private TecnologiaGateway tecnologiaGateway;

    @InjectMocks
    private TecnologiaValidatorService tecnologiaValidatorService;

    @Test
    void validarTecnologiasExisten_CuandoListaVacia_DeberiaRetornarMonoVacio() {
        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(Collections.emptyList()))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasExisten_CuandoListaNula_DeberiaRetornarMonoVacio() {
        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(null))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasExisten_CuandoTodasExisten_DeberiaRetornarMonoVacio() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L);
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        TecnologiaInfo tecnologia2 = TecnologiaInfo.builder().id(2L).nombre("Spring").build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.just(tecnologia1, tecnologia2));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasExisten_CuandoNingunaExiste_DeberiaLanzarExcepcion() {
        List<Long> tecnologiasIds = Arrays.asList(999L, 1000L);

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .expectError(TecnologiaNoEncontradaException.class)
                .verify();
    }

    @Test
    void validarTecnologiasExisten_CuandoGatewayRetornaError_DeberiaPropagarError() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L);
        RuntimeException error = new RuntimeException("Error de conexión");

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.error(error));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void validarTecnologiasExisten_CuandoUnSoloIdYExiste_DeberiaRetornarMonoVacio() {
        List<Long> tecnologiasIds = Collections.singletonList(1L);
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.just(tecnologia1));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasExisten_CuandoMultiplesIdsConDuplicadosEnSolicitud_DeberiaValidarCorrectamente() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 3L);
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        TecnologiaInfo tecnologia2 = TecnologiaInfo.builder().id(2L).nombre("Spring").build();
        TecnologiaInfo tecnologia3 = TecnologiaInfo.builder().id(3L).nombre("Hibernate").build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .verifyComplete();
    }

    @Test
    void validarTecnologiasExisten_CuandoAlgunasExistenYAlgunasNo_DeberiaLanzarExcepcion() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 3L);
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        TecnologiaInfo tecnologia2 = TecnologiaInfo.builder().id(2L).nombre("Spring").build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.just(tecnologia1, tecnologia2));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .expectError(TecnologiaNoEncontradaException.class)
                .verify();
    }

    @Test
    void validarTecnologiasExisten_CuandoGatewayRetornaMasTecnologiasDeLasSolicitadas_DeberiaValidarCorrectamente() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L);
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        TecnologiaInfo tecnologia2 = TecnologiaInfo.builder().id(2L).nombre("Spring").build();
        TecnologiaInfo tecnologia3 = TecnologiaInfo.builder().id(3L).nombre("Hibernate").build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList())).thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(tecnologiaValidatorService.validarTecnologiasExisten(tecnologiasIds))
                .verifyComplete();
    }
}


