import paho.mqtt.publish as publish
import random
import time
import sys

# Setup all needed information for MQTT
MQTT_SERVER = "localhost"
MQTT_PATH_WATER = "water"
MQTT_PATH_TEMP = "temp"
waitTime = 60

if len(sys.argv) == 2:
    try:
        waitTime = int(sys.argv[1])
    except ValueError:
        print ("Error! Using default 60 seconds delay")            

#Generates random values
while True:
    publish.single(MQTT_PATH_TEMP, random.randrange(15, 40, 1), hostname=MQTT_SERVER)
    publish.single(MQTT_PATH_WATER, bool(random.getrandbits(1)), hostname=MQTT_SERVER)
    time.sleep(waitTime)