package group1;

import java.io.Serializable;

public interface IPixel extends Serializable
{
    // All values between 0 and 255
	
    // YUV
    short getLuma();

    short getSaturation();

    short getColor();

    // RGB
    short getRed();

    short getGreen();

    short getBlue();
}