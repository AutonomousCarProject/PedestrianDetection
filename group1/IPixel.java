package group1;

public interface IPixel
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
    
    void setRGB(short r, short g, short b);
}