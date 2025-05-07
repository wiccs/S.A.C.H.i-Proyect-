#include <stdint.h>
#include "esp32-hal.h"
#include "WString.h"
#include "HardwareSerial.h"
#include "Finger503.h"
#include <Adafruit_Fingerprint.h>
#include <Base64.h>
#include <HTTPClient.h>



#define RX_PIN 16  // GPIO16 del ESP32 para RX (conectado a TX del sensor)
#define TX_PIN 17  // GPIO17 del ESP32 para TX (conectado a RX del sensor)

//--------------------------------------------------------- Metodos Principales ----------------------------------------------------------------------

// Constructor de la clase Finger503
Finger503::Finger503() : mySerial(2), finger(&mySerial) {
  // Inicialización de las variables miembro de la clase
}

// Método para iniciar el sensor
void Finger503::iniciar() {
  mySerial.begin(57600, SERIAL_8N1, RX_PIN, TX_PIN);
  finger.begin(57600);
  //Serial.begin(115200);//Comentar po si el sensor no se activa despues de la primera vez. 
  Serial.println("Buscando el sensor...");
  
  if (finger.verifyPassword()) {
     finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_PURPLE);
    Serial.println("¡Sensor detectado :)!");
    delay(5000);
    finger.LEDcontrol(FINGERPRINT_LED_OFF, 250, FINGERPRINT_LED_PURPLE);
     
  } else {
    Serial.println("No se pudo detectar el sensor.");
    while (1); // Si no se detecta el sensor, el programa se detiene
  }
}

// Método para registrar una huella
void Finger503::registrar() {
  finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_BLUE);
  
  int id = buscarIDLibre();

  int result = registerFingerprint(id);

  if (result >= 0) {
    Serial.print("Huella registrada con éxito en ID ");
    Serial.println(result);

    downloadFingerprintTemplate(id);

    // Mostrar número de huellas almacenadas después del registro
    finger.LEDcontrol(FINGERPRINT_LED_OFF, 250, FINGERPRINT_LED_BLUE);
    Serial.print("Número de huellas almacenadas después de registrar: ");
    Serial.println(finger.templateCount);
  
  //String hexTemplate = downloadFingerprintTemplate(1);
  //Serial.println("Plantilla obtenida:");
  //Serial.println(hexTemplate);

  //Enviamos el id al servidor para ser alamacenado

  } else {
    Serial.println("Falló el registro de huella.");
  }


  delay(5000);  // Espera antes de registrar otra huella
}
// Método para autenticar una huella
uint8_t Finger503::autenticar() {

    uint8_t p;
  Serial.println("Coloca el dedo en el sensor...");
  finger.LEDcontrol(FINGERPRINT_LED_ON, 0, FINGERPRINT_LED_BLUE);
  // Espera hasta que el usuario coloque el dedo
  while ((p = finger.getImage()) != FINGERPRINT_OK) { 
    finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_BLUE);
    delay(1000);
  }

   p = finger.getImage();

  switch (p) {
    case FINGERPRINT_OK:
    finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_PURPLE);
      Serial.println("Imagen tomada");
      break;
    case FINGERPRINT_NOFINGER:
      Serial.println("Huella no detectada");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Error de comunicacion");
      return p;
    case FINGERPRINT_IMAGEFAIL:
      Serial.println("Error de la imagen");
      return p;
    default:
      Serial.println("Error desconocido");
      return p;
  }

    

  // OK success!

  p = finger.image2Tz();
  
  switch (p) {
    case FINGERPRINT_OK:
      Serial.println("Imagen convertida");
      finger.LEDcontrol(FINGERPRINT_LED_OFF, 0, FINGERPRINT_LED_PURPLE);
      break;
    case FINGERPRINT_IMAGEMESS:
      Serial.println("Imagen muy desordenada");
      return p;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Error de comunicacion");
      return p;
    case FINGERPRINT_FEATUREFAIL:
      Serial.println("No se pudieron encontrar las características de las huellas dactilares");
      return p;
    case FINGERPRINT_INVALIDIMAGE:
      Serial.println("No se pudieron encontrar las características de las huellas dactilares");
      return p;
    default:
      Serial.println("Error desconocido");
      return p;
  }

  // OK converted!
  p = finger.fingerSearch();
  if (p == FINGERPRINT_OK) {
    Serial.println("Se encontro una coincidencia!");
  } else if (p == FINGERPRINT_PACKETRECIEVEERR) {
    Serial.println("Error de comunicacion");
    return p;
  } else if (p == FINGERPRINT_NOTFOUND) {
    Serial.println("No se encontro ninguna coincidencia");
    return p;
  } else {
    Serial.println("Error desconocido");
    return p;
  }

  // found a match!
  Serial.print("ID Encontrado en: "); Serial.print(finger.fingerID);

  uint16_t idAutent = finger.fingerID;

  sendAutenticationId(idAutent);

  Serial.print("Con confianza de: "); Serial.println(finger.confidence);

  

  return finger.fingerID;
}

void Finger503::formatearBd(){
  
Serial.println("\n\nDeleting all fingerprint templates!");
  //Serial.println("Press 'Y' key to continue");

 // while (1) {
  //  if (Serial.available() && (Serial.read() == 'Y')) {
   //   break;
   // }
 // }

  // set the data rate for the sensor serial port
  //finger.begin(57600);

  if (finger.verifyPassword()) {
    Serial.println("Found fingerprint sensor!");
  } else {
    Serial.println("Did not find fingerprint sensor :(");
    while (1);
  }

  finger.emptyDatabase();

  Serial.println("Now database is empty :)");
}








// ------------------------------------------------Métodos auxiliares para huellas-----------------------------------------------------------
int Finger503::registerFingerprint(int id) {
  Serial.print("Coloca el dedo para registrar en ID ");
  finger.LEDcontrol(FINGERPRINT_LED_ON, 0, FINGERPRINT_LED_BLUE);
  Serial.println(id);

  uint8_t p;

  // Espera hasta que el usuario coloque el dedo
  while ((p = finger.getImage()) != FINGERPRINT_OK) {
    Serial.println("Coloca el dedo en el sensor...");
    delay(1000);
  }

  p = finger.image2Tz(1);
  if (p != FINGERPRINT_OK) {
    Serial.println("Error al convertir imagen");
    return -1;
  }

  Serial.println("Retira el dedo...");
  finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_PURPLE);
  delay(2000);

  // Espera a que el usuario quite el dedo
  while (finger.getImage() != FINGERPRINT_NOFINGER) {
    delay(100);
  }
  
  finger.LEDcontrol(FINGERPRINT_LED_OFF, 0, FINGERPRINT_LED_PURPLE);
  finger.LEDcontrol(FINGERPRINT_LED_ON, 0, FINGERPRINT_LED_BLUE);

  Serial.println("Coloca el mismo dedo nuevamente...");

  // Espera hasta que el usuario coloque el dedo de nuevo
  while ((p = finger.getImage()) != FINGERPRINT_OK) {
    Serial.println("Coloca el dedo en el sensor...");
    delay(1000);
  }

  finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 250, FINGERPRINT_LED_PURPLE);

  p = finger.image2Tz(2);
  if (p != FINGERPRINT_OK) {
    Serial.println("Error al convertir segunda imagen");
    return -1;
  }

  p = finger.createModel();
  if (p != FINGERPRINT_OK) {
    Serial.println("Error al crear modelo de huella");
    return -1;
  }

  // Guardar el modelo en el ID proporcionado
  p = finger.storeModel(id);
  if (p == FINGERPRINT_OK) {
    Serial.print("Huella guardada en ID ");
    Serial.println(id);
    finger.LEDcontrol(FINGERPRINT_LED_BREATHING, 10, FINGERPRINT_LED_FLASHING);
    finger.LEDcontrol(FINGERPRINT_LED_ON, 0, FINGERPRINT_LED_BLUE);

    // Pausa para asegurar que el sensor actualice la memoria
    delay(4000);  // Añadir un retraso para dar tiempo a que el sensor actualice

    // Forzar una relectura del número de huellas almacenadas
    Serial.print("Número de huellas almacenadas después de guardar: ");
    Serial.println(finger.getTemplateCount());

    return id;
  } else {
    Serial.println("Error al guardar la huella.");
    return -1;
  }
}
// returns -1 if failed, otherwise returns ID #
int Finger503::getFingerprintIDez() {
  uint8_t p = finger.getImage();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.image2Tz();
  if (p != FINGERPRINT_OK)  return -1;

  p = finger.fingerFastSearch();
  if (p != FINGERPRINT_OK)  return -1;

  // found a match!
  Serial.print("Found ID #"); Serial.print(finger.fingerID);
  Serial.print(" with confidence of "); Serial.println(finger.confidence);
  return finger.fingerID;
}
//funcion para buscar ID sin asignar:
//Funcion para buscar id's sin asignar.
int Finger503::buscarIDLibre() {
    for (int id = 1; id <= 127; id++) {  // ID válidos (depende del modelo)
        if (finger.loadModel(id) != FINGERPRINT_OK) {  
            return id;  // Si no se puede cargar, significa que está libre
        }
    }
    return -1; // No hay IDs libres
}

// Métodos auxiliares para huellas
uint8_t Finger503::downloadFingerprintTemplate(uint16_t id) {
  Serial.println("------------------------------------");
  Serial.print("Intentando cargar #"); Serial.println(id);
  uint8_t p = finger.loadModel(id);
  switch (p) {
    case FINGERPRINT_OK:
      Serial.print("Template "); Serial.print(id); Serial.println(" loaded");
      break;
    case FINGERPRINT_PACKETRECIEVEERR:
      Serial.println("Error de comunicacion");
      return p;
    default:
      Serial.print("Error desconocido "); Serial.println(p);
      return p;
  }

  // OK success!

  Serial.print("Intentando obtener #"); Serial.println(id);
  p = finger.getModel();
  switch (p) {
    case FINGERPRINT_OK:
      Serial.print("Template "); Serial.print(id); Serial.println(" transferring:");
      break;
    default:
      Serial.print("Error desconocido "); Serial.println(p);
      return p;
  }
  //Este primer buffer prepara los datos recibidos por el sensor de forma cruda.
  //El R503, puede enviar datos en 2 paquetes de 267 bites, 11 de cada uno son "inutiles"
  uint8_t bytesReceived[534]; // reservamos 2 paquetes de datos de 267 en un buffer..
  memset(bytesReceived, 0xff, 534); //Luego usamos memeset, para que cada seccion de memoria se llene con 0xff (256, es decir nada.)

  uint32_t starttime = millis(); //Aqui declaramos un tiempo limite por si el sensor se queda colgado.
  int i = 0;
  while (i < 534 && (millis() - starttime) < 20000) {
    if (mySerial.available()) {
      bytesReceived[i++] = mySerial.read();
    }
  }
  Serial.print(i); Serial.println(" bytes read.");
  Serial.println("Decoding packet...");


// Buffer para almacenar la plantilla real de la huella digital
  uint8_t fingerTemplate[512]; // the real template
  memset(fingerTemplate, 0xff, 512);

  // filtering only the data packets
  int uindx = 9, index = 0;
  memcpy(fingerTemplate + index, bytesReceived + uindx, 256);   // Se pasan los primeros 256 bites del primer buffer al segundo.
  uindx += 256;       // skip data
  uindx += 2;         // skip checksum      //Se hace uso de un filtro para filtrar los 11 que no son utiles.
  uindx += 9;         // skip next header
  index += 256;       // advance pointer
  memcpy(fingerTemplate + index, bytesReceived + uindx, 256);   // segundos 256 bytes

  for (int i = 0; i < 512; ++i) {
  //Serial.print("0x");
  printHex(fingerTemplate[i], 2);
  //Serial.print(", ");
  }
  Serial.println("\ndone.");
  
  String base64Template = base64::encode(fingerTemplate, sizeof(fingerTemplate)); //Convertimos el arreglo de la plantilla en base64 y lo guardamos en una variable
  Serial.println("Plantilla lista para ser enviada!");
  
  sendTemplate(base64Template,id);
 // Serial.println(base64Template);
  
 

  return p;

  /*
    uint8_t templateBuffer[256];
    memset(templateBuffer, 0xff, 256);  //zero out template buffer
    int index=0;
    uint32_t starttime = millis();
    while ((index < 256) && ((millis() - starttime) < 1000))
    {
    if (mySerial.available())
    {
      templateBuffer[index] = mySerial.read();
      index++;
    }
    }

    Serial.print(index); Serial.println(" bytes read");

    //dump entire templateBuffer.  This prints out 16 lines of 16 bytes
    for (int count= 0; count < 16; count++)
    {
    for (int i = 0; i < 16; i++)
    {
      Serial.print("0x");
      Serial.print(templateBuffer[count*16+i], HEX);
      Serial.print(", ");
    }
    Serial.println();
    }*/
}

// Métodos auxiliares para huellas
void Finger503::printHex(int num, int precision) {
  char tmp[16];
  char format[128];

  sprintf(format, "%%.%dX", precision);

  sprintf(tmp, format, num);
  Serial.print(tmp);
}

/*
//metodo auxiliar para enviar la huella al servidor de springboot:
void Finger503::sendTemplate(String base64Template) {

  
    HTTPClient http;
    
    const String serverUrl = "http://localhost:8080/R503/upload"; 
    
    // Especificamos la URL del servidor
    http.begin(serverUrl);

    // Establecemos el tipo de contenido como JSON
    http.addHeader("Content-Type", "application/json");
    
    // Creamos el cuerpo de la solicitud con la plantilla Base64
    String jsonBody = "{\"template\":\"" + base64Template + "\"}";

    // Realizamos la solicitud POST
    int httpResponseCode = http.POST(jsonBody);

    // Revisamos la respuesta
    if (httpResponseCode > 0) {
      Serial.print("Respuesta del servidor: ");
      Serial.println(httpResponseCode);
    } else {
      Serial.print("Error en la solicitud POST: ");
      Serial.println(httpResponseCode);
    }

    // Cerramos la conexión
    http.end();
  
}*/

void Finger503::sendTemplate(String base64Template,uint16_t id) {
  
    HTTPClient http;
    
    //const String serverUrl = "http://localhost:8080/R503/upload";  // URL del servidor
    //const String serverUrl = "http://192.168.72.144:8080/R503/upload";//celular
     const String serverUrl = "http://192.168.227.144:8080/R503/upload";


    
    // Inicializamos la conexión
    http.begin(serverUrl);
    
    // Establecemos el tipo de contenido como JSON
    http.addHeader("Content-Type", "application/json");
    
    // Creamos el cuerpo de la solicitud con la plantilla Base64
    String jsonBody = "{\"template\":\"" + base64Template + "\", \"idFinger\":" + String(id) + "}";

 
    
    // Tiempo de espera en milisegundos (opcional)
    http.setTimeout(5000);  // 5 segundos
    
    // Realizamos la solicitud POST
    int httpResponseCode = http.POST(jsonBody);
    
    // Revisamos la respuesta
    if (httpResponseCode > 0) {
        // Si la respuesta es exitosa, mostramos el código de respuesta y el cuerpo del servidor
        String response = http.getString();
        Serial.print("Respuesta del servidor: ");
        Serial.println(httpResponseCode);
        Serial.print("Cuerpo de la respuesta: ");
        Serial.println(response);
    } else {
        // Si la solicitud falla, mostramos el código de error
        Serial.print("Error en la solicitud POST: ");
        Serial.println(httpResponseCode);
    }
    
    // Cerramos la conexión
    http.end();
}

void Finger503::sendAutenticationId(uint16_t id) {
  
    HTTPClient http;
    
    //const String serverUrl = "http://localhost:8080/R503/upload";  // URL del servidor
    //const String serverUrl = "http://192.168.72.144:8080/R503/upload";//celular
     const String serverUrl = "http://192.168.227.144:8080/R503/uploadAutent";//


    
    // Inicializamos la conexión
    http.begin(serverUrl);
    
    // Establecemos el tipo de contenido como JSON
    http.addHeader("Content-Type", "application/json");
    
    // Creamos el cuerpo de la solicitud con la plantilla Base64
    String jsonBody = "{\"id_autent\":" + String(id) + "}";

 
    // Tiempo de espera en milisegundos (opcional)
    http.setTimeout(5000);  // 5 segundos
    
    // Realizamos la solicitud POST
    int httpResponseCode = http.POST(jsonBody);
    
    // Revisamos la respuesta
    if (httpResponseCode > 0) {
        // Si la respuesta es exitosa, mostramos el código de respuesta y el cuerpo del servidor
        String response = http.getString();
        Serial.print("Respuesta del servidor: ");
        Serial.println(httpResponseCode);
        Serial.print("Cuerpo de la respuesta: ");
        Serial.println(response);
    } else {
        // Si la solicitud falla, mostramos el código de error
        Serial.print("Error en la solicitud POST: ");
        Serial.println(httpResponseCode);
    }
    
    // Cerramos la conexión
    http.end();
}










