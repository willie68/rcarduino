//#define debug
#include <debug.h>
#include <makros.h>
#include <RCArduinoESP8266.h>
#include <RCArduinoReceiver.h>

/*
  TestESP8266.ino, Die Signale werden von RC Arduino gelesen.
*/

// Definition einiger Schranken für die RC Erkennung
// Obere und untere Schranke der Nullpunkterkennung
// Werte höher als TOP und tiefer als BOTTOM werden als nicht Null erkannt

// ------------------------------------------------------------
// Ab hier kommen Definition für die Software selber
// Hier nur etwas ändern, wenn man sicher ist, was man tut...
// ------------------------------------------------------------
RCArduinoESP8266 msgProxy;
RCArduinoReceiver receiver;

void setup() {
  Serial.begin(115200);
  Serial.println("Test RCARduino with ESP8266");

  msgProxy.begin();
}

byte count = 0;
void loop() {
  // RC Arduino erkennung
  //poll();
  msgProxy.poll();
  if (msgProxy.hasMessage()) {
    byte msg[32];
    msgProxy.getMessage(msg);
    receiver.parseMessage(msg);
    for (byte i = 0; i < 32; i++) {
      Serial.print(msg[i], HEX);
    }    
    Serial.println();

    for (byte i = 1; i < 5; i++) {
      if (i != 1) {
        Serial.print(", ");
      }
      Serial.print(i);
      Serial.print(":");
      Serial.print(receiver.getAnalogChannel(i), HEX);
    }
    Serial.println();

    for (byte i = 1; i < 65; i++) {
      if (i != 1) {
        Serial.print(", ");
      }
      Serial.print(i);
      Serial.print(":");
      Serial.print(receiver.getDigitalChannel(i));
    }
    Serial.println();
  }
}

