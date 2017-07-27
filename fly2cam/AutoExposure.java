package fly2cam;

import group1.IPixel;

public class AutoExposure implements IAutoExposure
{
    private int framerate;
    private int currentFrame;

    private static final int BLOCK_SIZE = 16;
    private static final int SAMPLE_SIZE = 1;
    private static final int MAX_BOOST = 10;

    public AutoExposure(int framerate)
    {
        this.framerate = framerate;
        this.currentFrame = framerate - 1;
    }

    @Override
    public int exposureBoost(IPixel[][] pixels)
    {
        return 0;
    }

    private int oldShutter = 0;

    @Override
    public int shutterBoost(IPixel[][] pixels)
    {
        if (++currentFrame != framerate) return oldShutter;

        currentFrame = 0;

        final int height = pixels.length;
        final int width = pixels[0].length;

        final int mult = (765 * height * width) / (SAMPLE_SIZE * SAMPLE_SIZE);
        final int darkThresh = (int) (mult * 0.2f);
        final int lightThresh = (int) (mult * 0.8f);

        int sum = 0;
        for (int i1 = 0; i1 < width; i1 += BLOCK_SIZE)
        {
            for (int j1 = 0; j1 < height; j1 += BLOCK_SIZE)
            {
                int totalBrightness = 0;
                for (int i2 = 0; i2 < BLOCK_SIZE; i2 += SAMPLE_SIZE)
                {
                    for (int j2 = 0; j2 < BLOCK_SIZE; j2 += SAMPLE_SIZE)
                    {
                        final int i = (i1 * BLOCK_SIZE) + i2;
                        final int j = (j1 * BLOCK_SIZE) + j2;

                        IPixel pixel = pixels[i][j];
                        totalBrightness += pixel.getRed() + pixel.getGreen() + pixel.getBlue();
                    }
                }

                if (totalBrightness <= darkThresh)
                {
                    sum--;
                }
                else if (totalBrightness >= lightThresh)
                {
                    sum++;
                }
            }
        }

        final int maxSum = (int) (Math.ceil(width / (float) BLOCK_SIZE) * Math.ceil(height / (float) BLOCK_SIZE));
        final float sumRatio = 2 * ((((float) sum) / maxSum) - 0.5f);

        return Math.round(MAX_BOOST * sumRatio);
    }

    @Override
    public int gainBoost(IPixel[][] pixels)
    {
        return 0;
    }
}
