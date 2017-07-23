package group1;

import java.util.Random;

//Generates dummy data
public class Dummy3 implements IImage
{

    public int height = 240;
    public int width = 320;
    private Random myRand = new Random();
    private Pixel[][] image = new Pixel[320][240];

    // fills image array
    @Override
    public void readCam()
    {
        for (int i = 0; i < height; i++)
        {

            for (int j = 0; j < width; j++)
            {

                image[j][i] = new Pixel((short) 255, (short) 0, (short) 0);

            }

        }
    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }

    public void autoColor(){};
}
