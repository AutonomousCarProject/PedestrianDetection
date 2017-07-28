package group1;

public interface IImage
{
    IPixel[][] getImage();
    void setImage(IPixel[][] image);
	void readCam();
	void finish();
	void setAutoFreq(int autoFreq);
	int getFrameNo();
}
