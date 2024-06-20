package com.servicios.egg.repositorios;

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicios.egg.entidades.Trabajo;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajoRepositorio extends JpaRepository<Trabajo, Long> {

   // Buscar todos los trabajos con alta = true
   List<Trabajo> findAllByAltaTrue();

   // Buscar todos los trabajos con usuario = idUsuario
   List<Trabajo> findAllByUsuario(Usuario usuario);

   // Buscar todos los trabajos con proveedor = idProvedor
   List<Trabajo> findAllByProvedor(Optional<Provedor> provedor);
}
