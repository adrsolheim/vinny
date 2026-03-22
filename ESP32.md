ESP32 <-- Server

## Server

1. On batch update generate fixed size image from a template. Manipulate frame buffer if LCD support it
2. Transfer image to ESP32

## ESP32

Display batch information for tap to LCD (image format)
  - name
  - description e.g, type=pale ale
  - status e.g, connected/empty
Support commands to
  - Turn on/off screen
  - Update new image (serial/wifi)
