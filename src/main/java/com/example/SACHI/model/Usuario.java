package com.example.SACHI.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity  // Esta anotación indica que esta clase es una entidad JPA, lo que la convierte en una tabla de base de datos
public class Usuario {

    @Id  // Esto indica que esta propiedad será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que el ID se genere automáticamente al guardar un nuevo usuario
    private Long usuario_id;

    private String usuario_nombre;

    private String usuario_apellido;
    private String usuario_telefono;
    private String usuario_correo;
    @Lob //Encabezado para "grandes objetos binarios" (BLOB)
    private String usuario_template;

    private String usuario_fingerId;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias = new ArrayList<>();


    // Constructor vacío (requerido por JPA)
    public Usuario() {
    }

    // Constructor con parámetros para inicializar el usuario fácilmente
    public Usuario(String nombre,String apellido, String telefono, String correo, String template,String fingerId) {
        this.usuario_nombre = nombre;
        this.usuario_apellido = apellido;
        this.usuario_telefono = telefono;
        this.usuario_correo = correo;
        this.usuario_template = template;
        this.usuario_fingerId = fingerId;
    }

    public String getUsuario_nombre() {
        return usuario_nombre;
    }

    public Long getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Long id) {
        this.usuario_id = id;
    }

    public String getUsuario_nombre(String txtNombre) {
        return usuario_nombre;
    }

    public void setUsuario_nombre(String usuario_nombre) {
        this.usuario_nombre = usuario_nombre;
    }

    public String getUsuario_apellido() {
        return usuario_apellido;
    }

    public void setUsuario_apellido(String usuario_apellido) {
        this.usuario_apellido = usuario_apellido;
    }

    public String getUsuario_telefono() {
        return usuario_telefono;
    }

    public void setUsuario_telefono(String usuario_telefono) {
        this.usuario_telefono = usuario_telefono;
    }

    public String getUsuario_correo() {
        return usuario_correo;
    }

    public void setUsuario_correo(String usuario_correo) {
        this.usuario_correo = usuario_correo;
    }

    public String getUsuario_template() {
        return usuario_template;
    }

    public void setUsuario_template(String usuario_template) {
        this.usuario_template = usuario_template;
    }

    public String getUsuario_fingerId() {
        return usuario_fingerId;
    }

    public void setUsuario_fingerId(String usuario_fingerId) {
        this.usuario_fingerId = usuario_fingerId;
    }
}
