package group1;

//Generates dummy data
public class Dummy4 implements IImage
{

    public int height = 240;
    public int width = 320;
    private Pixel[][] image = new Pixel[320][240];

    //@formatter:off
    private static final int[][] baseImage = new int[][] {
        { 0, 0, 1, 1, 1, 1, 2, 2, 2, 2 },
        { 0, 0, 1, 1, 1, 1, 2, 2, 2, 2 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 2, 2, 1, 3, 3, 3, 3, 3, 3, 3 },
        { 2, 2, 1, 1, 3, 3, 3, 3, 3, 3 },
        { 2, 2, 2, 2, 1, 1, 3, 3, 3, 3 },
        { 2, 2, 2, 2, 1, 1, 3, 3, 3, 3 },
    };
    //@formatter:on

    // fills image array
    @Override
    public void readCam()
    {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                int color = baseImage[j / 40][i / 40];
                short yes = 255, no = 0;

                image[j][i] = new Pixel(color == 1 ? yes : no, color == 2 ? yes : no, color == 3 ? yes : no);

            }

        }
    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }

}
