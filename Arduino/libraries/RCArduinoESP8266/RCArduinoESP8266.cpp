/*
  RCArduinoESP8266.cpp - RC Arduino Empfängers - Version 0.1
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
#include <RCArduinoESP8266.h>

RCArduinoESP8266::RCArduinoESP8266() {
    initArrays();
}

void RCArduinoESP8266::initArrays() {
  for(byte i= 0; i < 16;i++) {
    analogChannels[i] = NULL_ANALOG_VALUE;
  }
  for(int i= 0; i < 128;i++) {
    digitalChannels[i] = 0;
  }
}


int RCArduinoESP8266::getAnalogChannel(int channel) {
}
	  
bool RCArduinoESP8266::getDigitalChannel(int channel) {
    int bitPos = (i-1) % 8;
    int bytePos =  (i-1) / 8;
	byte value = datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos];
	if (on) {
		value = (byte) (value | (byte) (1 << bitPos));
    } else {
		value = (byte) (value & ~(1 << bitPos));
	}
	datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos] = value;
}
	  
bool RCArduinoESP8266::parseMessage(byte message[]) {
}
/*
    public void setHostname(String hostname) {
        connectionHandler.setHostname(hostname);

        Message message = Message.obtain(connectionHandler, MessageCode.CLASS_CONNECTION, MessageCode.CONNECTION_RECONNECT, 0);
        connectionHandler.sendMessage(message);
    }

    private void initDatagram() {
        // RCArduino Message identifier
        datagram[0] = (byte) 0xdf;
        datagram[1] = (byte) 0x81;
        // RCArduino Priority 1 datagram
        datagram[2] = (byte) 0x00;
        datagram[3] = (byte) 0x11;

        for (int i = 4; i < datagram.length; i++) {
            datagram[i] = 0;
        }
    }

    public void switchOn(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " on");

        setSwitch(switchNumber, true);
    }

    public void switchOff(int switchNumber) {
        Log.i(LOG_TAG, "switch " + switchNumber + " off");

        setSwitch(switchNumber, false);
    }

    private void setSwitch(int switchNumber, boolean on) {
        int bitPos = (switchNumber-1) % 8;
        int bytePos =  (switchNumber-1) / 8;
        if ((bytePos >= 0) && (bytePos <= 7)){
            synchronized (datagram) {
                byte value = datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos];
                if (on) {
                    value = (byte) (value | (byte) (1 << bitPos));
                } else {
                    value = (byte) (value & ~(1 << bitPos));
                }
                datagram[MESSAGE_INDEX_DIGITAL_1 + bytePos] = value;
                transmitMessage();
            }
        }
    }

    public void setChannel(int channelNumber, int value) {
            if (channelNumber >= 1 && channelNumber <= 4) {
                synchronized (datagram) {
                    if (value > MAX_CHANNEL_VALUE) {
                        value = MAX_CHANNEL_VALUE;
                    }
                    if (value < MIN_CHANNEL_VALUE) {
                        value = MIN_CHANNEL_VALUE;
                    }
                    int index = (channelNumber - 1) * 2 + MESSAGE_INDEX_ANALOG_1;
                    byte highByte = (byte) ((value & 0xFF00) >> 8);
                    byte lowByte = (byte) (value & 0x00FF);
                    datagram[index] = highByte;
                    datagram[index + 1] = lowByte;
                    long actualTime = System.currentTimeMillis();
                    if (actualTime - lastAnalogTransmit > 100) {
                        transmitMessage();
                        lastAnalogTransmit = actualTime;
                    }
                    Log.i(LOG_TAG, "channel " + channelNumber + " " + value);
                }
            }
    }

    private void transmitMessage() {
            injectCRC16();

            Message msg = Message.obtain(connectionHandler, MessageCode.SEND_MESSAGE, datagram);
            connectionHandler.sendMessage(msg);
    }

    private void injectCRC16() {
        byte highCrcByte = 0;
        byte lowCrcByte = 0;
        for (int i = 0; i < 15; i++) {
            highCrcByte = (byte) (highCrcByte ^ datagram[i*2]);
            lowCrcByte = (byte) (lowCrcByte ^ datagram[(i*2)+1]);
        }
        datagram[30] = highCrcByte;
        datagram[31] = lowCrcByte;
    };

*/