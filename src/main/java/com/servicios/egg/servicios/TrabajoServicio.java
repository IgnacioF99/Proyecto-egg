package com.servicios.egg.servicios;

import java.util.List;

import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Estado;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ProvedorRepositorio;
import com.servicios.egg.repositorios.TrabajoRepositorio;
import com.servicios.egg.repositorios.UsuarioRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public class TrabajoServicio {
   @Autowired
   private TrabajoRepositorio trabajoRepositorio;

   @Autowired
   private UsuarioRepositorio usuarioRepositorio;

   @Autowired
   private ProvedorRepositorio provedorRepositorio;

   @Transactional
   public void crearTrabajo(String descripcion, double presupuesto, Long idUsuario, Long idProvedor)
         throws MyException {

      validar(descripcion, idUsuario);

      Trabajo trabajo = new Trabajo();
      Usuario usuario = usuarioRepositorio.findById(idUsuario).get();

      trabajo.setAlta(true);
      trabajo.setEstado(Estado.SOLICITADO);
      trabajo.setDescripcion(descripcion);
      trabajo.setPresupuesto(0);
      trabajo.setUsuario(usuario);
      trabajo.setProvedor(null);
      trabajo.setComentarios(null);
      trabajo.setCalificacion(5);
   }

   @Transactional
   public void modificarTrabajo(Long id, String descripcion) throws MyException {

      Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

      if (respuestaTrabajo.isPresent()) {
         Trabajo trabajo = new Trabajo();
         trabajo.setDescripcion(descripcion);

         trabajoRepositorio.save(trabajo);
      }
   }

   @Transactional
   public void modificarTrabajoEstado(Long id) throws MyException {

      Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

      if (respuestaTrabajo.isPresent()) {
         Trabajo trabajo = respuestaTrabajo.get();

         if (trabajo.getEstado().equals(Estado.SOLICITADO)) {
            trabajo.setEstado(Estado.ACEPTADO);
         } else if (trabajo.getEstado().equals(Estado.ACEPTADO)) {
            trabajo.setEstado(Estado.TERMINADO);
         }

         trabajoRepositorio.save(trabajo);
      }
   }

   @Transactional
   public List<Trabajo> listarTrabajoPorUsuario(Usuario usuario) {
      List<Trabajo> trabajoList = trabajoRepositorio.findAllByUsuario(usuario);
      return trabajoList;
   }

   @Transactional
   public void cotizarTrabajo(Long id, double presupuesto) throws MyException {

      Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

      if (respuestaTrabajo.isPresent()) {
         Trabajo trabajo = respuestaTrabajo.get();

         if (trabajo.getEstado().equals(Estado.ACEPTADO)) {
            trabajo.setPresupuesto(presupuesto);

            trabajoRepositorio.save(trabajo);
         }
      }
   }

   private void validar(String descripcion, Long idUsuario) throws MyException {

      if (descripcion.isEmpty() || descripcion == null) {
         throw new MyException("la descripcion no puede ser nulo o estar vacío");
      }
      if (idUsuario == null) {
         throw new MyException("el ID de Usuario no puede ser nulo o estar vacío");
      }
   }
}
