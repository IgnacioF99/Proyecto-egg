package com.servicios.egg.servicios;

import com.servicios.egg.entidades.Imagen;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ImagenRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class ImagenServicio {

   @Autowired
   private ImagenRepositorio imagenRepositorio;

   public Imagen guardar( MultipartFile archivo ) throws MyException {
      if ( archivo != null ) {
         try {
            Imagen imagen = new Imagen();
            imagen.setMine(archivo.getContentType());
            imagen.setNombreImagen(archivo.getName());
            imagen.setContenido(archivo.getBytes());
            return imagenRepositorio.save(imagen);
         } catch (Exception ex) {
            System.err.println(ex.getMessage());
         }
      }
      return null;
   }

   public Imagen actualizar( MultipartFile archivo, String idImagen ) throws MyException {
      if ( archivo != null ) {
         try {
            Imagen imagen = new Imagen();
            if ( idImagen != null ) {
               Optional<Imagen> respuestaImagen = imagenRepositorio.findById(idImagen);
               if ( respuestaImagen.isPresent() ) {
                  imagen = respuestaImagen.get();
               }
            }
            imagen.setMine(archivo.getContentType());
            imagen.setNombreImagen(archivo.getName());
            imagen.setContenido(archivo.getBytes());
            return imagenRepositorio.save(imagen);
         } catch (Exception ex) {
            System.err.println(ex.getMessage());
         }
      }
      return null;
   }

   @Transactional(readOnly = true)
   public List<Imagen> listarTodos() {
      return imagenRepositorio.findAll();
   }

   @SuppressWarnings("deprecation")
   public Imagen getOne( String id ) {
      return imagenRepositorio.getOne(id);
   }
}
