package com.example.SACHI.repository;

import com.example.SACHI.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    //Aqui creamos una lista que contendra los registros de fecha, recibe como parametro los registro que queremos que se
    //almacenen en la lista.
    List <Asistencia> findByAsistenciaFecha (LocalDate asistencia_fecha);


}
