package com.example.SACHI;

public class RegistroAlumnoDTO {

    private String txtNombre;
    private String txtApellido;
    private String txtTelefono;
    private String txtCorreo;
    private String plantillaHuella;
    private String fingerId;

    public String getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(String txtNombre) {
        this.txtNombre = txtNombre;
    }

    public String getTxtApellido() {
        return txtApellido;
    }

    public void setTxtApellido(String txtApellido) {
        this.txtApellido = txtApellido;
    }

    public String getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(String txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public String getTxtCorreo() {
        return txtCorreo;
    }

    public void setTxtCorreo(String txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    public String getPlantillaHuella() {
        return plantillaHuella;
    }

    public void setPlantillaHuella(String plantillaHuella) {
        this.plantillaHuella = plantillaHuella;
    }

    public String getFingerId() {
        return fingerId;
    }

    public void setFingerId(String fingerId) {
        this.fingerId = fingerId;
    }
}
