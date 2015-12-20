/*
  L298.cpp - motor driver with L298 - Version 0.1
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
#include "L298.h"

L298_Motor::L298_Motor(uint8_t IN1, uint8_t IN2, uint8_t ENA) {
  in1 = IN1;
  in2 = IN2;
  en = ENA;
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(en, OUTPUT);
  digitalWrite(en, 0);
  digitalWrite(in1, 0);
  digitalWrite(in2, 0);
}

void L298_Motor::speed(uint8_t speed) {
  digitalWrite(en, 0);
  if (dir) {
    digitalWrite(in1, 0);
    digitalWrite(in2, 1);
  } else {
    digitalWrite(in1, 1);
    digitalWrite(in2, 0);
  }
  analogWrite(en, speed);
}

void L298_Motor::direction(bool direction) {
  dir = direction;
}

void L298_Motor::brake() {
  digitalWrite(in1, 0);
  digitalWrite(in2, 0);
  digitalWrite(en, 1);
}

void L298_Motor::stop() {
  digitalWrite(en, 0);
}

L298_Switch::L298_Switch(uint8_t IN, bool rev) {
  revers = rev;
  pin = IN;
  pinMode(pin, OUTPUT);
  off(); 
}

void L298_Switch::on() {
  setValue(true);
}

void L298_Switch::off() {
  setValue(false);
}

void L298_Switch::setValue(bool on) {
  digitalWrite(pin, on ^ revers);
}

