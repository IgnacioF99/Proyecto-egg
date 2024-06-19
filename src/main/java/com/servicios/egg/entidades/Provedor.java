package com.servicios.egg.entidades;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Provedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean alta;

    private int numeroDeTrabajos;

    private int calificacionPromedio;

    @ManyToOne
    private Usuario usuario;

    @ManyToMany @JoinTable(name ="servicios_has_provedores",
    joinColumns = @JoinColumn(referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private List<Servicio> servicio;
}
