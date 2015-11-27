#include "RCArduinoESP8266.h"

RCArduinoESP8266 rcarduino;

byte message1[32] = {0xDF, 0x81,
                     0x0, 0x11,
                     0x4, 0x0,
                     0x4, 0x0,
                     0x4, 0x0,
                     0x4, 0x0,
                     0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0xAA, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xDF, 0x90
                    };

void setup() {
  Serial.begin(115200);
}

void loop() {
  bool test = rcarduino.parseMessage(message1);
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
  delay(10000);
}
