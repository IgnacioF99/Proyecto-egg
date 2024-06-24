package com.servicios.egg.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicios.egg.entidades.Comentario;
import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ComentarioRepositorio;
import com.servicios.egg.repositorios.TrabajoRepositorio;

@Service
public class ComentarioServicio {

   @Autowired
   private ComentarioRepositorio comentarioRepositorio;

   @Autowired
   private TrabajoRepositorio trabajoRepositorio;

   @Transactional
   public void crearcComentario(Long id, String comentario) throws MyException {
      validar(comentario);
      Trabajo trabajo = trabajoRepositorio.findById(id).get();
      Comentario newComentario = new Comentario();
      newComentario.setComentario(comentario);
      newComentario.setAlta(true);
      newComentario.setTrabajo(trabajo);
      comentarioRepositorio.save(newComentario);
   }

   @Transactional(readOnly = true)
   public List<Comentario> listarComentario() {
      List<Comentario> comentarios = new ArrayList<>();
      comentarios = comentarioRepositorio.findAll();
      return comentarios;
   }

   @Transactional(readOnly = true)
   public List<Comentario> listarComentarioAlta() {
      List<Comentario> comentarios = new ArrayList<>();
      comentarios = comentarioRepositorio.findAllByAltaTrue();
      return comentarios;
   }

   @Transactional
   public void modificarComentario(Long id, String comentario) throws MyException { // Comentario que puede modificar el
      // USER que recibe el
      // trabajo

      validar(comentario);
      Optional<Comentario> respuesta = comentarioRepositorio.findById(id);
      if (respuesta.isPresent()) {
         Comentario comment = respuesta.get();
         comment.setAlta(true);
         comment.setComentario(comentario);

         comentarioRepositorio.save(comment);
      }
   }

   @Transactional
   public void censurarComentario(Long id) { // Comentario que solo puede censurar el ADMIN

      Optional<Comentario> respuesta = comentarioRepositorio.findById(id);

      if (respuesta.isPresent()) {
         Comentario comment = respuesta.get();
         comment.setAlta(false);

         comentarioRepositorio.save(comment);
      }
   }

   public Comentario getOne(Long id) {
      return comentarioRepositorio.getReferenceById(id);
   }

   private void validar(String comentario) throws MyException {
      if (comentario.isEmpty() || comentario == null) {
         throw new MyException("El comentario no puede quedar vacio");
      }
   }
}
