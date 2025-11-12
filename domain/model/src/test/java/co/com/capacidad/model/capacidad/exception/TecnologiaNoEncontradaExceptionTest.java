package co.com.capacidad.model.capacidad.exception;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TecnologiaNoEncontradaExceptionTest {


    @Test
    void constructor_ConUnSoloId_DeberiaIncluirIdEnMensaje() {
        List<Long> idsNoEncontrados = Collections.singletonList(500L);
        TecnologiaNoEncontradaException exception = new TecnologiaNoEncontradaException(idsNoEncontrados);

        assertTrue(exception.getErrors().get(0).contains("500"));
    }

    @Test
    void constructor_DeberiaSerInstanciaDeBaseException() {
        TecnologiaNoEncontradaException exception = new TecnologiaNoEncontradaException(List.of(1L));

        assertTrue(exception instanceof BaseException);
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void constructor_ConListaVacia_DeberiaFuncionarCorrectamente() {
        TecnologiaNoEncontradaException exception = new TecnologiaNoEncontradaException(Collections.emptyList());

        assertTrue(exception.getErrors().get(0).contains("[]"));
    }
}

