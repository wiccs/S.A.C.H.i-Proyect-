package com.example.SACHI;


public class AutentRequest {
    private String id_autent; // El id de la persona autenticada.


    // Constructor vacío (requerido por Spring)
    public AutentRequest() {} //Contructor vacio que exige Springboot para hacer la deserializacion

    public String getId_autent() {
        return id_autent;
    }

    public void setId_autent(String id_autent) {
        this.id_autent = id_autent;
    }
}
