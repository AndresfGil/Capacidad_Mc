package co.com.capacidad.model.capacidad.gateways;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TecnologiaInfoTest {

    @Test
    void builder_DeberiaCrearInstanciaConTodosLosCampos() {
        TecnologiaInfo tecnologiaInfo = TecnologiaInfo.builder()
                .id(1L)
                .nombre("Java")
                .build();

        assertEquals(1L, tecnologiaInfo.getId());
        assertEquals("Java", tecnologiaInfo.getNombre());
    }

    @Test
    void builder_DeberiaCrearInstanciaConCamposNulos() {
        TecnologiaInfo tecnologiaInfo = TecnologiaInfo.builder().build();

        assertNull(tecnologiaInfo.getId());
        assertNull(tecnologiaInfo.getNombre());
    }

    @Test
    void noArgsConstructor_DeberiaCrearInstanciaVacia() {
        TecnologiaInfo tecnologiaInfo = new TecnologiaInfo();

        assertNull(tecnologiaInfo.getId());
        assertNull(tecnologiaInfo.getNombre());
    }

    @Test
    void allArgsConstructor_DeberiaCrearInstanciaConTodosLosParametros() {
        TecnologiaInfo tecnologiaInfo = new TecnologiaInfo(1L, "Python");

        assertEquals(1L, tecnologiaInfo.getId());
        assertEquals("Python", tecnologiaInfo.getNombre());
    }

    @Test
    void setters_DeberiaModificarValores() {
        TecnologiaInfo tecnologiaInfo = new TecnologiaInfo();
        tecnologiaInfo.setId(2L);
        tecnologiaInfo.setNombre("JavaScript");

        assertEquals(2L, tecnologiaInfo.getId());
        assertEquals("JavaScript", tecnologiaInfo.getNombre());
    }
}

