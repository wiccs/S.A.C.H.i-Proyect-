package com.example.SACHI;
import com.example.SACHI.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*; //Nos permite usar los encabezados propios de una apirest
import org.springframework.web.client.RestTemplate;//Esto sirve para hacer peticiones HTTP desde Spring Boot hacia otro servidor
import com.example.SACHI.repository.UsuarioRepository;
@CrossOrigin(origins = "http://localhost:8080")

@RestController //Indica que el controlador manejara respuestas rest
@RequestMapping("/R503") //Esta es la ruta(EndPoint) http://localhost:8080/huellas
public class R503controller {

    private static String ultimaPlantillaBase64 = null;

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyectar el repositorio

    private String R503 = "Apagado"; //Esta variable String tiene el estado por defecto del sensor.

    @GetMapping("/estado")
    public String obtenerEstadoLed() {
        return R503.equalsIgnoreCase("Encendido") ? "Encendido" : "Apagado";
    }

    @PostMapping("/control")
    public String controlarR503(@RequestParam String estado) {
        R503 = estado;

        // URL del ESP32 (cambia la IP según la que tenga en tu red)
        String urlESP = "http://192.168.1.70/R503?estado=" + estado;

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
        ultimaPlantillaBase64 = request.getTemplate(); // ¡Aquí la guardamos!

        // Puedes hacer más cosas aquí si quieres (como validación, guardado, etc.)
        return ResponseEntity.ok("Huella digital recibida correctamente.");
    }

    @GetMapping("/getTemplate")
    public ResponseEntity<String> getFingerprintTemplate() {
        if (ultimaPlantillaBase64 == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plantilla no encontrada.");
        }
        return ResponseEntity.ok(ultimaPlantillaBase64);
    }


    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@ModelAttribute RegistroAlumnoDTO dto) {
        // Mapear los datos del DTO a la entidad Usuario

        Usuario usuario = new Usuario(
                dto.getTxtNombre(),
                dto.getTxtApellido(),
                dto.getTxtTelefono(),
                dto.getTxtCorreo(),
                dto.getPlantillaHuella());

        // Guardar el usuario en la base de datos
        usuarioRepository.save(usuario); // Aquí es donde realmente guardas el usuario

        // Retornar una respuesta exitosa
        return ResponseEntity.ok("Usuario registrado correctamente");
    }
}


