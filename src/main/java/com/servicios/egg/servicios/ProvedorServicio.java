package com.servicios.egg.servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.repositorios.ProvedorRepositorio;
import com.servicios.egg.repositorios.ServicioRepositorio;
import com.servicios.egg.repositorios.UsuarioRepositorio;

@Service
public class ProvedorServicio {
   @Autowired
   private ProvedorRepositorio provedorRepositorio;

   @Autowired
   private UsuarioRepositorio usuarioRepositorio;

   @Autowired
   private ServicioRepositorio servicioRepositorio;

   @Transactional
   public void crearProvedor(Long idUsuario, List<Servicio> servicios) throws MyException {

      validar(servicios);

      Provedor provedor = new Provedor();
      Optional<Usuario> respuestaUsuario = usuarioRepositorio.findById(idUsuario);
      if (respuestaUsuario.isPresent()) {
         Usuario usuario = respuestaUsuario.get();
         provedor.setAlta(true);
         provedor.setNumeroDeTrabajos(0);
         provedor.setCalificacionPromedio(0);
         // usuario.setRol(Rol.PROV);
         provedor.setUsuario(usuario);
         provedor.setServicio(servicios);

         provedorRepositorio.save(provedor);
      }

   }

   @Transactional
   private void actualizarProvedor(Long id, List<Servicio> servicios) throws MyException {

      validar(servicios);

      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);
      Provedor provedor = new Provedor();

      if (respuestaProvedor.isPresent()) {
         provedor = respuestaProvedor.get();
         provedor.setServicio(servicios);

         provedorRepositorio.save(provedor);
      }
   }

   @Transactional
   private void actualizarCalificacionProvedor(Long id, int calificacionPromedio) throws MyException {

      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);
      Provedor provedor = new Provedor();

      if (respuestaProvedor.isPresent()) {
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
   public void eliminarProvedor(Long id) {
      Optional<Provedor> respuestaProvedor = provedorRepositorio.findById(id);

      if (respuestaProvedor.isPresent()) {
         Provedor provedor = respuestaProvedor.get();

         provedorRepositorio.delete(provedor);
      }
   }

   private void validar(List<Servicio> servicios) throws MyException {

      if (servicios.isEmpty()) {
         throw new MyException("Debe seleccional al menos un servicio");
      }

   }

   public Provedor getOne(Long id) {
      return provedorRepositorio.getReferenceById(id);
   }

}
