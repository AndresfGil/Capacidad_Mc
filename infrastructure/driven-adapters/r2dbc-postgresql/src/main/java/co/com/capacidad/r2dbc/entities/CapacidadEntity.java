package co.com.capacidad.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("capacidades")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class CapacidadEntity {

    @Id
    private Long id;

    private String nombre;
    private String descripcion;

    @Column("tecnologias_ids")
    private String tecnologiasIds;

    private Boolean activa;

    @Column("fecha_modificacion")
    private LocalDateTime fechaModificacion;
}
