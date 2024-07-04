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

import com.servicios.egg.entidades.Comentario;
import com.servicios.egg.servicios.ComentarioServicio;

@Controller
@RequestMapping("/comentario")
public class ComentarioControlador {

    @Autowired
    private ComentarioServicio comentarioServicio;

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {
        List<Comentario> comentarios = comentarioServicio.listarComentario();
        modelo.addAttribute("comentarios", comentarios);
        return "comentario_list.html"; // aca deberia ir "comentario_list"
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {

        return "";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        comentarioServicio.censurarComentario(id);
        return "redirect:/comentario/lista";
    }

    /*
     * @GetMapping("/comentario/eliminar")
     * public String eliminar(@RequestParam("id") Long id) {
     * comentarioServicio.eliminarComentario(id);
     * return "redirect:/comentario/lista"; // Redirige de nuevo a la lista de
     * comentarios
     * }
     */
}
