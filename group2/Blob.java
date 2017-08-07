package group2;

import group1.IPixel;
import java.io.Serializable;

public class Blob implements Serializable
{
    public int width, height;
    public int x, y;
    public IPixel color;
    public int id;

    public static int currentId = 0;

    public Blob() {}
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
