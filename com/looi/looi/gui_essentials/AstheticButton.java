/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;
import com.looi.looi.gui_essentials.Font;
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
            DEFAULT_BOTTOM_SHADOW = 45,
            DEFAULT_HOVER_LIGHT_UP = 40;
    public static final double DEFAULT_DEPTH = 6;
    public static final Color 
            DEFAULT_TEXT_COLOR = Color.BLACK,
            DEFAULT_BOTTOM_COLOR = Color.DARK_GRAY,
            DEFAULT_RIGHT_COLOR = Color.LIGHT_GRAY;
    
    
    
    private Color bottomColor,topColor,rightColor,leftColor,textColor,textColorPressed,bottomColorLightUp,topColorLightUp,rightColorLightUp,textColorLightUp;
    private Font font;
    private String text;
    private int buttonPressShadow,bottomShadow,rightShadow,hoverLightUp;
    private boolean isPressed = false,justPressed = false,justReleased = false,lastMouseClickValid = false;
    private double depth;
    private Background pressedBackground;
    public AstheticButton(double x, double y, double width, double height, String text, Background background) 
    {
        super(x,y,width,height,background);
        
        setTextColor(DEFAULT_TEXT_COLOR);
        
        if(background.ofColor())
        {
            setRightShadow(DEFAULT_RIGHT_SHADOW);
            setBottomShadow(DEFAULT_BOTTOM_SHADOW);
        }
        else
        {
            setBottomColor(DEFAULT_BOTTOM_COLOR);
            setRightColor(DEFAULT_RIGHT_COLOR);
        }
        setButtonPressShadow(DEFAULT_BUTTON_PRESS_SHADOW);
        setDepth(DEFAULT_DEPTH);
        setFont(DEFAULT_FONT);
        setText(text);
        setTextColorPressed(applyShadow(textColor,buttonPressShadow));
        if(background.ofColor())
        {
            setPressedBackground(new Background(applyShadow(background.getColor(),buttonPressShadow)));
        }
        else
        {
            setPressedBackground(background);
        }
        setHoverLightUp(DEFAULT_HOVER_LIGHT_UP);
    }
    //public Asthetic
    
    public void setBottomColor(Color c){bottomColor = c;}
    public void setTopColor(Color c){topColor = c;}
    public void setLeftColor(Color c){leftColor = c;}
    public void setRightColor(Color c){rightColor = c;}
    
    
    public void setTextColor(Color c){textColor = c;}
    public void setTextColorPressed(Color c){textColorPressed = c;}
    public Color getBottomColor(){return bottomColor;}
    public Color getTopColor(){return topColor;}
    public Color getRightColor(){return rightColor;}
    public Color getLeftColor(){return leftColor;}
    public Color getTextColor(){return textColor;}
    public Color getTextColorPressed(){return textColorPressed;}
    
    public void setFont(Font f){font = f;}
    public void setText(String s){text = s;}
    public void setDepth(double d){depth = d;}
    public void setPressedBackground(Background b){pressedBackground = b;}
    public Font getFont(){return font;}
    public String getText(){return text;}
    public double getDepth(){return depth;}
    public Background getPressedBackground(){return pressedBackground;}
    public int getHoverLightUp(){return hoverLightUp;}
    public void setHoverLightUp(int i)
    {
        hoverLightUp = i;
        if(getBackground().ofColor())
        {
            topColorLightUp = applyLight(getBackground().getColor(),hoverLightUp);
        }
        else
        {
            topColorLightUp = Color.BLACK;
        }
        rightColorLightUp = applyLight(rightColor,hoverLightUp);
        bottomColorLightUp = applyLight(bottomColor,hoverLightUp);
        textColorLightUp = applyLight(textColor,hoverLightUp);
    }
    
    public void setBottomShadow(int i)
    {
        bottomShadow = i;
        if(getBackground().ofColor())
            bottomColor = applyShadow(getBackground().getColor(),bottomShadow);
    }
    public void setRightShadow(int i)
    {
        rightShadow = i;
        if(getBackground().ofColor())
            rightColor = applyShadow(getBackground().getColor(),rightShadow);
    }
    
    public int getButtonPressShadow(){return buttonPressShadow;}
    public void setButtonPressShadow(int d)
    {
        buttonPressShadow = d;
        topColor = applyShadow(bottomColor,buttonPressShadow);
        leftColor = applyShadow(rightColor,buttonPressShadow);
    }
    protected Color applyShadow(Color beforeShadow, int shadow)
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
    protected Color applyLight(Color beforeShadow, int shadow)
    {
        int r = beforeShadow.getRed() + shadow;
        int g = beforeShadow.getGreen() + shadow;
        int b = beforeShadow.getBlue() + shadow;
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
                if(touchingMouse())
                {
                    setColor(rightColorLightUp);
                }
                else
                {
                    setColor(rightColor);
                }
                
                fillRect(getX(),getY(),getWidth(),getHeight());
                if(touchingMouse())
                {
                    setColor(bottomColorLightUp);
                }
                else
                {
                    setColor(bottomColor);
                }
                double[] xes = {getX(),getX()+getWidth()-depth,getX()+getWidth(),getX()};
                double[] ys = {getY()+getHeight()-depth,getY()+getHeight()-depth,getY()+getHeight(),getY()+getHeight()};
                fillPolygon(xes,ys);
                
                if(getBackground().ofColor())
                {
                    if(touchingMouse())
                    {
                        setColor(topColorLightUp);
                    }
                    else
                    {
                        setColor(getBackground().getColor());
                    }
                    
                    fillRect(getX(),getY(),getWidth()-depth,getHeight()-depth);
                }
                else
                {
                    drawImage(getBackground().getImage(),getX(),getY(),getWidth()-depth,getHeight()-depth);
                }
                if(touchingMouse())
                {
                    setColor(textColorLightUp);
                }
                else
                {
                    setColor(textColor);
                }
                
                super.setFont(getFont());
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
                super.setFont(getFont());
                drawString(text,getX()+depth,getY()+(getHeight()-depth/2.0+font.getSize())/2.0+depth);
            }
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
