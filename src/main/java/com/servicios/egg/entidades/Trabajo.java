package com.servicios.egg.entidades;

import java.util.List;

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

    private Double presupuesto;

    private boolean alta;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private int calificacion;

    @OneToMany(mappedBy = "trabajo", fetch = FetchType.LAZY)
    private List<Comentario> comentarios;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id") // Llave foranea
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provedor_id")
    private Provedor provedor;

}
