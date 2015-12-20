//#define debug
#include <debug.h>
#include <makros.h>
#include <L298.h>

#define MOTOR_IN1 2
#define MOTOR_IN2 4
#define MOTOR_ENA 3

#define SWITCH 14

// ------------------------------------------------------------
// Ab hier kommen Definition für die Software selber
// Hier nur etwas ändern, wenn man sicher ist, was man tut...
// ------------------------------------------------------------
L298_Motor Motor(MOTOR_IN1, MOTOR_IN2, MOTOR_ENA);
L298_Switch Switch(SWITCH, true);

void setup() {
  Serial.begin(115200);
  Serial.println("Kran V 1.0");
}

byte count = 0;
void loop() {
  // RC Arduino erkennung
  outputMotor();
  outputSwitch();
  count++;
  delay(20);
}

byte oldMotor = 0;
void outputMotor() {
  byte pwm = count;
  if (oldMotor != pwm) {
    if (between(pwm, 120, 136)) {
      Motor.brake();
    } else {
      if (pwm > 128) {
        Motor.direction(true);
        pwm = pwm - 128;
      } else {
        Motor.direction(false);
      }
      pwm = pwm * 2;
      Motor.speed(pwm);
    }
    oldMotor = pwm;
  }
}

void outputSwitch() {
  bool value = count > 128;
  Serial.print(' ');
  Serial.println(value);
  if (value) {
    Switch.on();
  } else {
    Switch.off();
  }
}

