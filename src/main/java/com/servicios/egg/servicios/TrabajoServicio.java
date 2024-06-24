package com.servicios.egg.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Estado;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ProvedorRepositorio;
import com.servicios.egg.repositorios.TrabajoRepositorio;
import com.servicios.egg.repositorios.UsuarioRepositorio;

@Service
public class TrabajoServicio {
   @Autowired
   private TrabajoRepositorio trabajoRepositorio;

   @Autowired
   private UsuarioRepositorio usuarioRepositorio;

   @Autowired
   private ProvedorRepositorio provedorRepositorio;

   @Transactional
   public void crearTrabajo(String descripcion, Long idUsuario, Long idProvedor)
         throws MyException {

      validar(descripcion, idUsuario);

      Trabajo trabajo = new Trabajo();
      Usuario usuario = usuarioRepositorio.findById(idUsuario).get();
      Provedor provedor = provedorRepositorio.findById(idProvedor).get();
      trabajo.setAlta(true);
      trabajo.setEstado(Estado.SOLICITADO);
      trabajo.setDescripcion(descripcion);
      trabajo.setPresupuesto(null);
      trabajo.setUsuario(usuario);
      trabajo.setProvedor(provedor);
      trabajo.setComentarios(null);
      trabajo.setCalificacion(0);

      trabajoRepositorio.save(trabajo);
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
   public List<Trabajo> listarTrabajoPorUsuario(Long id) {
      Optional<Usuario> usuario = usuarioRepositorio.findById(id);
      List<Trabajo> trabajoList = trabajoRepositorio.findAllByUsuario(usuario);
      return trabajoList;
   }

   @Transactional
   public List<Trabajo> listarTrabajos() {
      List<Trabajo> trabajoList = trabajoRepositorio.findAll();
      return trabajoList;
   }

   @Transactional
   public void cotizarTrabajo(Long id, Double presupuesto) throws MyException {

      Optional<Trabajo> respuestaTrabajo = trabajoRepositorio.findById(id);

      if (respuestaTrabajo.isPresent()) {
         Trabajo trabajo = respuestaTrabajo.get();
         trabajo.setPresupuesto(presupuesto);
         trabajo.setEstado(Estado.ACEPTADO);
         trabajoRepositorio.save(trabajo);
      }

   }

   @Transactional
   public void modificarCalificacion(Long id, int calificacion) throws MyException {
      validarCalif(calificacion);
      Optional<Trabajo> respuesta = trabajoRepositorio.findById(id);
      if (respuesta.isPresent()) {
         Trabajo trabajo = respuesta.get();
         trabajo.setCalificacion(calificacion);

         trabajoRepositorio.save(trabajo);
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

   private void validarCalif(int califi) throws MyException {
      if (califi < 0 || califi > 5) {
         throw new MyException("Ha ingresado una calificacion incorrecta");
      }
   }

   public Trabajo getOne(Long id) {
      return trabajoRepositorio.getReferenceById(id);
   }
}
