package com.example.SACHI;


public class FingerprintRequest {
    private String template; // La plantilla de huella en Base64

    // Constructor vacío (requerido por Spring)
    public FingerprintRequest() {}

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
