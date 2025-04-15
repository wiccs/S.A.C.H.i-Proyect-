package com.example.SACHI.model;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity //Indicamos que sera una tabla
public class Asistencia {
    @Id  // Esto indica que esta propiedad será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que el ID se genere automáticamente al guardar un nuevo usuario
    private Long asistencia_id;
    private LocalDate asistencia_fecha;

    private boolean asistencia_valor; // true = asistió, false = no asistió

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Esta es la forma en JPA de hacer foreign key, el campo se llama: usuario_id
    private Usuario usuario; //Muchas asistencia pertenecen a un solo usuario. Por eso este campo es de tipo Usuario.

}
