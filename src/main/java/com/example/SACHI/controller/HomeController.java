package com.example.SACHI.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {

    @GetMapping("/")  // Mapeamos la ruta raíz
    public String index() {
        return "index";  // Thymeleaf buscará la plantilla 'index.html'
    }

    @GetMapping("/registro")
    public String registrar() {
        return "RegisterUser";
    }
}
