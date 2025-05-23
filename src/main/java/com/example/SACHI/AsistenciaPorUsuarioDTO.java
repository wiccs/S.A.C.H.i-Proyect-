package com.example.SACHI;

public class AsistenciaPorUsuarioDTO {
    private String nombreUsuario;
    private Long totalAsistencias;

    public AsistenciaPorUsuarioDTO(String nombreUsuario, Long totalAsistencias) {
        this.nombreUsuario = nombreUsuario;
        this.totalAsistencias = totalAsistencias;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public Long getTotalAsistencias() {
        return totalAsistencias;
    }
}

