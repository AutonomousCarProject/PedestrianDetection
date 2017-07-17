public interface Pixel
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
