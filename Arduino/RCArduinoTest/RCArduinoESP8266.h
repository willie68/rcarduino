/*
  RCArduinoESP8266.h - RC Arduino Empfängers - Version 0.1
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
#ifndef RCArduinoESP8266_H_
#define RCArduinoESP8266_H_

#include <Arduino.h>
#include <inttypes.h>

#define MAX_ANALOG_CHANNELS 16
#define MAX_DIGITAL_CHANNELS 1024
#define MAX_ANALOG_VALUE 4096
#define NULL_ANALOG_VALUE 2048
#define MIN_ANALOG_VALUE 0
#define RCARDUINO_MESSAGE_LENGTH 32

class RCArduinoESP8266 
{
  public:
      RCArduinoESP8266();

      int getAnalogChannel(int channel);
      bool getDigitalChannel(int channel);
      bool parseMessage(byte message[]);
  private:
	  int analogChannelCount;
	  int digitalChannelCount;
	  uint16_t analogChannels[16];
	  byte digitalChannels[128];
	  
	  void initArrays();
    bool testCRC16(byte message[]);

    bool processPrioMessage(byte message[]);
    
    bool processPrio1Message(byte message[]);
};

#endif
