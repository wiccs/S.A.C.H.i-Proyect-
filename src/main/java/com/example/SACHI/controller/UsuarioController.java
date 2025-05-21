package com.example.SACHI.controller;

import com.example.SACHI.model.Usuario;
import com.example.SACHI.repository.AsistenciaRepository;
import com.example.SACHI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller // Este encabezado es util para vistas HMTL en modelo mvc

//Nuestra ruta principal:
@RequestMapping("/usuarios")
public class UsuarioController {

    //Inyectamos Nuestro repositorio:
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private R503controller r503controller;


    //Este controlaodr se encarga de mostrar los registros, de la paginacion y la busqueda.
    @GetMapping("/all")
    public String listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String query,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Usuario> paginaUsuarios;

        if (query != null && !query.isEmpty()) {
            paginaUsuarios = usuarioRepository.findByUsuarioNombreContaining(query, pageable);

        } else {
            paginaUsuarios = usuarioRepository.findAll(pageable);
        }

        model.addAttribute("usuarios", paginaUsuarios.getContent());
        model.addAttribute("totalPages", paginaUsuarios.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("query", query);

        return "listarUsuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);  // usar la instancia
        r503controller.EliminarConR503(id);
        return "redirect:/usuarios/all";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID no válido: " + id));
        model.addAttribute("usuario", usuario);
        return "ActualizarUsuarios"; // el HTML Thymeleaf
    }

    @PostMapping("/actualizar")
    public String actualizarUsuario(@ModelAttribute Usuario usuario) {
        usuarioRepository.save(usuario); // Spring hace el UPDATE
        return "redirect:/usuarios/all"; // redirige a la lista de usuarios
    }









}
