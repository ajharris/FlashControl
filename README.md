FlashControl
============

FlashControl is a serial controller that will adjust PWM and frequency values for pins 2 to 13 on the Arduino Mega 2560.  The Arduino sketch on the 2560 parses out the values, and if the pin being adjusted is pin 4 or 13 the values will be passed to Serial1, and interpreted by a second Arduino.  This was necessary to allow adjustment of all pins without adjusting the system timer.
