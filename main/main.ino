#include "Finger503.h"
#include <DisplaySt7796s.h>

#include <FS.h> //Problema en la libreria webServer
using fs::FS;

#include <WiFi.h>
#include <WebServer.h>

//Datos de la Red.
//const char* ssid = "SeventhTics";
//const char* password = "pstcqs#2018$22";
const char* ssid = "wiccsred";
const char* password = "123456789";
WebServer server(80);// Creamos un servidor web escuchando el puerto 80.

//instancia del objeto Finger503
Finger503 finger;
DisplaySt7796s pantallita;




void setup() {
  // Funciones Basicas:
  //finger.iniciar();
  //finger.autenticar();
  //pantallita.encender();

  Serial.begin(115200); 
    WiFi.begin(ssid, password);

    while (WiFi.status() != WL_CONNECTED) {
        delay(500);
        Serial.print(".");
    }
    Serial.println("WiFi Conectado");
    Serial.println("\nConectado a Wi-Fi con IP: " + WiFi.localIP().toString());  

    server.on("/R503", activarSensor);// Springboot
    server.begin();

}

void loop() {
  server.handleClient();//
  
}

//Funcion que activa el sensor R503.
void activarSensor() {
   String estado = server.arg("estado");
    if (estado == "Encendido") {
       Serial.println("Activando sensor de huellas...");
       finger.iniciar();
       finger.registrar();
      server.send(200, "text/plain", "Huella Registrda!");
    } else {
        server.send(200, "text/plain", "Error, algo anda mal...");
    }
    
}



