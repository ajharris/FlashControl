#include <PWM.h>

/* Takes arguments from the command line, adjusts the PWM
of LEDs connected to the pin number in the first value by the amount specified 
with the second value*/

int pin, value, function;
int array[3];


void setup(){
  Serial.begin(9600);
  Serial1.begin(9600);
  InitTimersSafe();
  for(int i = 2; i<14; i++){
    pinMode(i, OUTPUT);
  }

  Serial.print("Format your input '<function> <pin number> <value>'\n");
  Serial.print("where function 1 = set brightness, function 2 = set pwm value\n");
//  pinMode(1, OUTPUT);
}

void loop(){
}

void serialEvent(){
  Serial.print("Format your input '<function> <pin number> <value>'\n");
  Serial.print("where function 1 = set brightness, function 2 = set pwm value\n");
  if (Serial.available()){

    function = Serial.parseInt();
    pin = Serial.parseInt();
    value = Serial.parseInt();
  }
  

  if (pin != 4 && pin != 13){

    Serial.print("Pin: ");
    Serial.print(pin);
    Serial.print(", Value: ");
    Serial.println(value);
    if(function == 1 && value < 255){
        analogWrite(pin, value);
    }
    else if(function == 2){
        SetPinFrequency(pin,value);
    }
  }else{
    Serial1.print(function);
    Serial1.print(" ");
    Serial1.print(pin);
    Serial1.print(" ");
    Serial1.print(value);
    Serial1.print(" ");
//    delay(100);

    Serial.print("Pin: ");
    Serial.print(Serial1.parseInt());
    Serial.print(", Value: ");
    Serial.println(Serial1.parseInt());
  }
 
}

// 2014 Andrew Harris
