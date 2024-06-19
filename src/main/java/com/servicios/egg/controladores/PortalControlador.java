package com.servicios.egg.controladores;

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

import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.UsuarioServicio;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/")
public class PortalControlador {

   @Autowired
   UsuarioServicio usuarioServicio;

   @GetMapping("/")
   public String index() {
      return "index.html";
   }

   @GetMapping("/registrar")
   public String registrar() {
      return "registro.html";
   }

   @PostMapping("/registro")
   public String registro( @RequestParam String nombre, @RequestParam String email, @RequestParam String password,
                           String password2, String phone, ModelMap modelo, MultipartFile archivo ) {
      try {
         usuarioServicio.registrarUsuario(archivo, nombre, email, password, password2, phone);
         modelo.put("exito", "Usuario registrado correctamente!");
//         return "index.html"; evaluar esto

      } catch (MyException ex) {
         modelo.put("error", ex.getMessage());
         modelo.put("nombre", nombre); //En el caso de surgir la excepcion por password, el formulario mediante el th:value guarda los datos cargados antes del error
         modelo.put("email", email);
         modelo.put("phone", phone);

         return "registro.html";
      }

      return "index.html"; // evaluar si esto se requiere aqui
   }

   @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
   @GetMapping("/perfil")
   public String perfil( ModelMap modelo, HttpSession session ) {
      Usuario usuario = (Usuario) session.getAttribute("usuariosession");
      modelo.put("usuario", usuario);
      return "usuario_modificar.html";
   }

   @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
   @PostMapping("/perfil/{id}")
   public String actualizar( MultipartFile archivo, @PathVariable Long id, @RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String phone, ModelMap modelo ) {
      try {
         usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2, phone);
         modelo.put("exito", "Usuario actualizado corectamente");
         return "inicio.html";
      } catch (MyException ex) {
         modelo.put("error", ex.getMessage());
         modelo.put("nombre", nombre);
         modelo.put("email", email);

         return "usuario_modificar.html";
      }
   }

   @GetMapping("/login")
   public String login( @RequestParam(required = false) String error, ModelMap modelo ) {
      if ( error != null ) {
         modelo.put("error", "Usuario o Contraseña inválidos!");
      }
      return "login.html";
   }

   @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_PROV')")
   @GetMapping("/inicio")
   public String inicio( HttpSession session ) {
      Usuario logueado = (Usuario) session.getAttribute("usuariosession");

      if ( logueado.getRol().toString().equalsIgnoreCase("ADMIN") ) {
         return "redirect:/admin/dashboard";
      }

      if ( logueado.getRol().toString().equalsIgnoreCase("USER") ) {
         return "inicio.html";
      }
      return "index.html";
   }

   @GetMapping("/listarServicio")
   public String listarServicio (){
      return "servicios_list.html";
   }
}
