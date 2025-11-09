package co.com.capacidad.model.capacidad.exception;

import java.util.List;

public class TecnologiaNoEncontradaException extends BaseException {

    public TecnologiaNoEncontradaException(List<Long> idsNoEncontrados) {
        super(
                "Una o más tecnologías no fueron encontradas",
                "TECNOLOGIA_NO_ENCONTRADA",
                "Tecnologías no encontradas",
                404,
                List.of(String.format("Las siguientes tecnologías no existen: %s", idsNoEncontrados))
        );
    }
}

