
#include <PWM.h>

#include <LiquidCrystal.h>

// initialize the library with the numbers of the interface pins
LiquidCrystal lcd(53, 51, 46, 48, 50, 52);
int upButton = 49, upButtonState;
int dnButton = 47, dnButtonState;
int incButton = 45, incButtonState;
int decButton = 43, decButtonState;
int inMenu = 41, inButtonState;
int outMenu = 39, outButtonState;
int LEDNumber = 0;
int LEDMatrix[2][12] =  {
  {
    425, 455, 470, 525, 595, 625, 660, 730, 740, 850, 940  }
  , {
    0,0,0,0,0,0,0,0,0,0,0,0  }
};
char* menuItem[] = {
  "Presets", "Individual", "Global", "Vegas", "Serial", "PWM Frequency"};
int menuNumber = 0;
int count = 0;
int presetNumber = 0;

int pin = 0, value = 0;

//Joystick setup
const byte PIN_BUTTON_SELECT = 34;
const byte PIN_BUTTON_RIGHT = 22;
const byte PIN_BUTTON_UP = 24;
const byte PIN_BUTTON_DOWN = 26;
const byte PIN_BUTTON_LEFT = 28; 

const byte PIN_ANALOG_X = 0;
const byte PIN_ANALOG_Y = 1;

const int X_THRESHOLD_LOW = 505;
const int X_THRESHOLD_HIGH = 515;    

const int Y_THRESHOLD_LOW = 500;
const int Y_THRESHOLD_HIGH = 510;    

int x_position;
int y_position;

int x_direction;
int y_direction;

int mute;


void setup(){
  outMenu = PIN_BUTTON_UP;
  inMenu = PIN_BUTTON_DOWN;
  upButton = PIN_BUTTON_RIGHT;
  dnButton = PIN_BUTTON_LEFT;
  mute = PIN_BUTTON_SELECT;



  Serial.begin(9600);
  //Set pin mode for Mega controller and initialize
  for(int i = 2; i<14; i++){
    pinMode(i, OUTPUT);
    analogWrite(i, value);
  }
  pinMode(upButton, INPUT);
  lcd.begin(16, 2);
//  // Print a message to the LCD.
//  lcd.print("This is not");
//  lcd.setCursor(0,1);
//  lcd.print("a bomb");
//  delay(2500);
//  lcd.clear();
//  lcd.setCursor(0,0);
//  lcd.print("but don't cut");
//  lcd.setCursor(0,1);
//  lcd.print("the red wire");
//  delay(2500);

  //Joystick init

  pinMode(PIN_BUTTON_RIGHT, INPUT);
  digitalWrite(PIN_BUTTON_RIGHT, HIGH);
  pinMode(PIN_BUTTON_UP, INPUT);
  digitalWrite(PIN_BUTTON_UP, HIGH);
  pinMode(PIN_BUTTON_DOWN, INPUT);
  digitalWrite(PIN_BUTTON_DOWN, HIGH);
  pinMode(PIN_BUTTON_LEFT, INPUT);
  digitalWrite(PIN_BUTTON_LEFT, HIGH);
  pinMode(PIN_BUTTON_SELECT, INPUT);
  digitalWrite(PIN_BUTTON_SELECT, HIGH);
}

void loop(){

  menu();
  //  individualLED();

}

void serialEvent(){

  if(Serial.available() > 0){
    char array[2];
    Serial.readBytes(array, 2);
    pin = array[0];
    //Conditional to correct for bit length difference between Java and Arduino integers
    if (array[1]<0) value = 281 + array[1]-25;
    else value = array[1]-25;

        lcd.setCursor(4,0);
        lcd.print("Serial");
        analogWrite(pin, value);
        LEDMatrix[1][pin-2]++;
        lcd.setCursor(0, 1);
        lcd.print("               ");
        lcd.setCursor(0,1);
        lcd.print(pin);
        lcd.setCursor(6,1);
        lcd.print(value);

    }
}

void menu(){

  lcd.clear();
  lcd.setCursor(0,0);
  lcd.print("[     Menu     ]");
  lcd.setCursor(0,1);
  lcd.print(menuItem[menuNumber]);


  if(digitalRead(upButton) == LOW || analogRead(PIN_ANALOG_X) > X_THRESHOLD_HIGH){
    if (menuNumber < 5) menuNumber++;
    else menuNumber = 0;
    delay(300);
  }
  if(digitalRead(dnButton) == LOW || analogRead(PIN_ANALOG_X) < X_THRESHOLD_LOW){
    if (menuNumber > 0) menuNumber--;
    else menuNumber = 5;
    delay(300);
  }
  if(digitalRead(inMenu) == LOW){
    switch (menuNumber){
    case 0:
      lcd.clear();
      while(digitalRead(outMenu) != LOW){
        presets();
        if(digitalRead(mute) == LOW) break;
      }
      break;

    case 1:
      lcd.clear();
      while(digitalRead(outMenu) != LOW){
        individualLED();
        if(digitalRead(mute) == LOW) break;          
      }
      break;

    case 2:
      lcd.clear();
      while(digitalRead(outMenu) != LOW){
        global();
        if(digitalRead(mute) == LOW) break;
      }
      break;

    case 3:
      lcd.clear();
      while(digitalRead(outMenu) != LOW){
        vegas();
        if(digitalRead(mute) == LOW) break;
      }
      global(0);
      break;
    case 4:
      lcd.clear();
      while(digitalRead(outMenu) != LOW){
        lcd.setCursor(4, 0);
        lcd.print("Serial");
        serialEvent();
      }
      break;
    case 5:
      lcd.clear();
      InitTimersSafe();
      while(digitalRead(outMenu) != LOW){
        pwmSet();
      }
      break;
    }

  }  
  delay(100);
}

void individualLED(){
  lcd.setCursor(0,0);
  lcd.print("LED Wavelength:  ");
  lcd.setCursor(0,1);
  lcd.print(LEDMatrix[0][LEDNumber]);
  incButtonState = LOW;
  decButtonState = LOW;

  x_direction = 0;
  y_direction = 0;

  x_position = analogRead(PIN_ANALOG_X);
  y_position = analogRead(PIN_ANALOG_Y);

  if (y_position > Y_THRESHOLD_HIGH) {
    incButtonState = HIGH;
  } 
  else if (y_position < Y_THRESHOLD_LOW) {
    decButtonState = HIGH;
  }

  //Buttons will cycle through LED elements in array

  upButtonState = digitalRead(upButton);
  if(upButtonState == LOW || x_position > X_THRESHOLD_HIGH){
    if (LEDNumber < 10) LEDNumber++;
    if(upButtonState == LOW || x_position > X_THRESHOLD_HIGH) delay(300);
  }
  dnButtonState = digitalRead(dnButton);
  if(dnButtonState == LOW || x_position < X_THRESHOLD_LOW){
    if(LEDNumber > 0)LEDNumber--;
    if(dnButtonState == LOW || x_position < X_THRESHOLD_LOW) delay(300);
  }

  lcd.setCursor(4, 1);
  lcd.print("Pwr: ");
  lcd.print(((float)LEDMatrix[1][LEDNumber]/255*100));
  lcd.print("%");
  analogWrite(LEDNumber+2, LEDMatrix[1][LEDNumber]);

  //Buttons will increment and decrement PWM values for output pins

  if(incButtonState == HIGH && LEDMatrix[1][LEDNumber]<255){
    LEDMatrix[1][LEDNumber]++;
    if(LEDMatrix[1][LEDNumber]>255) LEDMatrix[1][LEDNumber] = 255;
    delay(150);
  }
  if(decButtonState == HIGH && LEDMatrix[1][LEDNumber]>0){
    LEDMatrix[1][LEDNumber]--;
    if(LEDMatrix[1][LEDNumber]<0) LEDMatrix[1][LEDNumber] = 0;
    delay(150);
  }
  if(digitalRead(mute) == LOW) zero();
}
//Sets of preset values for the LED PWM controllers
void presets(){

  String preset[8] = {
    "Off", "50%", "75%", "100%", "White", "Red", "Blue", "Green"  };

  lcd.setCursor(0,0);
  lcd.print("[    Preset    ]");


  switch(presetNumber){
  case 0:
    for(int i = 2; i<14; i++) analogWrite(i, 0);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 1:
    for(int i = 2; i<14; i++) analogWrite(i, 255*0.5);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 2:
    for(int i = 2; i<14; i++) analogWrite(i, 255*0.75);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 3:
    for(int i = 2; i<14; i++) analogWrite(i, 255);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 4:
    analogWrite(0, 255);
    analogWrite(1, 255);
    analogWrite(2, 255);
    analogWrite(3, 255);
    analogWrite(4, 255);
    analogWrite(5, 255);
    analogWrite(6, 255);
    analogWrite(7, 255);
    analogWrite(8, 255);
    analogWrite(9, 255);
    analogWrite(10, 255);
    analogWrite(11, 255);
    analogWrite(12, 255);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 5:
    analogWrite(0, 255);
    analogWrite(1, 255);
    analogWrite(2, 255);
    analogWrite(3, 255);
    analogWrite(4, 255);
    analogWrite(5, 255);
    analogWrite(6, 255);
    analogWrite(7, 255);
    analogWrite(8, 255);
    analogWrite(9, 255);
    analogWrite(10, 255);
    analogWrite(11, 255);
    analogWrite(12, 255);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 6:
    analogWrite(0, 255);
    analogWrite(1, 255);
    analogWrite(2, 255);
    analogWrite(3, 255);
    analogWrite(4, 255);
    analogWrite(5, 255);
    analogWrite(6, 255);
    analogWrite(7, 255);
    analogWrite(8, 255);
    analogWrite(9, 255);
    analogWrite(10, 255);
    analogWrite(11, 255);
    analogWrite(12, 255);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;
  case 7:
    analogWrite(0, 255);
    analogWrite(1, 255);
    analogWrite(2, 255);
    analogWrite(3, 255);
    analogWrite(4, 255);
    analogWrite(5, 255);
    analogWrite(6, 255);
    analogWrite(7, 255);
    analogWrite(8, 255);
    analogWrite(9, 255);
    analogWrite(10, 255);
    analogWrite(11, 255);
    analogWrite(12, 255);
    lcd.setCursor(5,1);
    lcd.print(preset[presetNumber]);
    break;

  }

  Serial.print(digitalRead(upButton));
  Serial.print("  ");
  Serial.println(digitalRead(dnButton));

  if(digitalRead(upButton) == LOW || analogRead(x_position) > X_THRESHOLD_HIGH){ 
    if (presetNumber < 7) presetNumber++;
    Serial.println(presetNumber);
    delay(300);
    lcd.clear();
  }

  if(digitalRead(dnButton) == LOW || analogRead(x_position) < X_THRESHOLD_LOW){ 
    if (presetNumber > 0) presetNumber--;
    Serial.println(presetNumber);
    delay(300);
    lcd.clear();
  }

  if(digitalRead(mute) == LOW) zero();



}

//Adjust all LED values at once
void global(int start){
  for(int i = 2; i<14; i++){
    analogWrite(i,start);
  }
}

int globalIntensity = value;

void global(){


  x_direction = 0;
  y_direction = 0;

  x_position = analogRead(PIN_ANALOG_X);
  y_position = analogRead(PIN_ANALOG_Y);
  if(y_position > Y_THRESHOLD_HIGH){
    if(globalIntensity<255) globalIntensity +=(int)(10*(float)y_position/1023);
    if(globalIntensity> 255)globalIntensity = 255;
    delay(300);
    lcd.clear();
  }
  if(y_position< Y_THRESHOLD_LOW){
    if(globalIntensity>0)globalIntensity -=(int)(10*(1-(float)y_position/1023));
    if(globalIntensity< 0)globalIntensity = 0;
    delay(300);
    lcd.clear();
  }
  lcd.setCursor(0,0);
  lcd.print("[    Global    ]");
  for(int i = 2; i<14; i++){
    analogWrite(i, globalIntensity);
    LEDMatrix[1][i-2] = globalIntensity;
  }
  lcd.setCursor(6,1);
  lcd.print(((float)globalIntensity/255)*100);
  lcd.print("%");

  if(digitalRead(mute) == LOW) zero();


}

//Crazy random blinking
void vegas(){
  int crazyPin = random(2,14);
  if (count % 4 == 0) lcd.print("      VEGAS MODE!!!!!");
  for(int i = 2; i<14; i++){
    analogWrite(i,0);
  }
  if(digitalRead(mute) == LOW) zero();
  analogWrite(crazyPin, 255);
  delay(20);
  count++;

}

void zero(){
  for(int i = 2; i<14; i++){
    analogWrite(i,0);
    LEDMatrix[1][i-2] = 0;
    Serial.println(LEDMatrix[1][i-2]);
  }
  Serial.println("EMERGENCY");
  globalIntensity = 0;
  presetNumber = 0;
}
int freq = 2;
void pwmSet(){
  
  if(analogRead(PIN_ANALOG_Y) > Y_THRESHOLD_HIGH && freq<30){
    freq++;

  }
  else if(analogRead(PIN_ANALOG_Y) < Y_THRESHOLD_LOW && freq>2){
    freq--;

  }
  for(int i = 2; i<14; i++){
    SetPinFrequency(i, freq);
  }
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("PWM Frequency");
  lcd.setCursor(0,1);
  lcd.print("Freq:  ");
  lcd.print(Timer1_GetFrequency());
  delay(100);
}


