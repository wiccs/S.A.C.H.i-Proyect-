package com.example.SACHI;

import com.example.SACHI.model.Asistencia;
import com.example.SACHI.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model; //Se encarga de las vistas al parecer
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

@Controller // Este encabezado es util para vistas HMTL en modelo mvc

@RequestMapping("/asistencia") //Nuestra ruta.
public class AsistenciaController {

    //Inyectamos el repositorio con autowired:)
    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @GetMapping("/hoy")
    public String verAsistenciasHoy(Model model) {
        LocalDate hoy = LocalDate.now();

        List<Asistencia> asistenciasHoy = asistenciaRepository.findByAsistenciaFecha(hoy);

        model.addAttribute("asistencias", asistenciasHoy);
        return "asistencias-hoy"; // apunta a src/main/resources/templates/asistencias-hoy.html
    }
}
