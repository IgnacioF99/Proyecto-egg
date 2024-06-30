package com.servicios.egg.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.servicios.egg.entidades.Servicio;

@Repository
public interface ServicioRepositorio extends JpaRepository<Servicio, Long> {

   // Buscar todos los servicios con alta = true
   List<Servicio> findAllByAltaTrue();

   @Query("SELECT s FROM Servicio s WHERE s.id IN :id")
   List<Servicio> listarServicios(@Param("id") List<Long> id);

}
