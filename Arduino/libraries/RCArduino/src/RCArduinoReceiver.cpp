/*
  RCArduinoReceiver.cpp - RC Arduino Empfängers - Version 0.1
  Copyright (c) 2012 Wilfried Klaas.  All right reserved.

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
#include "RCArduinoReceiver.h"

RCArduinoReceiver::RCArduinoReceiver() {
  initArrays();
}

void RCArduinoReceiver::initArrays() {
  for (byte i = 0; i < 16; i++) {
    analogChannels[i] = NULL_ANALOG_VALUE;
  }
  for (int i = 0; i < 128; i++) {
    digitalChannels[i] = 0;
  }
}

/**
    Abfrage eines analogen Kanals. Die Kanalnummer geht von 1..16
*/
int RCArduinoReceiver::getAnalogChannel(int channel) {
  return analogChannels[channel - 1];
}

/**
   Abfragen eines digitalen Kanals. Die Kanalnummer geht von 1..1024
*/
bool RCArduinoReceiver::getDigitalChannel(int channel) {
  int bitPos = (channel - 1) % 8;
  int bytePos =  (channel - 1) / 8;
  byte value = digitalChannels[bytePos];
  return	(value & (1 << bitPos)) > 0;
}

/**
   Hier kann man ein Byte array übergeben. Dieses wird dann untersucht, ob dort ein
   RCARduino Datagram enthalten ist. Wenn ja, wird der Inhalt übernommen.
   Rückgabewert ist true, wenn das Datagramm korrekt gelesen und verarbeitet werden konnte,
   sonst false.
*/
bool RCArduinoReceiver::parseMessage(byte message[]) {
  if (message[0] != 0xdf) {
    return false;
  }
  if (message[1] != 0x81) {
    return false;
  }
  if (testCRC16(message)) {
    if (message[2] == 0x00) {
      return processPrioMessage(message);
    }
  }
  return false;
}

bool RCArduinoReceiver::processPrioMessage(byte message[]) {
  if (message[3] == 0x11) {
    return processPrio1Message(message);
  }
  return false;
}

bool RCArduinoReceiver::processPrio1Message(byte message[]) {
  for (byte i = 0; i < 4; i++) {
	uint16_t hi = message[(i * 2) + 4];
	uint16_t lo = message[(i * 2) + 5];
    analogChannels[i] =  hi * 256 + lo;
  }
  for (byte i = 0; i < 8; i++) {
    digitalChannels[i] = message[i + 12];
  }
  return true;
}

bool RCArduinoReceiver::testCRC16(byte message[]) {
  byte highCrcByte = 0;
  byte lowCrcByte = 0;
  for (int i = 0; i < 15; i++) {
    highCrcByte = highCrcByte ^ message[i * 2];
    lowCrcByte = lowCrcByte ^ message[(i * 2) + 1];
  }
  if (message[30] != highCrcByte) {
    return false;
  }
  if (message[31] != lowCrcByte) {
    return false;
  }
  return true;
};

