//******************************************************************************
// See tutorial: 
// UPDATE TUTORIAL
//******************************************************************************

#include <OpenDevice.h>
#include <Adafruit_NeoPixel.h>

#define TONE_PIN 3

//-- Optional ----
#define PIXEL_PIN   6  // Digital IO pin connected to the NeoPixels.
#define PIXEL_COUNT 8  // Number of NeoPixels
Adafruit_NeoPixel ledStrip(PIXEL_COUNT, PIXEL_PIN, NEO_GRB + NEO_KHZ800);
int pixelIndex = 0;
// --

void setup(){
  ODev.clear(); // if you had problems try to clear EEPROM settings.
  ODev.enableDebug();
  ODev.name("MyDeviceBoard");
  
  ODev.addDevice("Led", 13, Device::DIGITAL); 
  ODev.addCommand("PlayTone", cmdPlayTone);
  ODev.addCommand("ChangeVolume", cmdChangeVolume);

  ledStrip.begin(); 
  ledStrip.show();  // Initialize all pixels to 'off'

  ODev.begin(Serial, 115200);
}

void loop(){
  ODev.loop();
}

void cmdPlayTone(){
  int note =  ODev.readInt();
  ODev.debug("Note", tone);
  tone(TONE_PIN, note, 250);
}

void cmdChangeVolume(){
  int opt =  ODev.readInt();
  if(opt == 0) pixelIndex--;
  if(opt == 1) pixelIndex++;
  uint32_t color = ledStrip.Color(255,   0,   0);
  ledStrip.clear();
  for(int i = 0; i < pixelIndex; i++){
    ledStrip.setPixelColor(i, color);
  }
  ledStrip.show();
}
