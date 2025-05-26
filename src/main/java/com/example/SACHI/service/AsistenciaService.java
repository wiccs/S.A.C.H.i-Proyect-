package com.example.SACHI.service;

import com.example.SACHI.model.Asistencia;
import com.example.SACHI.repository.AsistenciaRepository;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

//Los servicios en springboot llevan esta etiqueta es importante usarla por que si no el programa falla:
@Service
public class AsistenciaService {

    //Inyectamos el repositorio con autowired:)
    @Autowired
    private AsistenciaRepository asistenciaRepository;

    private String determinarFranjaHoraria(LocalTime hora) {
        if (hora.isAfter(LocalTime.of(8, 59)) && hora.isBefore(LocalTime.of(10, 1))) {
            return "mañana";
        } else if (hora.isAfter(LocalTime.of(13, 59)) && hora.isBefore(LocalTime.of(15, 1))) {
            return "tarde";
        } else if (hora.isAfter(LocalTime.of(21, 59)) && hora.isBefore(LocalTime.of(22, 59))) {
            return "noche";
        } else {
            return "fuera_de_rango";
        }
    }


    public Map<Long, Map<LocalDate, Map<String, Boolean>>> obtenerAsistenciasPorHorario(LocalDate desde, LocalDate hasta) {
        List<Asistencia> asistencias = asistenciaRepository.findByAsistenciaFechaBetween(desde, hasta);

        Map<Long, Map<LocalDate, Map<String, Boolean>>> resultado = new HashMap<>();

        System.out.println("Asistencias obtenidas: " + asistencias);

        for (Asistencia a : asistencias) {
            Long usuarioId = a.getUsuario().getUsuarioId();

            // Asegurar que haya mapa para el usuario
            resultado.computeIfAbsent(usuarioId, u -> new TreeMap<>());

            Map<LocalDate, Map<String, Boolean>> mapaPorFecha = resultado.get(usuarioId);

            // Asegurar que haya mapa para la fecha
            mapaPorFecha.computeIfAbsent(a.getAsistenciaFecha(), f -> {
                Map<String, Boolean> map = new HashMap<>();
                map.put("mañana", false);
                map.put("tarde", false);
                map.put("noche", false);
                return map;
            });

            if (!a.isAsistenciaValor()) {
                System.out.println("Asistencia ignorada por valor false");
                continue;
            }

            String franja = determinarFranjaHoraria(a.getAsistenciaHora());

            mapaPorFecha.get(a.getAsistenciaFecha()).put(franja, true);
        }

        return resultado;
    }

//Para un solo usuario.
//    public byte[] generarReporteAsistenciaPDF(String nombreUsuario, Map<LocalDate, Map<String, Boolean>> asistencias) throws Exception {
//        Document document = new Document();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        PdfWriter.getInstance(document, out);
//        document.open();
//
//        Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
//        Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);
//
//        document.add(new Paragraph("Reporte de Asistencia", titulo));
//        document.add(new Paragraph("Nombre: " + nombreUsuario, texto));
//        document.add(new Paragraph(" "));
//
//        PdfPTable tabla = new PdfPTable(4);
//        tabla.setWidthPercentage(100);
//        tabla.setWidths(new float[]{3, 2, 2, 2});
//
//        tabla.addCell("Fecha");
//        tabla.addCell("Mañana");
//        tabla.addCell("Tarde");
//        tabla.addCell("Noche");
//
//        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//        for (Map.Entry<LocalDate, Map<String, Boolean>> entrada : asistencias.entrySet()) {
//            tabla.addCell(formatoFecha.format(entrada.getKey()));
//
//            Map<String, Boolean> horarios = entrada.getValue();
//            tabla.addCell(horarios.getOrDefault("mañana", false) ? "●" : "✖");
//            tabla.addCell(horarios.getOrDefault("tarde", false) ? "●" : "✖");
//            tabla.addCell(horarios.getOrDefault("noche", false) ? "●" : "✖");
//        }
//
//        document.add(tabla);
//        document.close();
//
//        return out.toByteArray(); // Para que lo envíes como descarga
//    }
public byte[] generarReporteAsistenciaPDFParaTodos(Map<Long, Map<LocalDate, Map<String, Boolean>>> asistenciasPorUsuario) throws Exception {
    Document document = new Document();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    PdfWriter.getInstance(document, out);
    document.open();

    Font titulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
    Font subtitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
    Font texto = FontFactory.getFont(FontFactory.HELVETICA, 12);

    document.add(new Paragraph("Reporte de Asistencia de Todos los Usuarios", titulo));
    document.add(new Paragraph(" "));

    DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (Map.Entry<Long, Map<LocalDate, Map<String, Boolean>>> usuarioEntry : asistenciasPorUsuario.entrySet()) {
        Long usuarioId = usuarioEntry.getKey();
        Map<LocalDate, Map<String, Boolean>> asistencias = usuarioEntry.getValue();

        // Título para cada usuario
        document.add(new Paragraph("Usuario ID: " + usuarioId, subtitulo));
        document.add(new Paragraph(" "));

        PdfPTable tabla = new PdfPTable(4);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3, 2, 2, 2});

        tabla.addCell("Fecha");
        tabla.addCell("Mañana");
        tabla.addCell("Tarde");
        tabla.addCell("Noche");

        for (Map.Entry<LocalDate, Map<String, Boolean>> entrada : asistencias.entrySet()) {
            tabla.addCell(formatoFecha.format(entrada.getKey()));

            Map<String, Boolean> horarios = entrada.getValue();
            tabla.addCell(horarios.getOrDefault("mañana", false) ? "●" : "✖");
            tabla.addCell(horarios.getOrDefault("tarde", false) ? "●" : "✖");
            tabla.addCell(horarios.getOrDefault("noche", false) ? "●" : "✖");
        }

        document.add(tabla);
        document.add(new Paragraph(" ")); // Espacio entre usuarios
    }

    document.close();

    return out.toByteArray();
}

}
