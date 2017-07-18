package group2;

import group1.IPixel;

public class Blob
{
    public int width, height;
    public float centerX, centerY;
    public IPixel color;

    public Blob(int width, int height, float centerX, float centerY, IPixel color)
    {
        set(width, height, centerX, centerY, color);
    }

    public void set(int width, int height, float centerX, float centerY, IPixel color)
    {
        this.width = width;
        this.height = height;
        this.centerX = centerX;
        this.centerY = centerY;
        this.color = color;
    }
}
