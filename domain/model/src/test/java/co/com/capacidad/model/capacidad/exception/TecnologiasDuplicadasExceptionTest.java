package co.com.capacidad.model.capacidad.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TecnologiasDuplicadasExceptionTest {

    @Test
    void constructor_DeberiaInicializarConMensajeCorrecto() {
        List<Long> tecnologiasDuplicadas = Arrays.asList(1L, 2L);
        TecnologiasDuplicadasException exception = new TecnologiasDuplicadasException(tecnologiasDuplicadas);

        assertEquals("La lista de tecnologías contiene IDs duplicados", exception.getMessage());
        assertEquals("TECNOLOGIAS_DUPLICADAS", exception.getErrorCode());
        assertEquals("Tecnologías duplicadas", exception.getTitle());
        assertEquals(400, exception.getStatusCode());
        assertNotNull(exception.getErrors());
        assertEquals(1, exception.getErrors().size());
        assertTrue(exception.getErrors().get(0).contains("1"));
        assertTrue(exception.getErrors().get(0).contains("2"));
    }

    @Test
    void constructor_ConUnSoloIdDuplicado_DeberiaIncluirIdEnMensaje() {
        List<Long> tecnologiasDuplicadas = Collections.singletonList(5L);
        TecnologiasDuplicadasException exception = new TecnologiasDuplicadasException(tecnologiasDuplicadas);

        assertTrue(exception.getErrors().get(0).contains("5"));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        TecnologiasDuplicadasException exception = new TecnologiasDuplicadasException(List.of(1L));

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_ConListaVacia_DeberiaFuncionarCorrectamente() {
        TecnologiasDuplicadasException exception = new TecnologiasDuplicadasException(Collections.emptyList());

        assertTrue(exception.getErrors().get(0).contains("[]"));
    }
}

