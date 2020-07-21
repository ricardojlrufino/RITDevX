#include <OpenDevice.h>

int temperature = 0;
int humidity = 0;
int lumens = 0;

void setup() {
  //  ODev.clear(); // if you had problems try to clear EEPROM settings.
  ODev.name("MyBoard");

  ODev.addDevice("Led", 13, Device::DIGITAL);
  
  ODev.addSensor("Temperature", readTemperature)->readInterval = 200;
  ODev.addSensor("Humidity", readHumidity)->readInterval = 200;
  ODev.addSensor("Lumens", readLumens)->readInterval = 200;

  ODev.begin(Serial, 115200);
}

void loop() {
  ODev.loop();
}

//==================================================
// Listeners / Commands
//==================================================

value_t readTemperature() {
  // NOTE: Here you can substitute the code that actually reads any sensor.
  if(temperature == 100) temperature = 0;
  return temperature++;
}

value_t readHumidity() {
  if(humidity == 100) humidity = 0;
  return humidity++;
}

value_t readLumens() {
  if(lumens == 100) lumens = 0;
  return lumens++;
}
