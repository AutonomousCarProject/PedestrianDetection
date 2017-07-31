package group1;

public interface IImage
{
    IPixel[][] getImage();
	void readCam();
	void finish();
	void setAutoFreq(int autoFreq);	
        default void setImage(IPixel[][] image){}
        default int getFrameNo(){return 0;}
}
