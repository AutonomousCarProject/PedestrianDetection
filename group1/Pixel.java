package group1;
import java.lang.Math;

	
class Pixel implements IPixel {	//0-765
	//YUV
	
	short luma;
    short saturation;
    short color;

    // RGB
    short red;
    short green;
    short blue;
	
	Pixel(short red, short green, short blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		convert();
	}

	private void convert(){
		//luminance = sum of rgb
		luma = red + green + blue;
		
		//saturation = max - min of rgb
		saturation = (Math.max(Math.max(red, green), blue) - Math.min(Math.min(red, green), blue))*3;
		
		//hue
		short ave = (red + green + blue);
		int r = red*3;
		int g = green*3;
		int b = blue*3;
		int rdiff = r - ave;
		int gdiff = g - ave;
		int bdiff = b - ave;
		if(rdiff < colorMargin && gdiff < colorMargin && bdiff < colorMargin){	//if its not a distinct color
			int topMargin = 765 - colorMargin;
			if(r < colorMargin && g < colorMargin && b < colorMargin) color = 4;	//black
			else if(r > topMargin && g > topMargin && b > topMargin) color = 5;	//white
			else color = 3;
		}
		else if(rdiff > gdiff && rdiff > bdiff) color = 0;
		else if(gdiff > rdiff && gdiff > bdiff) color = 1;
		else if(bdiff > rdiff && bdiff > gdiff) color = 2;
	}
}