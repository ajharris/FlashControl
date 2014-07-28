#include <PWM.h>

/* Takes arguments from the command line, adjusts the PWM
of LEDs connected to the pin number in the first value by the amount specified 
with the second value*/

int pin, value, function;
char array[3];

void setup(){
  Serial.begin(9600);
  InitTimersSafe();
//  pinMode(0, INPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(13, OUTPUT);

}

void loop(){
    digitalWrite(13, HIGH);
}

void serialEvent(){

  if(Serial.available() > 0){

   
    
    Serial.readBytes(array, 3);
    function = array[0];
    pin = array[1];
    
    //Conditional to correct for bit length difference between Java and Arduino integers
    if (array[2]<0) value = 281 + array[2]-25;
    else value = array[2]-25;
    digitalWrite(13, LOW);
    
    if (pin == 4 || pin == 13){

      if(pin == 4) pin = 9;
      if(pin == 13) pin = 10;
      if(function == 1 && value < 255){
        analogWrite(pin, value);
      }
      else if(function == 2){
        SetPinFrequency(pin,value);
      }
    }  
  }
}

// 2014 Andrew Harris, Ryerson University (Koivisto Lab)
