package com.example.SACHI.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity  // Esta anotación indica que esta clase es una entidad JPA, lo que la convierte en una tabla de base de datos
public class Usuario {

    @Id  // Esto indica que esta propiedad será la clave primaria de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Esto hace que el ID se genere automáticamente al guardar un nuevo usuario
    private Long usuarioId;

    @Column(name = "usuario_nombre")
    private String usuarioNombre;

    @Column(name = "usuario_apellido")
    private String usuarioApellido;

    @Column(name = "usuario_telefono")
    private String usuarioTelefono;
    @Column(name = "usuario_correo")
    private String usuarioCorreo;

    @Column(name = "usuario_template", length = 1000)
//    @Lob //Encabezado para "grandes objetos binarios" (BLOB)
    private String usuarioTemplate;

    @Column(name = "usuario_fingerId")
    private String usuarioFingerId;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias = new ArrayList<>();


    // Constructor vacío (requerido por JPA)
    public Usuario() {
    }

    // Constructor con parámetros para inicializar el usuario fácilmente
    public Usuario(String nombre,String apellido, String telefono, String correo, String template,String fingerId) {
        this.usuarioNombre = nombre;
        this.usuarioApellido = apellido;
        this.usuarioTelefono = telefono;
        this.usuarioCorreo = correo;
        this.usuarioTemplate = template;
        this.usuarioFingerId = fingerId;
    }

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long id) {
        this.usuarioId = id;
    }


    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getUsuarioApellido() {
        return usuarioApellido;
    }

    public void setUsuarioApellido(String usuarioApellido) {
        this.usuarioApellido = usuarioApellido;
    }

    public String getUsuarioTelefono() {
        return usuarioTelefono;
    }

    public void setUsuarioTelefono(String usuarioTelefono) {
        this.usuarioTelefono = usuarioTelefono;
    }

    public String getUsuarioCorreo() {
        return usuarioCorreo;
    }

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    public String getUsuarioTemplate() {
        return usuarioTemplate;
    }

    public void setUsuarioTemplate(String usuarioTemplate) {
        this.usuarioTemplate = usuarioTemplate;
    }

    public String getUsuarioFingerId() {
        return usuarioFingerId;
    }

    public void setUsuarioFingerId(String usuarioFingerId) {
        this.usuarioFingerId = usuarioFingerId;
    }
}
