#include "Finger503.h"
#include <DisplaySt7796s.h>

#include <FS.h> //Problema en la libreria webServer
using fs::FS;

#include <WiFi.h>
#include <WebServer.h>

//salon:.
//const char* ssid = "SeventhTics";
//const char* password = "pstcqs#2018$22";

//Mi celular:
const char* ssid = "wiccsred";
const char* password = "123456789";

//Mi casita:
//const char* ssid = "INFINITUM841B";
//const char* password = "E1KUEL7CZHA";

#define LED_PIN 2 


WebServer server(80);// Creamos un servidor web escuchando el puerto 80.


//instancia del objeto Finger503 y DisplaySt7796s:
Finger503 finger;
DisplaySt7796s pantallita;




void setup() {
  
  Serial.println("Activando sensor de huellas...");
       finger.iniciar();
       server.send(200, "text/plain", "Sensor activado!");

   pinMode(LED_PIN, OUTPUT);
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

    digitalWrite(LED_PIN, HIGH);
    delay(1000);
    digitalWrite(LED_PIN, LOW);
    Serial.println("WiFi Conectado");
    Serial.println("\nConectado a Wi-Fi con IP: " + WiFi.localIP().toString());  

    server.on("/R503", activarSensor);// Escucha las solicitudes de esta ruta (Springboot)
   
    server.begin();

}

void loop() {
  server.handleClient();//Se mantiene escuchando las solicitudes. 
  
}


//Funcion que activa el sensor R503.
void activarSensor() {
   String estado = server.arg("estado");
    if (estado == "Encendido") {
       Serial.println("Activando sensor de huellas...");
       finger.iniciar();
       server.send(200, "text/plain", "Sensor activado!");
       //finger.registrar();
      //server.send(200, "text/plain", "Huella Registrda!");
    } else {
        if(estado == "Registrando"){
          Serial.println("Activando la funcion de registro...");
          finger.registrar();
          server.send(200,"text/plain","Registro completado!");
        }else{
          if(estado == "Autenticando"){
          Serial.println("Activando la funcion de registro...");
          finger.autenticar();
          server.send(200,"text/plain","Autenticacion Finalizada!");
    }
    else{
      server.send(200, "text/plain", "Error, algo anda mal...");
    }
    }
    }
    }
    






    






