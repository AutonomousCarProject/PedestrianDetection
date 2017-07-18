package group1;
import java.lang.Math;

	
public class Pixel implements IPixel {	//0-765
	//YUV
	private short luma;
    private short saturation;
    private short color;

	
	
    // RGB
    private short red;
    private short green;
    private short blue;
	
	int colorMargin = 30;
	
	Pixel(short red, short green, short blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		convert();
	}

	//Pixel(){}

	public void setRGB(short red, short green, short blue){
		this.red = red;
		this.green = green;
		this.blue = blue;
		convert();
	}
	
	public short getLuma(){
		return luma;
	}

	public short getSaturation(){
		return saturation;
	}
	
	public short getColor(){
		return color;
	}	
	
	public short getRed(){
		return red;
	}	
	
	public short getGreen(){
		return green;
	}	
	
	public short getBlue(){
		return blue;
	}	
	
	private void convert(){
		//luminance = sum of rgb
		luma = (short)(red + green + blue);
		
		//saturation = max - min of rgb
		saturation = (short)((Math.max(Math.max(red, green), blue) - Math.min(Math.min(red, green), blue))*3);
		
		//hue
		short ave = (short)(red + green + blue);
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