#include "HardwareSerial.h"
#include "Finger503.h"
#include <Adafruit_Fingerprint.h>


#define RX_PIN 16  // GPIO16 del ESP32 para RX (conectado a TX del sensor)
#define TX_PIN 17  // GPIO17 del ESP32 para TX (conectado a RX del sensor)

// Constructor de la clase Finger503
Finger503::Finger503() : mySerial(2), finger(&mySerial) {
  // Inicialización de las variables miembro de la clase
}

// Método para iniciar el sensor
void Finger503::iniciar() {
  mySerial.begin(57600, SERIAL_8N1, RX_PIN, TX_PIN);
  finger.begin(57600);
  Serial.begin(115200);
  Serial.println("Buscando el sensor...");
  
  if (finger.verifyPassword()) {
    Serial.println("¡Sensor detectado! 🎉");
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

    // Mostrar número de huellas almacenadas después del registro
    finger.LEDcontrol(FINGERPRINT_LED_OFF, 250, FINGERPRINT_LED_BLUE);
    Serial.print("Número de huellas almacenadas después de registrar: ");
    Serial.println(finger.templateCount);

  } else {
    Serial.println("Falló el registro de huella.");
  }

  delay(5000);  // Espera antes de registrar otra huella
}

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
  Serial.print("Con confianza de: "); Serial.println(finger.confidence);

  return finger.fingerID;
}



// Métodos auxiliares para huellas
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
