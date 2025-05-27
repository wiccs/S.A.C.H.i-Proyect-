package com.example.SACHI.model;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity //Indicamos que sera una tabla
public class Asistencia {
    @Id  // Esto indica que esta propiedad será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que el ID se genere automáticamente al guardar un nuevo usuario
    private Long asistencia_id;
    @Column(name = "asistencia_fecha")
    private LocalDate asistenciaFecha;
    @Column(name = "asistencia_hora")
    private LocalTime asistenciaHora;
    @Column(name = "asistencia_valor")
    private boolean asistenciaValor;

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Esta es la forma en JPA de hacer foreign key, el campo se llama: usuario_id
    private Usuario usuario; //Muchas asistencia pertenecen a un solo usuario. Por eso este campo es de tipo Usuario.

    public Long getAsistencia_id() {
        return asistencia_id;
    }

    public void setAsistencia_id(Long asistencia_id) {
        this.asistencia_id = asistencia_id;
    }

    public LocalDate getAsistenciaFecha() {
        return asistenciaFecha;
    }

    public void setAsistenciaFecha(LocalDate asistenciaFecha) {
        this.asistenciaFecha = asistenciaFecha;
    }

    public LocalTime getAsistenciaHora() {
        return asistenciaHora;
    }

    public void setAsistenciaHora(LocalTime asistenciaHora) {
        this.asistenciaHora = asistenciaHora;
    }

    public boolean isAsistenciaValor() {
        return asistenciaValor;
    }

    public void setAsistenciaValor(boolean asistenciaValor) {
        this.asistenciaValor = asistenciaValor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
