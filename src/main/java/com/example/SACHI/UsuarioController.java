package com.example.SACHI;

import com.example.SACHI.model.Usuario;
import com.example.SACHI.repository.AsistenciaRepository;
import com.example.SACHI.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller // Este encabezado es util para vistas HMTL en modelo mvc
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @GetMapping("/all")
    public String listarUsuarios(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "5") int size,
                                 Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> paginaUsuarios = usuarioRepository.findAll(pageable);

        model.addAttribute("usuarios", paginaUsuarios.getContent());
        model.addAttribute("totalPages", paginaUsuarios.getTotalPages());
        model.addAttribute("currentPage", page);

        return "listarUsuarios"; // tu archivo usuarios.html
    }


}
