#include <PWM.h>

/* Takes arguments from the command line, adjusts the PWM
of LEDs connected to the pin number in the first value by the amount specified 
with the second value*/

int pin, value, function;

void setup(){
  Serial.begin(9600);
  InitTimersSafe();
//  pinMode(0, INPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH);
}

void loop(){
}

void serialEvent(){
//  Serial.print("Format your input '<function> <pin number> <value>'\n where function 1 = set brightness, function 2 = set pwm value");
  if (Serial.available()){
    
    function = Serial.parseInt();
    pin = Serial.parseInt();
    value = Serial.parseInt();

  }
  
  if (pin == 4 || pin == 13){

    Serial.print(pin);
    Serial.print(" ");
    Serial.print(value);
    if(pin == 4) pin = 9;
    if(pin == 13) pin = 3;
    if(function == 1 && value < 255){
      analogWrite(pin, value);
    }
    else if(function == 2){
      SetPinFrequency(pin,value);
    }
  }
    
}

// 2014 Andrew Harris
