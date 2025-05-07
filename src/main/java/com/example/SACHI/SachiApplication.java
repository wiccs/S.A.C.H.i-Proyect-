package com.example.SACHI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "*") // Permite solicitudes desde cualquier origen

@SpringBootApplication
@EnableScheduling //Permite activar funciones de forma automatica.
public class SachiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SachiApplication.class, args);
	}

}
