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

import com.servicios.egg.entidades.Comentario;
import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.enums.Localidad;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.ComentarioServicio;
import com.servicios.egg.servicios.ProvedorServicio;
import com.servicios.egg.servicios.ServicioServicio;
import com.servicios.egg.servicios.TrabajoServicio;
import com.servicios.egg.servicios.UsuarioServicio;

@Controller
@RequestMapping("/provedor")
public class ProvedorControlador {

    @Autowired
    private ProvedorServicio provedorServicio;

    @Autowired
    private TrabajoServicio trabajoServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @PreAuthorize("hasAnyRole('ROLE_PROV','ROLE_USER')")
    @GetMapping("/dashboard")
    public String mostrarPanelProvedor(ModelMap modelo) {
        List<Servicio> servicioList = servicioServicio.listarServicio();
        List<Trabajo> trabajoList = trabajoServicio.listarTrabajos();
        List<Comentario> comentarioList = comentarioServicio.listarComentario();
        modelo.addAttribute("servicios", servicioList);
        modelo.addAttribute("trabajos", trabajoList);
        modelo.addAttribute("comentarios", comentarioList);
        return "panel_provedor.html";
    }

    @GetMapping("/presupuestar/{id}")
    public String presupuestar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("trabajo", trabajoServicio.getOne(id));
        return "presupuesto_form.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable @RequestParam(required = false) Long id,
            String nombre, String email, String phone, MultipartFile archivo, String password, String password2,
            Localidad localidad,
            ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password,
                    password2, phone, localidad);
            modelo.put("exito", "Ha actualizado sin problemas el perfil");

            return "redirect:/provedor/dashboard";
        } catch (MyException e) {
            modelo.put("error", e.getMessage());
            return "usuario_form.html";
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/crear/{id}")
    public String crearProvedor(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("servicios", servicioServicio.listarServicio());
        modelo.addAttribute("usuario", usuarioServicio.getOne(id));
        return "provedor_form.html";
    }

    @GetMapping("/crearTrabajo/{id}")
    public String crearTrabajo(@PathVariable Long id, ModelMap modelo) {
        Provedor provedor = provedorServicio.getOne(id);
        modelo.put("provedor", provedor);
        return "trabajo_form_prov.html";
    }

    @PostMapping("/crearTrabajo/{idProvedor}")
    public String crearTrabajo(@RequestParam Long idUsuario, @PathVariable Long idProvedor, String descripcion,
            ModelMap modelo) {

        try {
            trabajoServicio.crearTrabajo(descripcion, idUsuario, idProvedor);
            modelo.put("exito", "Su solicitud ha sido creada exitosamente!");
            return "redirect:/provedor/dashboard";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "trabajo_form.html";
        }
    }

    // @PostMapping("/modificarServicios/{id}")
    // public String modificarServicio(@RequestParam Long )

    @PostMapping("crear/{id}")
    public String crearProvedor(@PathVariable Long id, @RequestParam List<Long> servicios,
            ModelMap modelo) {
        try {
            List<Servicio> serviciosList = servicioServicio.listarServicios(servicios);
            usuarioServicio.cambiarRol(id);
            provedorServicio.crearProvedor(id, serviciosList);
            modelo.put("exito", "Se ha registrado como proveedor correctamente");
            return "redirect:/provedor/dashboard";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "presupuesto_form.html";
        }
    }

    @PostMapping("/presupuestar/{id}")
    public String presupuestar(@PathVariable Long id, ModelMap modelo, Double presupuesto) {
        try {
            trabajoServicio.cotizarTrabajo(id, presupuesto);
            modelo.put("exito", "Se ha presupuestado correctamente");
            return "redirect:/provedor/dashboard";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "presupuesto_form.html";
        }
    }

    @GetMapping("/finalizar/{id}")
    public String finalizado(@PathVariable Long id, ModelMap modelo) {
        trabajoServicio.finalizarTrabajo(id);
        return "redirect:/provedor/dashboard";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Provedor> provedorList = provedorServicio.listarProvedores();
        modelo.addAttribute("provedores", provedorList);
        return "provedor_list_prov.html";
    }

    @GetMapping("/aceptarTrabajo/{id}")
    public String aceptarTrabajo(@PathVariable Long id) {
        trabajoServicio.aceptarTrabajo(id);
        return "redirect:/provedor/dashboard";
    }

    @GetMapping("/comentar/{id}") //
    public String calificar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("trabajo", trabajoServicio.getOne(id));
        return "comentario_form_prov.html";
    }

    @PostMapping("/comentar/{id}") // id del trabajo, pensar como setear eso
    public String calificar(@PathVariable Long id, @RequestParam String comentario,
            int calificacion, ModelMap modelo) {
        try {
            comentarioServicio.crearcComentario(id, comentario);
            trabajoServicio.modificarCalificacion(id, calificacion);
            modelo.put("exito", "Gracias por comentar");
            return "redirect:/provedor/dashboard";
        } catch (MyException e) {
            modelo.put("error", e.getMessage());
            return "comentario_form.html";
        }
    }

}
