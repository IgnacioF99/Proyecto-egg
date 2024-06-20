package com.servicios.egg.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.servicios.egg.entidades.Imagen;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ServicioRepositorio;

@Service
public class ServicioServicio {

   @Autowired
   private ServicioRepositorio servicioRepositorio;

   @Autowired
   private ImagenServicio imagenServicio;

   @Transactional
   public void crearServicio(String nombre, String descripcion, MultipartFile archivo) throws MyException {

      validar(nombre, archivo);

      Servicio servicio = new Servicio();

      servicio.setAlta(true);
      servicio.setNombre(nombre.toUpperCase());
      servicio.setDescripcion(descripcion);
      Imagen imagen = imagenServicio.guardar(archivo);
      servicio.setImagen(imagen);

      servicioRepositorio.save(servicio);
   }

   @Transactional
   public void actualizarServicio(Long id, String nombre, String descripcion, MultipartFile archivo)
         throws MyException {

      validar(nombre, archivo);

      Optional<Servicio> respuestaServicio = servicioRepositorio.findById(id);

      if (respuestaServicio.isPresent()) {
         Servicio servicio = respuestaServicio.get();
         servicio.setNombre(nombre.toUpperCase());
         servicio.setDescripcion(descripcion);

         // Solo válida y actualiza la imagen si un archivo es proporcionado
         /*
          * if (archivo != null && !archivo.isEmpty()) {
          * Imagen imagen = imagenServicio.actualizar(archivo,
          * servicio.getImagen().getIdImagen());
          * servicio.setImagen(imagen);
          * }
          */

         String idImagen = servicio.getImagen().getIdImagen();
         if (archivo != null && !archivo.isEmpty()) {
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            servicio.setImagen(imagen);
         } else {
            Imagen imagen = servicio.getImagen();
            servicio.setImagen(imagen);
         }

         servicioRepositorio.save(servicio);
      }
   }

   @Transactional
   public void borrarServicios(Long id) {
      Optional<Servicio> respuestaServicio = servicioRepositorio.findById(id);
      if (respuestaServicio.isPresent()) {
         Servicio servicio = respuestaServicio.get();
         servicio.setAlta(false);
         servicioRepositorio.save(servicio);
      }
   }

   @Transactional(readOnly = true)
   public List<Servicio> listarServicios() {
      List<Servicio> servicioList = new ArrayList<>();
      servicioList = servicioRepositorio.findAll();

      return servicioList;
   }

   @Transactional(readOnly = true)
   public List<Servicio> listarServiciosAlta() {
      List<Servicio> servicioList = new ArrayList<>();
      servicioList = servicioRepositorio.findAllByAltaTrue();

      return servicioList;
   }

   private void validar(String nombre, MultipartFile archivo) throws MyException {
      if (nombre == null || nombre.isEmpty()) {
         throw new MyException("el nombre no puede ser nulo o vacio");
      }

      if (archivo == null || archivo.isEmpty()) {
         throw new MyException("el archivo no puede ser nulo o vacio");
      }
   }

   public Servicio getOne(Long id) {
      return servicioRepositorio.getReferenceById(id);
   }
}
