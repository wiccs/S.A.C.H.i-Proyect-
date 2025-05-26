package com.example.SACHI.controller;

import com.example.SACHI.model.Asistencia;
import com.example.SACHI.model.Usuario;
import com.example.SACHI.repository.AsistenciaRepository;
import com.example.SACHI.repository.UsuarioRepository;
import com.example.SACHI.service.AsistenciaService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model; //Se encarga de las vistas al parecer
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Controller // Este encabezado es util para vistas HMTL en modelo mvc

@RequestMapping("/asistencia") //Nuestra ruta.
public class AsistenciaController {

    //Inyectamos el repositorio con autowired:)
    @Autowired
    private AsistenciaRepository asistenciaRepository;
    private UsuarioRepository usuarioRepository;

    private final AsistenciaService asistenciaService;

    //Esta es la forma en la que se inyecta un servicio:
    @Autowired
    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

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

    @GetMapping("/reporte")
    public void generarReporte(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=asistencias.pdf");

        List<Asistencia> asistencias = asistenciaRepository.findAll(); // o filtradas

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Reporte de Asistencias"));
        document.add(new Paragraph(" ")); // espacio

        for (Asistencia asistencia : asistencias) {
            String linea = String.format("ID: %d | Nombre: %s %s | Fecha: %s | Hora: %s ",
                    asistencia.getAsistencia_id(),
                    asistencia.getUsuario().getUsuarioNombre(),
                    asistencia.getUsuario().getUsuarioApellido(),
                    asistencia.getAsistenciaFecha(),
                    asistencia.getAsistenciaHora()
            );
            document.add(new Paragraph(linea));
        }

        document.close();
    }

    @GetMapping("/reportes")
    public ResponseEntity<byte[]> generarReporteAsistencia(


            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin
    ) {
        try {

            System.out.println("Su fecha es = " + fechaInicio);
            System.out.println("Su fecha final es = " + fechaFin);

            // 1. Obtener asistencias procesadas por fecha y franja horaria
//            Map<LocalDate, Map<String, Boolean>> asistencias =
//                    asistenciaService.obtenerAsistenciasPorHorario(fechaInicio, fechaFin);

            Map<Long, Map<LocalDate, Map<String, Boolean>>> asistencias =
                    asistenciaService.obtenerAsistenciasPorHorario(fechaInicio, fechaFin);


            System.out.println("Asistencias encontradas: " + asistencias);

            // 2. Generar el PDF
            byte[] pdf = asistenciaService.generarReporteAsistenciaPDFParaTodos(asistencias);

            // 3. Preparar headers para que se descargue el archivo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .attachment()
                    .filename("reporte_asistencia.pdf")
                    .build());

            return new ResponseEntity<>(pdf, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // Esto nos dice que puede fallar
            // En caso de error, se devuelve 500 con el mensaje
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

   





}
