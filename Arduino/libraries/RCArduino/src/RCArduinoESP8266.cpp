/*
  RCArduinoESP.cpp - RC Arduino Empfängers, ESP 8266 Implementierung - Version 0.1
  Copyright (c) 2015 Wilfried Klaas.  All right reserved.

  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
*/
#include <Arduino.h>
#include <inttypes.h>
#include <AltSoftSerial.h>
#include "RCArduinoESP8266.h"

RCArduinoESP8266::RCArduinoESP8266() {
  initArrays();
  initSerial();
}

void RCArduinoESP8266::initArrays() {
  for (byte i = 0; i < 32; i++) {
    message[i] =0;
  }
  pos = 0;
}

void RCArduinoESP8266::initSerial() {
  mySerial.begin(115200);
}

/**
    Abfrage eines analogen Kanals. Die Kanalnummer geht von 1..16
*/
void RCArduinoESP8266::poll() {
  if (pos == 32) {
    pos = 0;
  }
  if (mySerial.available()) {
    byte value = mySerial.read();
    if (pos == 0) {
      if (value == 0xdf) {
        message[pos] = value;
        pos++;
      }
    } else {
      message[pos] = value;
      pos++;
    }
  }
}

/**
   Abfragen eines digitalen Kanals. Die Kanalnummer geht von 1..1024
*/
bool RCArduinoESP8266::hasMessage() {
  return  pos == 32;
}

/**
   Hier kann man ein Byte array übergeben. Dieses wird dann untersucht, ob dort ein
   RCARduino Datagram enthalten ist. Wenn ja, wird der Inhalt übernommen.
   Rückgabewert ist true, wenn das Datagramm korrekt gelesen und verarbeitet werden konnte,
   sonst false.
*/
void RCArduinoESP8266::getMessage(byte msg[]) {
  for(byte i = 0; i < 32; i++) {
    msg[i] = message[i];
  }
}

