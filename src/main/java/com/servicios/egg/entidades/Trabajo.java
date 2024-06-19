package com.servicios.egg.entidades;

import com.servicios.egg.enums.Estado;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;

    private double presupuesto;

    private boolean alta;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private int calificacion;

    @ManyToOne
    private Comentario comentario;

    @ManyToOne
    private Usuario usuario;

    @OneToOne
    private Provedor provedor;

}
