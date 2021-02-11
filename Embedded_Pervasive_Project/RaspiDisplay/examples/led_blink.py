from gpiozero import LED
from time import sleep

ledPin = LED("GPIO4")

# Uses hardware: 1 red LED, 1 500 Ohm Resistor
# Connect from GPIO4 (Pin 7) to LED to Resistor to any GND (Example Pin 6)
while True:
    ledPin.on()
    print("On")
    sleep(0.5)
    ledPin.off()
    print("Off")
    sleep(0.5)