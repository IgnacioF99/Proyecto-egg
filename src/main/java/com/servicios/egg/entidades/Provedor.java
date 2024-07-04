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

    private Double calificacionPromedio;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "servicios_has_provedores", joinColumns = @JoinColumn(name = "provedor_id"), inverseJoinColumns = @JoinColumn(name = "servicio_id"))
    private List<Servicio> servicio;

    @OneToMany(mappedBy = "provedor", fetch = FetchType.LAZY)
    private List<Trabajo> trabajo;
}
