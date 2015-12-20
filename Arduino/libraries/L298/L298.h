/*
  L298.h - motor driver with L298 - Version 0.1
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
#ifndef L298_H_
#define L298_H_

#include <Arduino.h>
#include <inttypes.h>

class L298_Motor
{
  public:
    L298_Motor(uint8_t IN1, uint8_t IN2, uint8_t EN);

    void speed(uint8_t speed);
    void direction(bool direction);
    void brake();
    void stop();
  private:
    bool dir;
    uint8_t in1, in2, en;
};

class L298_Switch
{
  public:
    L298_Switch(uint8_t IN, bool rev);
    void on();
    void off();
    
  private:
    uint8_t pin;
    bool revers, value;
    void setValue(bool on);
};

#endif


