package com.servicios.egg.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.servicios.egg.entidades.Provedor;
import com.servicios.egg.excepciones.MyException;
import com.servicios.egg.servicios.ProvedorServicio;
import com.servicios.egg.servicios.TrabajoServicio;

@Controller
@RequestMapping("/provedor")
public class ProvedorControlador {

    @Autowired
    private ProvedorServicio provedorServicio;

    @Autowired
    private TrabajoServicio trabajoServicio;

    @GetMapping("/presupuestar/{id}")
    public String presupuestar(@PathVariable Long id, ModelMap modelo) {
        modelo.put("trabajo", trabajoServicio.getOne(id));
        return "presupuesto_form.html";
    }

    @PostMapping("/presupuestar/{id}")
    public String presupuestar(@PathVariable Long id, ModelMap modelo, double presupuesto) {
        try {
            trabajoServicio.cotizarTrabajo(id, presupuesto);
            modelo.put("exito", "Se ha presupuestado correctamente");
            return "redirect:/inicio";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "presupuesto_form.html";
        }
    }

    @PostMapping("/finalizado/{id}")
    public String finalizado(@PathVariable Long id, ModelMap modelo) {
        try {
            trabajoServicio.modificarTrabajoEstado(id);
            modelo.put("exito", "El trabajo ya fue finalizado.");
            return "redirect:/inicio";
        } catch (MyException ex) {
            modelo.put("error", ex.getMessage());
            return "presupuesto_form.html";
        }
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Provedor> provedorList = provedorServicio.listarProvedores();
        modelo.addAttribute("provedorList", provedorList);
        return "provedor_list.html";
    }

}
