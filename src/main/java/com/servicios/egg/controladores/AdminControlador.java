package com.servicios.egg.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Localidad;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.ServicioServicio;
import com.servicios.egg.servicios.UsuarioServicio;

@Controller
@RequestMapping("/admin")
public class AdminControlador {
   @Autowired
   private UsuarioServicio usuarioServicio;

   @Autowired
   private ServicioServicio servicioServicio;

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   @GetMapping("/dashboard")
   public String panelAdministrativo() {
      return "inicio.html";
   }

   @GetMapping("/usuarios")
   public String listar(ModelMap modelo) {
      List<Usuario> usuarioList = usuarioServicio.listarUsuarios();
      modelo.addAttribute("usuarios", usuarioList);

      return "usuarios_list.html";
   }

   @GetMapping("/modificarRol/{id}")
   public String cambiarRol(@PathVariable Long id) {
      usuarioServicio.cambiarRolUsuario(id);
      return "redirect:/admin/usuarios";
   }
   
   @GetMapping("/bajaUsuario/{id}")
   public String darBajaUsuario(@PathVariable Long id) {
      usuarioServicio.darDeBajaUsuario(id);
      return "redirect:/admin/usuarios";
   }
   
   @GetMapping("/altaUsuario/{id}")
   public String darAltaUsuario(@PathVariable Long id) {
      usuarioServicio.darDeAltaUsuario(id);
      return "redirect:/admin/usuarios";
   }

   @GetMapping("/modificar/{id}")
   public String modificar(@PathVariable Long id, ModelMap modelo) {
      modelo.addAttribute("usuario", usuarioServicio.getOne(id));
      modelo.addAttribute("localidades", Localidad.values());

      return "usuario_modificar.html";
   }

   @PostMapping("/modificar/{id}")
   public String modificar( @PathVariable Long id, String nombre, String email, MultipartFile archivo, String password,
                            String password2, String phone, Localidad localidad, ModelMap modelo) {
      try {
         usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2, phone, localidad);
         modelo.put("exito", "Sus datos han sido actualizados correctamente");
         return "redirect:/admin/usuarios";

      } catch (MyException e) {
         modelo.addAttribute("error", e.getMessage());
         modelo.addAttribute("nombre",nombre);
         modelo.addAttribute("email",email);
         modelo.addAttribute("phone",phone);
         modelo.addAttribute("localidades",Localidad.values());

         return "usuario_modificar.html";
      }
   }

   @GetMapping("/crearServicio")
   public String crearServicio() {

      return "servicio_form.html";
   }

   @PostMapping("/crearServicio")
   public String crearServicio( @RequestParam String nombre, @RequestParam String descripcion, MultipartFile archivo, ModelMap modelo) {

       try {
           servicioServicio.crearServicio(nombre,descripcion, archivo);
           return "redirect:/admin/listarServicios";

       } catch (MyException ex) {
         modelo.put("error", ex.getMessage());
         return "redirect:/admin/crearServicio";
       }
   }

   @GetMapping("/listarServicios")
   public String listarServicios(ModelMap modelo) {
      List<Servicio> servicioList = servicioServicio.listarServicios();
      modelo.addAttribute("servicios", servicioList);

      return "servicios_list.html";
   }

   @GetMapping("/modificarServicio/{id}")
   public String modificarServicio(@PathVariable Long id, ModelMap modelo) {
      modelo.addAttribute("servicio", servicioServicio.getOne(id));

      return "servicio_modificar.html";
   }

   @PostMapping("/modificarServicio/{id}")
   public String modificarServicio(@PathVariable Long id, String nombre, String descripcion, MultipartFile archivo, ModelMap modelo){
      try {
         servicioServicio.actualizarServicio(id,nombre,descripcion,archivo);
         modelo.put("exito", "El servicio ha sido actualizado correctamente");

         return "redirect:/admin/listarServicios";

      } catch (MyException e) {
         modelo.put("error", e.getMessage());
         
         return "servicio_modificar.html";
      }
   }
}
