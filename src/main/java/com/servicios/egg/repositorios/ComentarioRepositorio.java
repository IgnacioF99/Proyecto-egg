package com.servicios.egg.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.servicios.egg.entidades.Comentario;

import java.util.List;

@Repository
public interface ComentarioRepositorio extends JpaRepository<Comentario,Long> {

   // Buscar todos los comentarios con alta = true
   List<Comentario> findAllByAltaTrue();
    
}
