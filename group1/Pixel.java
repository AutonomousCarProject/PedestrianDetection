	
Pixel extends IPixel {	
	//YUV
	
	short luma;

    short saturation;

    short color;

    // RGB
    short red;

    short green;

    short blue;
	
	Pixel(short luma, short saturation, short color){
		this.luma = luma;
		this.saturation = saturation;
		this.color = color;
		red = null;
		green = null;
		blue = null;
	}
	
	Pixel(short red, short green, short blue){
		luma = null;
		saturation = null;
		color = null;
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
}