/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseEvent;

/**
 *
 * @author peter_000
 */
public abstract class AstheticButton extends Button
{
    public static final Font DEFAULT_FONT = new Font("",Font.PLAIN,16);
    public static final int 
            DEFAULT_BUTTON_PRESS_SHADOW = 65,
            DEFAULT_RIGHT_SHADOW = 70,
            DEFAULT_BOTTOM_SHADOW = 45;
    public static final double DEFAULT_DEPTH = 6;
    public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
    
    
    private Color bottomColor,topColor,rightColor,leftColor,textColor,textColorPressed;
    private Font font;
    private String text;
    private int buttonPressShadow;
    private boolean isPressed = false,justPressed = false,justReleased = false,lastMouseClickValid = false;
    private double depth;
    private Background pressedBackground;
    public AstheticButton(double x, double y, double width, double height, String text, Font font, Color textColor, Background background, Color bottomColor, Color rightColor, int buttonPressShadow, double depth) 
    {
        super(x,y,width,height,background);
        this.textColor = textColor;
        this.buttonPressShadow = buttonPressShadow;
        this.depth = depth;
        this.font = font;
        this.text = text;
        this.bottomColor = bottomColor;
        this.rightColor = rightColor;
        topColor = applyShadow(bottomColor,buttonPressShadow);
        leftColor = applyShadow(rightColor,buttonPressShadow);
        textColorPressed = applyShadow(textColor,buttonPressShadow);
        if(background.ofColor())
        {
            pressedBackground = new Background(applyShadow(background.getColor(),buttonPressShadow));
        }
        else
        {
            pressedBackground = background;
        }
    }
    public AstheticButton(double x, double y, double width, double height, String text, Image image, Color bottomColor, Color rightColor)
    {
        this(x,y,width,height,text,DEFAULT_FONT,DEFAULT_TEXT_COLOR,new Background(image),bottomColor,rightColor,DEFAULT_BUTTON_PRESS_SHADOW,DEFAULT_DEPTH);
    }
    public AstheticButton(double x, double y, double width, double height, String text, Color frontColor)
    {
        this(x,y,width,height,text,DEFAULT_FONT,DEFAULT_TEXT_COLOR,new Background(frontColor),applyShadow(frontColor,DEFAULT_BOTTOM_SHADOW),applyShadow(frontColor,DEFAULT_RIGHT_SHADOW),DEFAULT_BUTTON_PRESS_SHADOW,DEFAULT_DEPTH);
    }
    public static Color applyShadow(Color beforeShadow, int shadow)
    {
        int r = beforeShadow.getRed() - shadow;
        int g = beforeShadow.getGreen() - shadow;
        int b = beforeShadow.getBlue() - shadow;
        if(r > 255)r = 255;
        if(g > 255)g = 255;
        if(b > 255)b = 255;
        if(r < 0)r = 0;
        if(g < 0)g = 0;
        if(b < 0)b = 0;
        return new Color(r,g,b);
    }
    /*protected Image applyShadow(Image beforeShadow)
    {
        BufferedImage shadowImg = new BufferedImage(beforeShadow.getWidth(null),beforeShadow.getHeight(null),BufferedImage.TYPE_INT_ARGB);
        BufferedImage beforeShadowBI = new BufferedImage()
        for(int r = 0; r < shadowImg.getWidth(); r++)
        {
            for(int c = 0; c < shadowImg.getHeight(); c++)
            {
                
                Color newColor = beforeShadow.get
                int p = (newAlpha << 24) | (newRed << 16) | (newGreen << 8) | newBlue;
                shadowImg.setRGB(c, c, c);
            }
        }
    }*/
    protected void looiStep()
    {
        isPressed = false;
        justPressed = false;
        justReleased = false;
        boolean mouseOnButton = (getInternalMouseX() > getX() && getInternalMouseX() < getX()+getWidth() && getInternalMouseY() > getY() && getInternalMouseY() < getY()+getHeight());

        //check last click
        if(mouseLeftPressed())
        {
            if(mouseOnButton)
            {
                lastMouseClickValid = true;
            }
            else
            {
                lastMouseClickValid = false;
            }
        }

        if(mouseOnButton)
        {
            if(mouseLeftPressed())
            {
                justPressed = true;
            }
            if(mouseLeftReleased() && lastMouseClickValid)
            {
                justReleased = true;
            }
            if(mouseLeftEngaged() && lastMouseClickValid)
            {
                isPressed = true;
            }
        }
        if(justReleased)
        {
            action();
        }
    }
    protected void mouseReleased(MouseEvent k){}
    protected void looiPaint()
    {
        setFont(font);
        if(!isPressed())
            {
                setColor(rightColor);
                fillRect(getX(),getY(),getWidth(),getHeight());
                setColor(bottomColor);
                double[] xes = {getX(),getX()+getWidth()-depth,getX()+getWidth(),getX()};
                double[] ys = {getY()+getHeight()-depth,getY()+getHeight()-depth,getY()+getHeight(),getY()+getHeight()};
                fillPolygon(xes,ys);
                
                if(getBackground().ofColor())
                {
                    setColor(getBackground().getColor());
                    fillRect(getX(),getY(),getWidth()-depth,getHeight()-depth);
                }
                else
                {
                    drawImage(getBackground().getImage(),getX(),getY(),getWidth()-depth,getHeight()-depth);
                }
                
                setColor(textColor);
                drawString(text,getX(),getY()+(getHeight()-depth+font.getSize())/2.0);
            }
        else
            {
                setColor(leftColor);
                fillRect(getX(),getY(),getWidth(),getHeight());
                setColor(topColor);
                double[] xes = {getX(),getX()+getWidth(),getX()+getWidth(),getX()+depth};
                double[] ys = {getY(),getY(),getY()+depth,getY()+depth};
                fillPolygon(xes,ys);
                
                if(pressedBackground.ofColor())
                {
                    setColor(pressedBackground.getColor());
                    fillRect(getX()+depth,getY()+depth,getWidth()-depth,getHeight()-depth);
                }
                else
                {
                    drawImage(pressedBackground.getImage(),getX()+depth,getY()+depth,getWidth()-depth,getHeight()-depth);
                }

                setColor(textColorPressed);
                drawString(text,getX()+depth,getY()+(getHeight()-depth/2.0+font.getSize())/2.0+depth);
            }
    }
    public String getText()
    {
        return text;
    }
    public boolean isPressed()
    {
        return isPressed;
    }
    public boolean justPressed()
    {
        return justPressed;
    }
    public boolean justReleased()
    {
        return justReleased;
    }
    
}
