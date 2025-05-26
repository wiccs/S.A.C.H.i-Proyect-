package com.example.SACHI.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;

import static com.example.SACHI.controller.R503controller.ipEsp32;

@Service
public class R503service {

    String ipSistemaESP32 = " ";


    public String autenticarConR503auto() {
        System.out.println("Autenticacion automaticada activada");
        String urlESP = "http://" + ipEsp32 + "/R503?estado=autoAutentic";
        RestTemplate restTemplate = new RestTemplate();
        String respuestaESP = restTemplate.getForObject(urlESP, String.class);
        return "Se envió autenticación al ESP32. Respuesta: " + respuestaESP;
    }

    public String DesactivarAutR503auto() {
        System.out.println("Autenticacion automaticada desactivada");
        String urlESP = "http://" + ipEsp32 + "/R503?estado=Encendido";
        RestTemplate restTemplate = new RestTemplate();
        String respuestaESP = restTemplate.getForObject(urlESP, String.class);
        return "Se envió autenticación al ESP32. Respuesta: " + respuestaESP;
    }


    // Se ejecuta automáticamente todos los días a las 7:00 AM
    @Scheduled(cron = "0 41 16 * * *")
    public void tareaProgramada() {

        String respuesta = autenticarConR503auto();
        System.out.println("Ejecución programada: " + respuesta);

    }

    @Scheduled(cron = "0 42 16 * * *")
    public void tareaProgramada2() {

        String respuesta = DesactivarAutR503auto();
        System.out.println("Ejecución programada: " + respuesta);

    }



}
