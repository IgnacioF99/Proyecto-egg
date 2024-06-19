package com.servicios.egg.repositorios;

import com.servicios.egg.entidades.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario,Long> {

   // Buscar todos los usuarios con alta = true
   List<Usuario> findAllByAltaTrue();

   // Buscar usuario por nombreUser
   @Query("SELECT u FROM Usuario u WHERE u.email = :email")
   public Usuario buscarUsuarioPorEmail( @Param("email") String email);

}
