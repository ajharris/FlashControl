int freq, count;
int array[2];

void setup(){
  pinMode(13, OUTPUT);
  digitalWrite(13, HIGH);
  Serial.begin(9600);
  freq = 375;
  count = 0;
}

void loop(){
  serialEvent();
}

void serialEvent(){
//  if(Serial.available() > 0){
    while(freq<1100){
       int array[] = {freq, count};
       Serial.println(array[0] + " " + array[1]);
      freq += 10;
    }
    freq = 375;
    delay(1000);
    digitalWrite(13, LOW);
    delay(1000);
    digitalWrite(13, HIGH);
//  }
}
