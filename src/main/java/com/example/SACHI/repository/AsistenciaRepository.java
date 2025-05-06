package com.example.SACHI.repository;

import com.example.SACHI.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    List<Asistencia> findByAsistenciaFecha(LocalDate asistencia_fecha);

}
