package co.com.capacidad.model.capacidad;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Capacidad {

    private Long id;
    private String nombre;
    private String descripcion;
    private List<Long> tecnologiasIds;

}
