package co.com.capacidad.model.capacidad;

import co.com.capacidad.model.capacidad.gateways.TecnologiaInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CapacidadConTecnologias {
    private Long id;
    private String nombre;
    private String descripcion;
    private Boolean activa;
    private List<TecnologiaInfo> tecnologias;
}


