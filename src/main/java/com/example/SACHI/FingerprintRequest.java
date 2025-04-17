package com.example.SACHI;


public class FingerprintRequest {
    private String template; // La plantilla de huella en Base64
    private String idFinger;

    // Constructor vacío (requerido por Spring)
    public FingerprintRequest() {} //Contructor vacio que exige Springboot para hacer la deserializacion

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getIdFinger() {
        return idFinger;
    }

    public void setIdFinger(String idFinger) {
        this.idFinger = idFinger;
    }
}
