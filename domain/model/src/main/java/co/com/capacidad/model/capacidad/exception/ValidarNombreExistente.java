package co.com.capacidad.model.capacidad.exception;

import java.util.List;

public class ValidarNombreExistente extends BaseException {

    public ValidarNombreExistente(String nombre) {
        super(
                "El nombre de la tecnología ya está registrado",
                "NOMBRE_EXISTENTE",
                "Nombre no disponible",
                409,
                List.of(String.format("Ya existe una tecnología con el nombre '%s'", nombre))
        );
    }
}
