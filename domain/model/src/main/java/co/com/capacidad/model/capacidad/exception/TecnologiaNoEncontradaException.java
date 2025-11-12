package co.com.capacidad.model.capacidad.exception;

import java.util.List;

public class TecnologiaNoEncontradaException extends BaseException {

    public TecnologiaNoEncontradaException(List<Long> idsNoEncontrados) {
        super(
                "Una o más tecnologías no fueron encontradas o estan inactivas",
                "TECNOLOGIA_NO_ENCONTRADA",
                "Tecnologías no encontradas o inactivas",
                404,
                List.of(String.format("Las siguientes tecnologías no existen o estan inactivas: %s", idsNoEncontrados))
        );
    }
}

