package com.servicios.egg.entidades;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;

    private boolean alta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trabajo_id")//Llave foranea
    private Trabajo trabajo;

}
