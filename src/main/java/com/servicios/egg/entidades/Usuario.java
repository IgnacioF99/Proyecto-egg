package com.servicios.egg.entidades;

import java.util.List;

import com.servicios.egg.enums.Localidad;
import com.servicios.egg.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String phone;

    @Column(unique = true)
    private String email;

    private String password;

    @OneToOne
    private Imagen imagen;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    @Enumerated(EnumType.STRING)
    private Localidad localidad;

    private boolean alta;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provedor_id")
    private Provedor provedores;

    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private List<Trabajo> trabajos;
}