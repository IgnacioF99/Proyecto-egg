package com.servicios.egg.servicios;

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ProvedorRepositorio;
import com.servicios.egg.repositorios.ServicioRepositorio;
import com.servicios.egg.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProvedorServicio {
   @Autowired
   private ProvedorRepositorio provedorRepositorio;

   @Autowired
   private UsuarioRepositorio usuarioRepositorio;

   @Autowired
   private ServicioRepositorio servicioRepositorio;

   @Transactional
   public void crearProvedor( Long idUsuario, int numeroDeTrabajos, List<Servicio> servicios ) throws MyException {

      validar(servicios);

      Provedor provedor = new Provedor();
      Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

      provedor.setAlta(true);
      provedor.setNumeroDeTrabajos(0);
      provedor.setCalificacionPromedio(5);
      provedor.setUsuario(usuario);
      provedor.setServicio(servicios);

      provedorRepositorio.save(provedor);

   }

   @Transactional
   private void actualizarProvedor( Long id, List<Servicio> servicios ) throws MyException {

      validar(servicios);

      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);
      Provedor provedor = new Provedor();

      if ( respuestaProvedor.isPresent() ) {
         provedor = respuestaProvedor.get();
         provedor.setServicio(servicios);

         provedorRepositorio.save(provedor);
      }
   }

   @Transactional
   private void actualizarCalificacionProvedor( Long id, int calificacionPromedio ) throws MyException {

      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);
      Provedor provedor = new Provedor();

      if ( respuestaProvedor.isPresent() ) {
         provedor = respuestaProvedor.get();
         provedor.setCalificacionPromedio(calificacionPromedio);

         provedorRepositorio.save(provedor);
      }
   }

   @Transactional(readOnly = true)
   public List<Provedor> listarProvedores() {

      List<Provedor> provedorList = new ArrayList<>();
      provedorList = provedorRepositorio.findAll();

      return provedorList;
   }

   @Transactional(readOnly = true)
   public List<Provedor> listarProvedoresAlta() {

      List<Provedor> provedorList = new ArrayList<>();
      provedorList = provedorRepositorio.findAllByAltaTrue();

      return provedorList;
   }

   @Transactional
   public void eliminarProvedor( Long id ) {
      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);

      if ( respuestaProvedor.isPresent() ) {
         Provedor provedor = respuestaProvedor.get();

         provedorRepositorio.delete(provedor);
      }
   }


   private void validar( List<Servicio> servicios ) throws MyException {

      if ( servicios.isEmpty() ) {
         throw new MyException("Debe seleccional al menos un servicio");
      }

   }

}
