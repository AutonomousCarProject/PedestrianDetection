package group1;

public class Dummy3 implements IImage
{
    public int height = 480;
    public int width = 640;
    Pixel[][] image = new Pixel[height][width];

    public Dummy3()
    {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                if (x < (width / 3))
                {
                    image[y][x] = new Pixel((short) 255, (short) 0, (short) 0);
                }
                else if (x < ((2 * width) / 3))
                {
                    image[y][x] = new Pixel((short) 0, (short) 255, (short) 0);
                }
                else
                {
                    image[y][x] = new Pixel((short) 0, (short) 0, (short) 255);
                }

            }

        }

    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }
}