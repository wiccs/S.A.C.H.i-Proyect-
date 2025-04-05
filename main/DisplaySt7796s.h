#include <TFT_eSPI.h> // Incluye la biblioteca TFT
#include <Arduino.h>  // Permite usar funciones de Arduino

class DisplaySt7796s{
private:
    TFT_eSPI tft;  // Crea el objeto TFT
  

public:
    DisplaySt7796s(); //Mi constructor

    void encender(); //Metodo para prender la pantalla

};