package co.com.capacidad.usecase.enrichment;

import co.com.capacidad.model.capacidad.Capacidad;
import co.com.capacidad.model.capacidad.CapacidadConTecnologias;
import co.com.capacidad.model.capacidad.gateways.TecnologiaGateway;
import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import co.com.capacidad.model.capacidad.page.CustomPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CapacidadEnrichmentServiceTest {

    @Mock
    private TecnologiaGateway tecnologiaGateway;

    @InjectMocks
    private CapacidadEnrichmentService capacidadEnrichmentService;

    private Capacidad capacidad1;
    private Capacidad capacidad2;
    private TecnologiaInfo tecnologia1;
    private TecnologiaInfo tecnologia2;
    private TecnologiaInfo tecnologia3;

    @BeforeEach
    void setUp() {
        capacidad1 = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend Java")
                .descripcion("Creación de la lógica del servidor con Java")
                .tecnologiasIds(Arrays.asList(1L, 2L))
                .build();

        capacidad2 = Capacidad.builder()
                .id(2L)
                .nombre("Desarrollo Frontend React")
                .descripcion("Creación de interfaces con React")
                .tecnologiasIds(Arrays.asList(2L, 3L))
                .build();

        tecnologia1 = TecnologiaInfo.builder()
                .id(1L)
                .nombre("Java")
                .build();

        tecnologia2 = TecnologiaInfo.builder()
                .id(2L)
                .nombre("Spring Boot")
                .build();

        tecnologia3 = TecnologiaInfo.builder()
                .id(3L)
                .nombre("React")
                .build();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoPaginaVacia_DeberiaRetornarPaginaVacia() {
        CustomPage<Capacidad> pageVacia = CustomPage.<Capacidad>builder()
                .data(Collections.emptyList())
                .totalRows(0L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageVacia))
                .assertNext(page -> {
                    assertThat(page.getData()).isEmpty();
                    assertThat(page.getTotalRows()).isEqualTo(0L);
                    assertThat(page.getPageSize()).isEqualTo(10);
                    assertThat(page.getPageNum()).isEqualTo(0);
                    assertThat(page.getHasNext()).isFalse();
                    assertThat(page.getSort()).isEqualTo("nombre ASC");
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoUnaCapacidad_DeberiaEnriquecerCorrectamente() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(1);
                    assertThat(page.getTotalRows()).isEqualTo(1L);
                    assertThat(page.getPageSize()).isEqualTo(10);
                    assertThat(page.getPageNum()).isEqualTo(0);
                    assertThat(page.getHasNext()).isFalse();
                    assertThat(page.getSort()).isEqualTo("nombre ASC");

                    CapacidadConTecnologias capacidadEnriquecida = page.getData().get(0);
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(1L);
                    assertThat(capacidadEnriquecida.getNombre()).isEqualTo("Desarrollo Backend Java");
                    assertThat(capacidadEnriquecida.getDescripcion()).isEqualTo("Creación de la lógica del servidor con Java");
                    assertThat(capacidadEnriquecida.getTecnologias()).hasSize(2);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getId()).isEqualTo(1L);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getNombre()).isEqualTo("Java");
                    assertThat(capacidadEnriquecida.getTecnologias().get(1).getId()).isEqualTo(2L);
                    assertThat(capacidadEnriquecida.getTecnologias().get(1).getNombre()).isEqualTo("Spring Boot");
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoMultiplesCapacidades_DeberiaEnriquecerTodas() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1, capacidad2))
                .totalRows(2L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(2);
                    assertThat(page.getTotalRows()).isEqualTo(2L);

                    CapacidadConTecnologias capacidad1Enriquecida = page.getData().get(0);
                    assertThat(capacidad1Enriquecida.getId()).isEqualTo(1L);
                    assertThat(capacidad1Enriquecida.getTecnologias()).hasSize(2);
                    assertThat(capacidad1Enriquecida.getTecnologias().get(0).getNombre()).isEqualTo("Java");
                    assertThat(capacidad1Enriquecida.getTecnologias().get(1).getNombre()).isEqualTo("Spring Boot");

                    CapacidadConTecnologias capacidad2Enriquecida = page.getData().get(1);
                    assertThat(capacidad2Enriquecida.getId()).isEqualTo(2L);
                    assertThat(capacidad2Enriquecida.getTecnologias()).hasSize(2);
                    assertThat(capacidad2Enriquecida.getTecnologias().get(0).getNombre()).isEqualTo("Spring Boot");
                    assertThat(capacidad2Enriquecida.getTecnologias().get(1).getNombre()).isEqualTo("React");
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoCapacidadSinTecnologias_DeberiaRetornarListaVacia() {
        Capacidad capacidadSinTecnologias = Capacidad.builder()
                .id(3L)
                .nombre("Capacidad sin tecnologías")
                .descripcion("Descripción")
                .tecnologiasIds(Collections.emptyList())
                .build();

        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidadSinTecnologias))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(1);
                    CapacidadConTecnologias capacidadEnriquecida = page.getData().get(0);
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(3L);
                    assertThat(capacidadEnriquecida.getTecnologias()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoTecnologiasIdsEsNull_DeberiaRetornarListaVacia() {
        Capacidad capacidadConTecnologiasNull = Capacidad.builder()
                .id(4L)
                .nombre("Capacidad con tecnologías null")
                .descripcion("Descripción")
                .tecnologiasIds(null)
                .build();

        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidadConTecnologiasNull))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(1);
                    CapacidadConTecnologias capacidadEnriquecida = page.getData().get(0);
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(4L);
                    assertThat(capacidadEnriquecida.getTecnologias()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoAlgunaTecnologiaNoExiste_DeberiaFiltrarNulls() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        // Solo retorna tecnología1, pero capacidad1 tiene tecnologías 1L y 2L
        // La tecnología 2L no existe, debería filtrarse
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(1);
                    CapacidadConTecnologias capacidadEnriquecida = page.getData().get(0);
                    assertThat(capacidadEnriquecida.getTecnologias()).hasSize(1);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getId()).isEqualTo(1L);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getNombre()).isEqualTo("Java");
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoGatewayFalla_DeberiaPropagarError() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1))
                .totalRows(1L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        RuntimeException error = new RuntimeException("Error de conexión");

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.error(error));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoCapacidadesCompartenTecnologias_DeberiaExtraerIdsUnicos() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1, capacidad2))
                .totalRows(2L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(false)
                .sort("nombre ASC")
                .build();

        // capacidad1 tiene [1L, 2L], capacidad2 tiene [2L, 3L]
        // IDs únicos deberían ser [1L, 2L, 3L]
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getData()).hasSize(2);
                    // Verificar que ambas capacidades tienen sus tecnologías correctas
                    assertThat(page.getData().get(0).getTecnologias()).hasSize(2);
                    assertThat(page.getData().get(1).getTecnologias()).hasSize(2);
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoPaginaConHasNext_DeberiaPreservarMetadata() {
        CustomPage<Capacidad> pageCapacidades = CustomPage.<Capacidad>builder()
                .data(Arrays.asList(capacidad1))
                .totalRows(15L)
                .pageSize(10)
                .pageNum(0)
                .hasNext(true)
                .sort("cantidadTecnologias DESC")
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(pageCapacidades))
                .assertNext(page -> {
                    assertThat(page.getTotalRows()).isEqualTo(15L);
                    assertThat(page.getPageSize()).isEqualTo(10);
                    assertThat(page.getPageNum()).isEqualTo(0);
                    assertThat(page.getHasNext()).isTrue();
                    assertThat(page.getSort()).isEqualTo("cantidadTecnologias DESC");
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxVacio_DeberiaRetornarFluxVacio() {
        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.empty()))
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConUnaCapacidad_DeberiaEnriquecerCorrectamente() {
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidad1)))
                .assertNext(capacidadEnriquecida -> {
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(1L);
                    assertThat(capacidadEnriquecida.getNombre()).isEqualTo("Desarrollo Backend Java");
                    assertThat(capacidadEnriquecida.getTecnologias()).hasSize(2);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getId()).isEqualTo(1L);
                    assertThat(capacidadEnriquecida.getTecnologias().get(1).getId()).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConMultiplesCapacidades_DeberiaEnriquecerTodas() {
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidad1, capacidad2)))
                .assertNext(capacidad1Enriquecida -> {
                    assertThat(capacidad1Enriquecida.getId()).isEqualTo(1L);
                    assertThat(capacidad1Enriquecida.getTecnologias()).hasSize(2);
                })
                .assertNext(capacidad2Enriquecida -> {
                    assertThat(capacidad2Enriquecida.getId()).isEqualTo(2L);
                    assertThat(capacidad2Enriquecida.getTecnologias()).hasSize(2);
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConCapacidadSinTecnologias_DeberiaRetornarListaVacia() {
        Capacidad capacidadSinTecnologias = Capacidad.builder()
                .id(3L)
                .nombre("Capacidad sin tecnologías")
                .descripcion("Descripción")
                .tecnologiasIds(Collections.emptyList())
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidadSinTecnologias)))
                .assertNext(capacidadEnriquecida -> {
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(3L);
                    assertThat(capacidadEnriquecida.getTecnologias()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConCapacidadConTecnologiasNull_DeberiaRetornarListaVacia() {
        Capacidad capacidadConTecnologiasNull = Capacidad.builder()
                .id(4L)
                .nombre("Capacidad con tecnologías null")
                .descripcion("Descripción")
                .tecnologiasIds(null)
                .build();

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.empty());

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidadConTecnologiasNull)))
                .assertNext(capacidadEnriquecida -> {
                    assertThat(capacidadEnriquecida.getId()).isEqualTo(4L);
                    assertThat(capacidadEnriquecida.getTecnologias()).isEmpty();
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxGatewayFalla_DeberiaPropagarError() {
        RuntimeException error = new RuntimeException("Error de conexión");

        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.error(error));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidad1)))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConCapacidadesCompartenTecnologias_DeberiaExtraerIdsUnicos() {
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1, tecnologia2, tecnologia3));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidad1, capacidad2)))
                .assertNext(capacidad1Enriquecida -> {
                    assertThat(capacidad1Enriquecida.getTecnologias()).hasSize(2);
                })
                .assertNext(capacidad2Enriquecida -> {
                    assertThat(capacidad2Enriquecida.getTecnologias()).hasSize(2);
                })
                .verifyComplete();
    }

    @Test
    void enriquecerCapacidadesConTecnologias_CuandoFluxConAlgunaTecnologiaNoExiste_DeberiaFiltrarNulls() {
        when(tecnologiaGateway.obtenerTecnologiasPorIds(anyList()))
                .thenReturn(Flux.just(tecnologia1));

        StepVerifier.create(capacidadEnrichmentService.enriquecerCapacidadesConTecnologias(Flux.just(capacidad1)))
                .assertNext(capacidadEnriquecida -> {
                    assertThat(capacidadEnriquecida.getTecnologias()).hasSize(1);
                    assertThat(capacidadEnriquecida.getTecnologias().get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }
}

