package group1;

public class Dummy2 implements IImage
{
    public int height = 480;
    public int width = 640;
    Pixel[][] image = new Pixel[height][width];

    public Dummy2()
    {
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                image[y][x] = new Pixel((short) 255, (short) 0, (short) 0);

            }

        }

    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }
}