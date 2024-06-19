package com.servicios.egg.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.servicios.egg.entidades.Provedor;

import java.util.List;

@Repository
public interface ProvedorRepositorio extends JpaRepository<Provedor,Long> {

   // Buscar todos los proveedores con alta = true
   List<Provedor> findAllByAltaTrue();

   // Buscar el ultimo numero de trabajo de
    
}
