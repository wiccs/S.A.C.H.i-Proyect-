package com.example.SACHI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity  // Esta anotación indica que esta clase es una entidad JPA, lo que la convierte en una tabla de base de datos
public class Usuario {

    @Id  // Esto indica que esta propiedad será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que el ID se genere automáticamente al guardar un nuevo usuario
    private Long id;

    private String usuario_nombre;

    private String usuario_apellido;
    private String usuario_correo;
    private String usuario_template;

    // Constructor vacío (requerido por JPA)
    public Usuario() {
    }

    // Constructor con parámetros para inicializar el usuario fácilmente
    public Usuario(String nombre,String usuario_apellido, String correo, String template) {
        this.usuario_nombre = nombre;
        this.usuario_apellido = usuario_apellido;
        this.usuario_correo = correo;
        this.usuario_template = template;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario_nombre() {
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
}
