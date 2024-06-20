/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.servicios.egg.controladores;

import java.util.List;

import com.servicios.egg.enums.Localidad;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "index"; // Aca deberia ir un "inicio.html" o modificar el index
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

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable Long id, ModelMap modelo) {
        modelo.put("usuario", usuarioServicio.getOne(id));
        return "usuario_form.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable @RequestParam(required = false) Long id,
            String nombre, String email, String phone, MultipartFile archivo, String password, String password2, Localidad localidad,
            ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2, phone, localidad);
            modelo.put("exito", "Ha actualizado sin problemas el perfil");
            return "redirect:/usuario/dashboard";
        } catch (MyException e) {
            modelo.put("error", e.getMessage());
            return "usuario_form.html";
        }
    }
}
