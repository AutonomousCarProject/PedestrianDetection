package group1;

public interface IImage
{
    IPixel[][] getImage();
	void readCam();
	void finish();
	void setAutoFreq(int autoFreq);	
	void setImage(IPixel[][] image);
	int getFrameNo();
}
