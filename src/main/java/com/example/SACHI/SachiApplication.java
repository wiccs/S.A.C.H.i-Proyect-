package com.example.SACHI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableScheduling
public class SachiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(SachiApplication.class);

		// Lee el puerto de la variable de entorno PORT (usada por Railway)
		String port = System.getenv("PORT");
		if (port != null) {
			app.setDefaultProperties(java.util.Collections.singletonMap("server.port", port));
		}

		app.run(args);
	}
}
