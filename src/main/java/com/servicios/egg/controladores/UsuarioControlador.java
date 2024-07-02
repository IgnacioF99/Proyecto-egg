/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

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

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.entidades.Usuario;
import com.servicios.egg.enums.Localidad;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.ComentarioServicio;
import com.servicios.egg.servicios.ProvedorServicio;
import com.servicios.egg.servicios.ServicioServicio;
import com.servicios.egg.servicios.TrabajoServicio;
import com.servicios.egg.servicios.UsuarioServicio;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private TrabajoServicio trabajoServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    @Autowired
    private ProvedorServicio provedorServicio;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/dashboard")
    public String mostrarPanelUsuario(ModelMap modelo) {
        List<Servicio> servicioList = servicioServicio.listarServicio();
        List<Usuario> usuarioList = usuarioServicio.listarUsuarios();
        List<Trabajo> trabajoList = trabajoServicio.listarTrabajos();
        modelo.addAttribute("usuarios", usuarioList);
        modelo.addAttribute("trabajos", trabajoList);
        modelo.addAttribute("localidades", Localidad.values());
        modelo.addAttribute("servicios", servicioList);
        return "panel_usuario.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarioList = usuarioServicio.listarUsuarios();
        // En usuario servicio deberia ir el metodo listar
        modelo.addAttribute("usuarioList", usuarioList);
        return "usuarios_list.html"; // aca deberia ir "usuario_list"
    }

    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable Long id) {
        usuarioServicio.cambiarRolUsuario(id);
        return "redirect:/admin/usuarios";
    }

    /*
     * @GetMapping("/modificar/{id}")
     * public String modificar(@PathVariable Long id, ModelMap modelo) {
     * modelo.put("usuario", usuarioServicio.getOne(id));
     * return "usuario_form.html";
     * }
     */

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable @RequestParam(required = false) Long id,
            String nombre, String email, String phone, MultipartFile archivo, String password, String password2,
            Localidad localidad,
            ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password,
                    password2, phone, localidad);
            modelo.put("exito", "Ha actualizado sin problemas el perfil");
            return "redirect:/usuario/dashboard";
        } catch (MyException e) {
            modelo.put("error", e.getMessage());
            return "usuario_form.html";
        }
    }

    @GetMapping("/crearTrabajo/{id}")
    public String crearTrabajo(@PathVariable Long id, ModelMap modelo) {
        Provedor provedor = provedorServicio.getOne(id);
        modelo.put("provedor", provedor);
        return "trabajo_form.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    @PostMapping("/crearTrabajo/{idProvedor}")
    public String crearTrabajo(@RequestParam Long idUsuario, @PathVariable Long idProvedor, String descripcion,
            ModelMap modelo) {

        try {
            trabajoServicio.crearTrabajo(descripcion, idUsuario, idProvedor);
            modelo.put("exito", "Su solicitud ha sido creada exitosamente!");
            return "redirect:/usuario/dashboard";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "trabajo_form.html";
        }
    }

    @GetMapping("/listarTrabajos/{id}")
    public String listarTrabajos(@PathVariable Long id, ModelMap modelo) {
        List<Trabajo> trabajoList = trabajoServicio.listarTrabajoPorUsuario(id);
        modelo.addAttribute("trabajos", trabajoList);
        return "trabajo_list.html";
    }

    @GetMapping("/presupuesto/{id}")
    public String aceptarPresu(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("trabajo", trabajoServicio.getOne(id));
        return "presupuesto_form.html";
    }

    @GetMapping("/aceptarTrabajo/{id}")
    public String aceptarTrabajo(@PathVariable Long id) {
        trabajoServicio.aceptarTrabajo(id);
        return "redirect:/usuario/dashboard";
    }

    @GetMapping("/comentar/{id}") //
    public String calificar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("trabajo", trabajoServicio.getOne(id));
        return "comentario_form.html";
    }

    @PostMapping("/comentar/{id}") // id del trabajo, pensar como setear eso
    public String calificar(@PathVariable Long id, @RequestParam String comentario,
            ModelMap modelo) {
        try {
            comentarioServicio.crearcComentario(id, comentario);
            // trabajoServicio.modificarCalificacion(id, calificacion);
            modelo.put("exito", "Gracias por comentar");
            return "redirect:/usuario/dashboard";
        } catch (MyException e) {
            modelo.put("error", e.getMessage());
            return "comentario_form.html";
        }
    }

}
