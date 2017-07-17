package group2;

import group1.IPixel;

public class TestPixel implements IPixel
{
    private short color;

    public TestPixel(int color)
    {
        this.color = (short) color;
    }

    @Override
    public short getLuma()
    {
        return 255;
    }

    @Override
    public short getSaturation()
    {
        return 255;
    }

    @Override
    public short getColor()
    {
        return color;
    }

    @Override
    public short getRed()
    {
        return 255;
    }

    @Override
    public short getGreen()
    {
        return 0;
    }

    @Override
    public short getBlue()
    {
        return 0;
    }

}