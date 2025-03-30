#define FINGER503_H

#include <Arduino.h>  // Permite usar funciones de Arduino
#include <Adafruit_Fingerprint.h> // Libreria del sensor R503
#include <HardwareSerial.h> //Libreria que permite la comunicacion UART.

class Finger503 {

//En c++ debemos declarar todo lo que vamos a usar en .cpp (funciones, metodos,objetos,constructores...)
 private:
    HardwareSerial mySerial;  //Declaramos este objeto para poder implementarlo en .cpp
    Adafruit_Fingerprint finger; //Lo mismo 
    int registerFingerprint(int id); //Una funcion auxiliar.
    int getFingerprintIDez();//Otra funcion Auxiliar
    int buscarIDLibre();//Otra funcion auxiliar.

public:
    // Constructor
    Finger503();

    // Métodos
    void iniciar();  // Inicializa el sensor.
    void registrar();  // Verifica una huella.
    uint8_t autenticar();  // Verifica una huella.
    bool contar(); // Mostrar numero de huellas registradas.
    bool eliminar(); // Eliminar el registro.
    
};