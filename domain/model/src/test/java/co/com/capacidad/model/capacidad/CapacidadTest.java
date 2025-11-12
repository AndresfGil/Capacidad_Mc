package co.com.capacidad.model.capacidad;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacidadTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L);
        Capacidad capacidad = Capacidad.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologiasIds(tecnologiasIds)
                .activa(true)
                .build();

        assertEquals(1L, capacidad.getId());
        assertEquals("Desarrollo Backend", capacidad.getNombre());
        assertEquals("Capacidad en desarrollo backend", capacidad.getDescripcion());
        assertEquals(tecnologiasIds, capacidad.getTecnologiasIds());
        assertTrue(capacidad.getActiva());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        Capacidad capacidad = Capacidad.builder().build();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getDescripcion());
        assertNull(capacidad.getTecnologiasIds());
        assertNull(capacidad.getActiva());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        Capacidad capacidad = new Capacidad();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getDescripcion());
        assertNull(capacidad.getTecnologiasIds());
        assertNull(capacidad.getActiva());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L, 3L);
        Capacidad capacidad = new Capacidad(1L, "Frontend", "Capacidad en frontend", tecnologiasIds, true);

        assertEquals(1L, capacidad.getId());
        assertEquals("Frontend", capacidad.getNombre());
        assertEquals("Capacidad en frontend", capacidad.getDescripcion());
        assertEquals(tecnologiasIds, capacidad.getTecnologiasIds());
        assertTrue(capacidad.getActiva());
    }

    @Test
    void toBuilder_DeberiaCrearNuevaInstanciaConValoresModificados() {
        Capacidad original = Capacidad.builder()
                .id(1L)
                .nombre("Backend")
                .descripcion("Original")
                .tecnologiasIds(Arrays.asList(1L, 2L))
                .build();

        Capacidad modificada = original.toBuilder()
                .descripcion("Modificada")
                .build();

        assertEquals(1L, modificada.getId());
        assertEquals("Backend", modificada.getNombre());
        assertEquals("Modificada", modificada.getDescripcion());
        assertNotSame(original, modificada);
    }

    @Test
    void setters_DeberiaModificarValores() {
        Capacidad capacidad = new Capacidad();
        List<Long> tecnologiasIds = Arrays.asList(1L, 2L);

        capacidad.setId(2L);
        capacidad.setNombre("DevOps");
        capacidad.setDescripcion("Capacidad en DevOps");
        capacidad.setTecnologiasIds(tecnologiasIds);
        capacidad.setActiva(false);

        assertEquals(2L, capacidad.getId());
        assertEquals("DevOps", capacidad.getNombre());
        assertEquals("Capacidad en DevOps", capacidad.getDescripcion());
        assertEquals(tecnologiasIds, capacidad.getTecnologiasIds());
        assertFalse(capacidad.getActiva());
    }

    @Test
    void builder_ConTecnologiasIdsVacia_DeberiaFuncionarCorrectamente() {
        Capacidad capacidad = Capacidad.builder()
                .id(1L)
                .nombre("Test")
                .tecnologiasIds(Collections.emptyList())
                .activa(true)
                .build();

        assertNotNull(capacidad.getTecnologiasIds());
        assertTrue(capacidad.getTecnologiasIds().isEmpty());
        assertTrue(capacidad.getActiva());
    }
}

