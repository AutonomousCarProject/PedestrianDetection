package group1;

public class Dummy2 implements IImage
{

    public int height = 240;
    public int width = 320;
    Pixel[][] image = new Pixel[320][240];

    public Dummy2()
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

}