package com.example.SACHI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; //Nos permite usar los encabezados propios de una apirest
import org.springframework.web.client.RestTemplate;//Esto sirve para hacer peticiones HTTP desde Spring Boot hacia otro servidor
@CrossOrigin(origins = "http://localhost:8080")

@RestController //Indica que el controlador manejara respuestas rest
@RequestMapping("/R503") //Esta es la ruta(EndPoint) http://localhost:8080/huellas
public class R503controller {

    private String R503 = "Apagado"; //Esta variable String tiene el estado por defecto del sensor.

    @GetMapping("/estado")
    public String obtenerEstadoLed() {
        return R503.equalsIgnoreCase("Encendido") ? "Encendido" : "Apagado";
    }

    @PostMapping("/control")
    public String controlarR503(@RequestParam String estado) {
        R503 = estado;

        // URL del ESP32 (cambia la IP según la que tenga en tu red)
        String urlESP = "http://192.168.72.205/R503?estado=" + estado;

        // Enviar la petición al ESP32
        RestTemplate restTemplate = new RestTemplate();
        String respuestaESP = restTemplate.getForObject(urlESP, String.class);

        return "Spring Boot: " + (R503.equalsIgnoreCase("Encendido") ? "LED Encendido" : "LED Apagado") +
                " | Respuesta ESP32: " + respuestaESP;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFingerprint(@RequestBody FingerprintRequest request) {
        // Aquí procesamos directamente la huella sin servicio
        String base64Template = request.getTemplate();
        System.out.println("Template recibido (Base64): " + base64Template);

        // Puedes hacer más cosas aquí si quieres (como validación, guardado, etc.)
        return ResponseEntity.ok("Huella digital recibida correctamente.");
    }
}


