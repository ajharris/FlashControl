#include <PWM.h>

/* Takes arguments from the command line, adjusts the PWM
of LEDs connected to the pin number in the first value by the amount specified 
with the second value*/

int pin, value, function;
char array[3];


void setup(){
  Serial.begin(9600);
  Serial1.begin(9600);
  InitTimersSafe();
  for(int i = 2; i<14; i++){
    pinMode(i, OUTPUT);
  }


}

void loop(){
  digitalWrite(13, HIGH);
}

void serialEvent(){

  if (Serial.available()){


    Serial.readBytes(array, 3);
    function = array[0];
    pin = array[1];
    //Conditional to correct for bit length difference between Java and Arduino integers
    if (array[2]<0) value = 281 + array[2]-25;
    else value = array[2]-25;
  }
  

  if (pin != 4 && pin != 13){
    Serial1.print(function);
    Serial1.print(" ");
    Serial1.print(pin);
    Serial1.print(" ");
    Serial1.print(value);

    if(function == 1 && value < 255){
      analogWrite(pin, value);
    }
    else if(function == 2){
      SetPinFrequency(pin,value);
    }
    
  }else{

    Serial1.write(array);
    
  }


  
}

// 2014 Andrew Harris, Ryerson University (Koivisto Lab)
