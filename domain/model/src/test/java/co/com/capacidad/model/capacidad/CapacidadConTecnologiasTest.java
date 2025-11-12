package co.com.capacidad.model.capacidad;

import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapacidadConTecnologiasTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        TecnologiaInfo tecnologia1 = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        TecnologiaInfo tecnologia2 = TecnologiaInfo.builder().id(2L).nombre("Spring").build();
        List<TecnologiaInfo> tecnologias = Arrays.asList(tecnologia1, tecnologia2);

        CapacidadConTecnologias capacidad = CapacidadConTecnologias.builder()
                .id(1L)
                .nombre("Desarrollo Backend")
                .descripcion("Capacidad en desarrollo backend")
                .tecnologias(tecnologias)
                .build();

        assertEquals(1L, capacidad.getId());
        assertEquals("Desarrollo Backend", capacidad.getNombre());
        assertEquals("Capacidad en desarrollo backend", capacidad.getDescripcion());
        assertEquals(tecnologias, capacidad.getTecnologias());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        CapacidadConTecnologias capacidad = CapacidadConTecnologias.builder().build();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getDescripcion());
        assertNull(capacidad.getTecnologias());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        CapacidadConTecnologias capacidad = new CapacidadConTecnologias();

        assertNull(capacidad.getId());
        assertNull(capacidad.getNombre());
        assertNull(capacidad.getDescripcion());
        assertNull(capacidad.getTecnologias());
    }


    @Test
    void setters_DeberiaModificarValores() {
        CapacidadConTecnologias capacidad = new CapacidadConTecnologias();
        TecnologiaInfo tecnologia = TecnologiaInfo.builder().id(1L).nombre("Java").build();
        List<TecnologiaInfo> tecnologias = Collections.singletonList(tecnologia);

        capacidad.setId(2L);
        capacidad.setNombre("DevOps");
        capacidad.setDescripcion("Capacidad en DevOps");
        capacidad.setTecnologias(tecnologias);

        assertEquals(2L, capacidad.getId());
        assertEquals("DevOps", capacidad.getNombre());
        assertEquals("Capacidad en DevOps", capacidad.getDescripcion());
        assertEquals(tecnologias, capacidad.getTecnologias());
    }

    @Test
    void builder_ConTecnologiasVacia_DeberiaFuncionarCorrectamente() {
        CapacidadConTecnologias capacidad = CapacidadConTecnologias.builder()
                .id(1L)
                .nombre("Test")
                .tecnologias(Collections.emptyList())
                .build();

        assertNotNull(capacidad.getTecnologias());
        assertTrue(capacidad.getTecnologias().isEmpty());
    }
}

