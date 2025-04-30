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

    public Long getAsistencia_id() {
        return asistencia_id;
    }

    public void setAsistencia_id(Long asistencia_id) {
        this.asistencia_id = asistencia_id;
    }

    public LocalDate getAsistencia_fecha() {
        return asistencia_fecha;
    }

    public void setAsistencia_fecha(LocalDate asistencia_fecha) {
        this.asistencia_fecha = asistencia_fecha;
    }

    public boolean isAsistencia_valor() {
        return asistencia_valor;
    }

    public void setAsistencia_valor(boolean asistencia_valor) {
        this.asistencia_valor = asistencia_valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
