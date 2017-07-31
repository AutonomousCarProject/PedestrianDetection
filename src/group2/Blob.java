package group2;

import group1.IPixel;

public class Blob
{
    public int width, height;
    public int x, y;
    public IPixel color;
    public int id;

    public static int currentId = 0;

    public Blob(int width, int height, int x, int y, IPixel color)
    {
        set(width, height, x, y, color);
    }

    public void set(int width, int height, int x, int y, IPixel color)
    {
        set(width, height, x, y, color, currentId++);
    }

    public void set(int width, int height, int x, int y, IPixel color, int id)
    {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.color = color;
        this.id = id;
    }
}
