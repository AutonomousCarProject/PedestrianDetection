/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class Rectangle extends GuiComponent
{
    public static final double
            DEFAULT_X = 0,
            DEFAULT_Y = 0,
            DEFAULT_WIDTH = 100,
            DEFAULT_HEIGHT = 100;
    public static final Background
            DEFAULT_BACKGROUND = new Background(Color.BLACK);
    
    
    
    private double width,height;
    private Background background;
    
    
    public Rectangle(double x, double y, double width, double height, Background background)
    {
        super(x,y);
        this.width = width;
        this.height = height;
        this.background = background;
    }
    public Rectangle()
    {
        this(DEFAULT_X,DEFAULT_Y,DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_BACKGROUND);
    }
    public void move(double xInc, double yInc)
    {
        super.setPosition(getX() + xInc,getY() + yInc);
    }
    public void setDimensions(double width, double height)
    {
        this.width = width;
        this.height = height;
    }
    public double getWidth()
    {
        return width;
    }
    public double getHeight()
    {
        return height;
    }
    public Background getBackground()
    {
        return background;
    }
    public void setBackground(Background background)
    {
        this.background = background;
    }
    protected void looiPaint()
    {
        if(background.ofColor())
        {
            setColor(background.getColor());
            fillRect(getX(),getY(),width,height);
        }
        else
        {
            drawImage(background.getImage(),getX(),getY(),width,height);
        }
    }
}

