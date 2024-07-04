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

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.entidades.Servicio;
import com.servicios.egg.entidades.Trabajo;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.ProvedorServicio;
import com.servicios.egg.servicios.ServicioServicio;
import com.servicios.egg.servicios.TrabajoServicio;

@Controller
@RequestMapping("/provedor")
public class ProvedorControlador {

    @Autowired
    private ProvedorServicio provedorServicio;

    @Autowired
    private ServicioServicio servicioServicio;

    @Autowired
    private TrabajoServicio trabajoServicio;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_PROV')")
    @GetMapping("/dashboard")
    public String mostrarPanelProvedor(ModelMap modelo) {
        List<Servicio> servicioList = servicioServicio.listarServicios();
        List<Trabajo> trabajoList = trabajoServicio.listarTrabajos();
        modelo.addAttribute("servicios", servicioList);
        modelo.addAttribute("trabajos", trabajoList);
        return "panel_provedor.html";
    }

    @GetMapping("/presupuestar/{id}")
    public String presupuestar(@PathVariable Long id, ModelMap modelo) {
        modelo.addAttribute("trabajo", trabajoServicio.getOne(id));
        return "presupuesto_form.html";
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
        return "provedor_list.html";
    }

}
