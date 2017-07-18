package group2;

import group1.IPixel;

public class Blob
{
    public int width, height;
    public int centerX, centerY;
    public IPixel color;

    public Blob(int width, int height, int centerX, int centerY, IPixel color)
    {
        this.width = width;
        this.height = height;
        this.centerX = centerX;
        this.centerY = centerY;
        this.color = color;
    }
}
