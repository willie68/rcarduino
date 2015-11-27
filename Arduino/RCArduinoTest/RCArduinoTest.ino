#include <AltSoftSerial.h>
#include "RCArduinoReceiver.h"

/**
 * Dies ist ein Denmoprogramm für die RCArduino Bibliothek.
 * Hier werden Datagramme nach RCArduino Struktur über die 2. serielle 
 * Schnittstelle angenommen und verarbeitet. Die Daten können u.a. auch 
 * von der mit entsprechender FIrmware ausgestetteten Wifi Modul mit dem 
 * ESP8266 stammen. (Siehe Extraprojekt)
 * z.Z. werden von der Bibliothek nur Prio1Messages verarbeitet.
 * Belegt werden dafür folgende Pins.
 * 0,1 für die normale serielle Schnittstelle
 * 8,9 für die 2. serielle Schnittstelle 
 * (Pin 10 ist damit auch nicht mehr für die Erzeugung von PWM Signalen zu gebrauchen)
 * Somit 
 */

RCArduinoReceiver rcarduino;
AltSoftSerial mySerial;

byte message1[32] = {
  0xDF, 0x81,
  0x0, 0x11,
  0x4, 0x0,
  0x4, 0x0,
  0x4, 0x0,
  0x4, 0x0,
  0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xDF, 0x90
};
byte message2[32] =  {
  0xDF, 0x81,
  0x0, 0x11,
  0x4, 0x0,
  0x4, 0x0,
  0x4, 0x0,
  0x4, 0x0,
  0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xDF, 0x90
};

byte msg[32];
byte pos;

void setup() {
  Serial.begin(115200);
  mySerial.begin(115200);
  for (byte i = 0; i <= 32; i++) {
    msg[i] = 0;
  }
  pos = 0;
}


void loop() {
  if (mySerial.avaible()) {
    byte value = mySerial.read();
    if (pos == 0) {
      if (value == 0xdf) {
        msg[pos] = value;
        pos++;
      }
    } else {
      msg[pos] = value;
      pos++;
    }
  }
  if (pos == 32) {
    bool test = rcarduino.parseMessage(msg);
    Serial.print("First:");
    Serial.println(test);
    for (byte i = 1; i <= 4; i++) {
      int value = rcarduino.getAnalogChannel(i);
      Serial.print("a");
      Serial.print(i);
      Serial.print(":");
      Serial.println(value);
    }
    Serial.print("d");
    for (int i = 1; i <= 1024; i++) {
      bool value = rcarduino.getDigitalChannel(i);
      Serial.print(value);
      if ((i % 128) == 0) {
        Serial.println();
      }
    }
    Serial.println();
    pos = 0;
  }
}
