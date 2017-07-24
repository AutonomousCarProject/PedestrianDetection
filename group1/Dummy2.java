package group1;

import java.util.Random;

//Generates dummy data
public class Dummy2 implements IImage
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

                if (j < (width / 3f))
                {
                    image[j][i] = new Pixel((short) 255, (short) 0, (short) 0);
                }
                else if (j < (width / 1.5f))
                {
                    image[j][i] = new Pixel((short) 0, (short) 255, (short) 0);
                }
                else
                {
                    image[j][i] = new Pixel((short) 0, (short) 0, (short) 255);
                }

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
