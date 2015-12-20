/*
  RCArduinoESP.h - RC Arduino Empfängers, ESP 8266 Implementierung - Version 0.1
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
#ifndef RCARDUINOESP8266_H_
#define RCARDUINOESP8266_H_

#include <Arduino.h>
#include <inttypes.h>
#include <AltSoftSerial.h>

class RCArduinoESP8266
{
  public:
      RCArduinoESP8266();

	  void begin();
      void poll();
      bool hasMessage();
      void getMessage(byte msg[]);
  private:
    byte message[32];
    byte pos;
	bool inMessage;

    AltSoftSerial mySerial;
    
    void initArrays();
};

#endif

