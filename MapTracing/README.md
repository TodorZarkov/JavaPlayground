To run the aplicaton type on the console(windows or linux or weatever OS suport jre-7):

java -jar tracer.jar NAME_OF_THE_MAP_PICTURE NAME_OF_THE_CANVAS_PICTURE


For example:

java -jar tracer.jar map7.bmp for_map7.bmp

NOTE:
https://bugs.launchpad.net/ubuntu/+source/openjdk-7/+bug/1110138







Using Tracer is pretty easy although without much benefit. It has mainly
demonstrative purpose.

Clicking with the main button of the mouse at the map You set the beginning
point. Clicking(with the same button) for the second time You set the 
destination point and, simultaneously with that, it calculates a shortest path
between these two points.

To clear all the paths You click with the secondary mouse button.

NOTE: It may costs more time with a bigger map. For the example map7.bmp,
with two core intel 2.4GHz it costs about 1.5 seconds worst case. More about
the efficiency issues and the algorithms behind the application You can read
on the main page of the program.








You can draw your own map, just consider the following rules:

The forbidden colour is set to black(0x000000). That means the pixels with
that color cannot be reached. Any other color is available. The color of 
a pixel represent its weight; that means if we have two pixels named 
respectively rootX and rootY, the weight between them would be calculate as:

weight(rootX,rootY) = abs(rootXr + rootXg + rootXb - rootYr - rootYg - rootYb)+1

if rootX and rootY are not diagonal pixels, and

weight(rootX,rootY) = weight(rootX,rootY)*sqrt(2)

if rootX and rootY are diagonal pixels.
In the formulae above the postfixes r, g , b means respectivelly red, green 
and blue, which are the red, green and blue bytes of the color of the pixel.
 You may notice that the byte type has 256 numbers, but it contains also 
negative numbers so for example 0xff is -1 ,and such number is passed to the 
formulae above not 255!!!

The weight between two pixels you can consider as a lenght between them,
or as the one of them is taller than the other with the calculated weight.

The above rules must be obtain in order to create a mapFile which can be
passed as a first argument to the tracer.jar file, e.g.

java -jar tracer.jar YOUR_MAPfILE

The second argument is just a cnavas on which the calculated path is
going to be drawn, so it's good to be related with the mapFile, i.e.
same width, same height...

If the second argument is not passed, the mapFile will serve as canvas.
All the files remains unchanged; no drawing onto the files are there from
the aplication.
