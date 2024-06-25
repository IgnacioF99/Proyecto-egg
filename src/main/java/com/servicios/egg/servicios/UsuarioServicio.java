package com.servicios.egg.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.servicios.egg.entidades.Imagen;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Localidad;
import com.servicios.egg.enums.Rol;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServicio implements UserDetailsService {
   @Autowired
   private UsuarioRepositorio usuarioRepositorio;

   @Autowired
   private ImagenServicio imagenServicio;

   @Override
   public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

      Usuario usuario = usuarioRepositorio.buscarUsuarioPorEmail(email);
      if (usuario != null) {
         List<GrantedAuthority> permisos = new ArrayList<>();
         GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());
         permisos.add(p);

         ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

         HttpSession session = attr.getRequest().getSession(true);
         session.setAttribute("usuariosession", usuario);

         return new User(usuario.getEmail(), usuario.getPassword(), permisos);
      } else {
         return null;
      }

   }

   @Transactional
   public void registrarUsuario(MultipartFile archivo, String nombre, String email, String password, String password2,
         String phone, Localidad localidad) throws MyException {

      validar(nombre, email, password, password2);

      Usuario usuario = new Usuario();

      usuario.setNombre(nombre);
      usuario.setEmail(email);
      usuario.setPassword(new BCryptPasswordEncoder().encode(password));
      usuario.setRol(Rol.USER);
      usuario.setPhone(phone);
      usuario.setLocalidad(localidad);
      usuario.setAlta(true);
      Imagen imagen = imagenServicio.guardar(archivo);
      usuario.setImagen(imagen);

      usuarioRepositorio.save(usuario);
   }

   @Transactional
   public void actualizarUsuario(MultipartFile archivo, Long id, String nombre, String email, String password,
         String password2, String phone, Localidad localidad) throws MyException {

      validar(nombre, email, password, password2);

      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);

      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();
         usuario.setNombre(nombre);
         usuario.setEmail(email);
         usuario.setPassword(new BCryptPasswordEncoder().encode(password));
         usuario.setPhone(phone);
         usuario.setLocalidad(localidad);

         String idImagen = usuario.getImagen().getIdImagen();
         if (archivo != null && !archivo.isEmpty()) {
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
            usuario.setImagen(imagen);
         } else {
            Imagen imagen = usuario.getImagen();
            usuario.setImagen(imagen);
         }

         /*
          * String idImagen = null;
          * if ( usuario.getImagen() != null ) {
          * idImagen = usuario.getImagen().getIdImagen();
          * }
          * Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
          * usuario.setImagen(imagen);
          */

         usuarioRepositorio.save(usuario);
      }
   }

   @Transactional(readOnly = true)
   public List<Usuario> listarUsuarios() {

      List<Usuario> usuariosList = new ArrayList<>();
      usuariosList = usuarioRepositorio.findAll();

      return usuariosList;
   }

   @Transactional(readOnly = true)
   public List<Usuario> listarProvedores() {
      List<Usuario> listaProvedores = new ArrayList<>();
      listaProvedores = usuarioRepositorio.buscarProvedores();
      return listaProvedores;
   }

   @Transactional(readOnly = true)
   public List<Usuario> listarUsuariosAlta() {

      List<Usuario> usuariosList = new ArrayList<>();
      usuariosList = usuarioRepositorio.findAllByAltaTrue();

      return usuariosList;
   }

   @Transactional
   public List<Usuario> buscarUsuariosPorLocalidad(Localidad localidad) {

      List<Usuario> usuarioList = usuarioRepositorio.findAllByLocalidad(localidad);
      return usuarioList;
   }

   @Transactional
   public void cambiarRolUsuario(Long id) {
      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);

      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();

         if (usuario.getRol().equals(Rol.USER)) {
            usuario.setRol(Rol.ADMIN);
         } else if (usuario.getRol().equals(Rol.ADMIN)) {
            usuario.setRol(Rol.USER);
         }
         usuarioRepositorio.save(usuario);
      }
   }

   @Transactional
   public void darDeBajaUsuario(Long id) {
      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);

      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();

         usuario.setAlta(false);
         usuarioRepositorio.save(usuario);
      }
   }

   @Transactional
   public void darDeAltaUsuario(Long id) {
      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);

      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();

         usuario.setAlta(true);
         usuarioRepositorio.save(usuario);
      }
   }

   @Transactional
   public void eliminarUsuario(Long id) {
      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(id);

      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();

         usuarioRepositorio.delete(usuario);
      }
   }

   private void validar(String nombre, String email, String password, String password2) throws MyException {

      if (nombre == null || nombre.isEmpty()) {
         throw new MyException("el nombre no puede ser nulo o estar vacío");
      }
      if (email == null || email.isEmpty()) {
         throw new MyException("el email no puede ser nulo o estar vacío");
      }
      if (password == null || password.isEmpty() || password.length() <= 5) {
         throw new MyException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
      }
      if (!password.equals(password2)) {
         throw new MyException("Las contraseñas ingresadas deben ser iguales");
      }

   }

   public Usuario getOne(Long id) {
      return usuarioRepositorio.getReferenceById(id);
   }
}