package group1;

public interface IPixel
{
    // All values between 0 and 255
	
    // YUV
    short getLuma();

    short getSaturation();

    short getColor();

    //CUZ
    void setColor(short hue);
    //Hue is an integer, doesn't mater on what scale for now

    // RGB
    short getRed();

    short getGreen();

    short getBlue();
}