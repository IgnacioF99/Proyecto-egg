package com.servicios.egg.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.servicios.ServicioServicio;
import com.servicios.egg.servicios.UsuarioServicio;

@Controller
@RequestMapping("/imagen")
public class ImagenControlador {
   @Autowired
   UsuarioServicio usuarioServicio;

   @Autowired
   ServicioServicio servicioServicio;

   @GetMapping("/perfil/{id}")
   public ResponseEntity<byte[]> imagenUsuario(@PathVariable Long id) {
      Usuario usuario = usuarioServicio.getOne(id);
      byte[] imagen = usuario.getImagen().getContenido();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);
      return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
   }

   @GetMapping("/servicio/{id}")
   public ResponseEntity<byte[]> imagenServicio(@PathVariable Long id) {
      Servicio servicio = servicioServicio.getOne(id);
      byte[] imagen = servicio.getImagen().getContenido();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);

      return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
   }

   @GetMapping("/provedor/{id}")
   public ResponseEntity<byte[]> imagenProvedor(@PathVariable Long id) {
      Usuario usuario = usuarioServicio.getOne(id);
      byte[] imagen = usuario.getImagen().getContenido();
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.IMAGE_JPEG);

      return new ResponseEntity<>(imagen, headers, HttpStatus.OK);
   }
}
