package co.com.capacidad.model.capacidad.exception;

import java.util.List;

public class TecnologiasDuplicadasException extends BaseException {

    public TecnologiasDuplicadasException(List<Long> tecnologiasDuplicadas) {
        super(
                "La lista de tecnologías contiene IDs duplicados",
                "TECNOLOGIAS_DUPLICADAS",
                "Tecnologías duplicadas",
                400,
                List.of(String.format("Los siguientes IDs de tecnologías están duplicados: %s", tecnologiasDuplicadas))
        );
    }
}

