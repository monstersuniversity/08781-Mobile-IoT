import RPi.GPIO as GPIO
import time
import sys
from hx711 import HX711
import requests
import random

def cleanAndExit():
    print "Cleaning..."
    GPIO.cleanup()
    print "Bye!"
    sys.exit()

hx = HX711(5, 6)

# I've found out that, for some reason, the order of the bytes is not always the same between versions of python, numpy and the hx711 itself.
# Still need to figure out why does it change.
# If you're experiencing super random values, change these values to MSB or LSB until to get more stable values.
# There is some code below to debug and log the order of the bits and the bytes.
# The first parameter is the order in which the bytes are used to build the "long" value.
# The second paramter is the order of the bits inside each byte.

# In this case, 92 is 1 gram because, with 1 as a reference unit I got numbers near 0 without any weight
# and I got numbers around 184000 when I added 2kg. So, according to the rule of thirds:
# If 2000 grams is 184000 then 1000 grams is 184000 / 2000 = 92.
hx.set_reference_unit(30)
#hx.set_reference_unit(92)

result = hx.reset()
print result;
hx.tare()


ENDPOINT = "http://ec2-52-87-198-206.compute-1.amazonaws.com:3000/add"

preVal = 0

while True:
    try:
        # These three lines are usefull to debug wether to use MSB or LSB in the reading formats
        # for the first parameter of "hx.set_reading_format("LSB", "MSB")".
        # Comment the two lines "val = hx.get_weight(5)" and "print val" and uncomment the three lines to see what it prints.
        # np_arr8_string = hx.get_np_arr8_string()
        # binary_string = hx.get_binary_string()
        # print binary_string + " " + np_arr8_string
        
        # Prints the weight. Comment if you're debbuging the MSB and LSB issue.
        val = max(0, hx.get_weight(5))
        if (val > 0 ):
            total = (random.random() * 3.0 + 1) * val; 
            data = {'id' : "1", 'total' : str(total), 'recycle':str(val)};
            requests.post(url = ENDPOINT, data = data);
            print data

        hx.power_down()
        hx.power_up()
        time.sleep(5)
    except (KeyboardInterrupt, SystemExit):
        cleanAndExit()
