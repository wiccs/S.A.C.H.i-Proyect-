package com.example.SACHI.controller;

import com.example.SACHI.model.Asistencia;
import com.example.SACHI.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model; //Se encarga de las vistas al parecer
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/filtrar")
    public String filtrarAsistencias(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model
    ) {
        List<Asistencia> asistencias;

        if (fechaInicio != null && fechaFin != null) {
            asistencias = asistenciaRepository.findByAsistenciaFechaBetween(fechaInicio, fechaFin);
        } else if (fechaInicio != null) {
            asistencias = asistenciaRepository.findByAsistenciaFecha(fechaInicio);
        } else {
            asistencias = asistenciaRepository.findAll(); // o vacía
        }

        model.addAttribute("asistencias", asistencias);
        return "usuariosFiltrados";
    }

    @GetMapping("/filtro")
    public String verfiltro(Model model) {
        return "selector-fechas";
    }




}
