package com.servicios.egg.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Localidad;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

   // Buscar todos los usuarios con alta = true
   List<Usuario> findAllByAltaTrue();

   // Buscar todos los usuarios por localidad = "ZNorte"
   List<Usuario> findAllByLocalidad(Localidad localidad);

   // Buscar usuario por nombreUser
   @Query("SELECT u FROM Usuario u WHERE u.email = :email")
   public Usuario buscarUsuarioPorEmail(@Param("email") String email);

   @Query("SELECT u FROM Usuario u WHERE u.rol = 'PROV'")
   List<Usuario> buscarProvedores();

}
