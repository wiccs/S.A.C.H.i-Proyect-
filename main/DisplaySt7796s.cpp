#include <DisplaySt7796s.h>
#include <Arduino.h>

//Constructor:
DisplaySt7796s::DisplaySt7796s():tft(){ //Es una buena practica inicializar el constructor antes de usarlo

}

void DisplaySt7796s::encender(){
  tft.init();  // Inicializa la pantalla
  tft.setRotation(3);  // Configura la rotación de la pantalla
  tft.fillScreen(TFT_BLACK);  // Rellena la pantalla de negro
  tft.setTextColor(TFT_WHITE);  // Establece el color de texto
  tft.setTextSize(2);  // Tamaño del texto
  tft.setCursor(0, 0);  // Posición inicial del cursor
  tft.println("Hola Mundo! desde un display st7796s"); 
}