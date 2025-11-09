package co.com.capacidad.usecase.capacidad;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.exception.TecnologiaNoEncontradaException;
import co.com.capacidad.model.capacidad.exception.TecnologiasDuplicadasException;
import co.com.capacidad.model.capacidad.gateways.CapacidadRepository;
import co.com.capacidad.usecase.validator.TecnologiaValidatorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacidadUseCaseTest {

    @Mock
    private CapacidadRepository capacidadRepository;

    @Mock
    private TecnologiaValidatorService tecnologiaValidatorService;

    @InjectMocks
    private CapacidadUseCase capacidadUseCase;

    private Capacidad capacidad;
    private List<Long> tecnologiasIds;

    @BeforeEach
    void setUp() {
        tecnologiasIds = Arrays.asList(1L, 2L);
        capacidad = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologiasIds(tecnologiasIds)
                .build();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiasValidas_DeberiaGuardarExitosamente() {
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiasDuplicadas_DeberiaLanzarTecnologiasDuplicadasException() {
        List<Long> idsDuplicados = Arrays.asList(1L, 1L, 2L);
        Capacidad capacidadConDuplicados = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologiasIds(idsDuplicados)
                .build();

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidadConDuplicados))
                .expectErrorMatches(throwable -> throwable instanceof TecnologiasDuplicadasException
                        && throwable.getMessage().equals("La lista de tecnologías contiene IDs duplicados"))
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoMultiplesTecnologiasDuplicadas_DeberiaLanzarExcepcion() {
        List<Long> idsDuplicados = Arrays.asList(1L, 2L, 1L, 3L, 2L);
        Capacidad capacidadConDuplicados = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologiasIds(idsDuplicados)
                .build();

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidadConDuplicados))
                .expectError(TecnologiasDuplicadasException.class)
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoTodasLasTecnologiasSonDuplicadas_DeberiaLanzarExcepcion() {
        List<Long> idsDuplicados = Arrays.asList(1L, 1L, 1L);
        Capacidad capacidadConDuplicados = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologiasIds(idsDuplicados)
                .build();

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidadConDuplicados))
                .expectError(TecnologiasDuplicadasException.class)
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiaNoExiste_DeberiaLanzarTecnologiaNoEncontradaException() {
        TecnologiaNoEncontradaException exception = new TecnologiaNoEncontradaException(Arrays.asList(2L));
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.error(exception));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectErrorMatches(throwable -> throwable instanceof TecnologiaNoEncontradaException
                        && throwable.getMessage().equals("Una o más tecnologías no fueron encontradas"))
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoMultiplesTecnologiasNoExisten_DeberiaLanzarExcepcion() {
        TecnologiaNoEncontradaException exception = new TecnologiaNoEncontradaException(Arrays.asList(2L, 3L));
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.error(exception));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectError(TecnologiaNoEncontradaException.class)
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiasIdsEsNull_DeberiaGuardarExitosamente() {
        capacidad.setTecnologiasIds(null);
        when(tecnologiaValidatorService.validarTecnologiasExisten(null)).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiasIdsEstaVacia_DeberiaGuardarExitosamente() {
        capacidad.setTecnologiasIds(Collections.emptyList());
        when(tecnologiaValidatorService.validarTecnologiasExisten(Collections.emptyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoUnaSolaTecnologia_DeberiaGuardarExitosamente() {
        List<Long> idsUnicos = Collections.singletonList(1L);
        capacidad.setTecnologiasIds(idsUnicos);
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoMultiplesTecnologiasValidas_DeberiaGuardarExitosamente() {
        List<Long> idsMultiples = Arrays.asList(1L, 2L, 3L, 4L);
        capacidad.setTecnologiasIds(idsMultiples);
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoTecnologiaValidatorServiceRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de conexión");
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.error(error));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoRepositorioRetornaError_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error al guardar");
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.error(error));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void guardarCapacidad_CuandoRepositorioRetornaNull_DeberiaManejarNull() {
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        Capacidad capacidadNull = null;
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidadNull));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext((Capacidad) null)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoCapacidadSinId_DeberiaGuardarExitosamente() {
        capacidad.setId(null);
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }

    @Test
    void guardarCapacidad_CuandoCapacidadSinNombre_DeberiaGuardarExitosamente() {
        capacidad.setNombre(null);
        when(tecnologiaValidatorService.validarTecnologiasExisten(anyList())).thenReturn(Mono.empty());
        when(capacidadRepository.guardarCapacidad(any(Capacidad.class))).thenReturn(Mono.just(capacidad));

        StepVerifier.create(capacidadUseCase.guardarCapacidad(capacidad))
                .expectNext(capacidad)
                .verifyComplete();
    }
}
