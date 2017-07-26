/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import com.looi.looi.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 *
 * @author peter_000
 */
public class Slider extends Rectangle
{
    public static final double 
            DEFAULT_TRACK_HEIGHT = 3,
            DEFAULT_SLIDER_WIDTH = 10,
            DEFAULT_SLIDER_HEIGHT = 10,
            DEFAULT_MAX_SLIDER_HEIGHT = 20,
            DEFAULT_HORIZONTAL_MARGIN_FRACTION = .1;//.1 of width
    public static final Color 
            DEFAULT_TRACK_COLOR = Color.LIGHT_GRAY,
            DEFAULT_SLIDER_COLOR = Color.BLACK;
    private double percentage = 0;
    private double trackHeight;
    private double horizontalMargin;
    private SliderObject sliderObject;
    private double sliderMinX;
    private double sliderMaxX;
    private Color sliderColor;
    private Color trackColor;
    
    
    public Slider(double x, double y, double width, double height, Background background, double horizontalMargin, double trackHeight, double sliderWidth, double sliderHeight, double maxSliderHeight, Color sliderColor, Color trackColor)
    {
        super(x,y,width,height,background);
        setHorizontalMargin(horizontalMargin);
        setTrackHeight(trackHeight);
        add(sliderObject = new SliderObject(sliderWidth,sliderHeight,maxSliderHeight));
        setSliderColor(sliderColor);
        setTrackColor(trackColor);
    }
    public Slider(double x, double y, double width, double height, Background background, double horizontalMargin, double trackHeight, double sliderWidth, double sliderHeight, double maxSliderHeight)
    {
        this(x,y,width,height,background,horizontalMargin,trackHeight,sliderWidth,sliderHeight,maxSliderHeight,DEFAULT_SLIDER_COLOR,DEFAULT_TRACK_COLOR);
    }
    public Slider(double x, double y, double width, double height, Background background)
    {
        this(x,y,width,height,background,DEFAULT_HORIZONTAL_MARGIN_FRACTION*width,DEFAULT_TRACK_HEIGHT,DEFAULT_SLIDER_WIDTH,DEFAULT_SLIDER_HEIGHT,DEFAULT_MAX_SLIDER_HEIGHT);
    }
    public double getHorizontalMargin(){return horizontalMargin;}
    public double getTrackHeight(){return trackHeight;}
    public void setHorizontalMargin(double horizontalMargin){this.horizontalMargin = horizontalMargin;}
    public void setTrackHeight(double trackHeight){this.trackHeight = trackHeight;}
    public double getSliderMinX(){return sliderMinX;}
    public double getSliderMaxX(){return sliderMaxX;}
    public Color getSliderColor(){return sliderColor;}
    public void setSliderColor(Color sliderColor){this.sliderColor = sliderColor;}
    public Color getTrackColor(){return trackColor;}
    public void setTrackColor(Color trackColor){this.trackColor = trackColor;}
    
    
    
    
    protected void looiStep()
    {
        super.looiStep();
        sliderMinX = getX() + getHorizontalMargin();
        sliderMaxX = getX() + getWidth() - getHorizontalMargin();
        double totalDistance = sliderMaxX - sliderMinX;
        double sliderObjectX = sliderObject.getX();
        double sliderObjectProgress = sliderObjectX - sliderMinX;
        
        //sliderObject.setPosition(startX + distance * percentage/100,getY() + getHeight()/2);
        percentage = sliderObjectProgress/totalDistance * 100;
    }
    protected void looiPaint()
    {
        super.looiPaint();
        setColor(trackColor);
        fillRect(getX() + getHorizontalMargin(),getY() + getHeight()/2 - getTrackHeight()/2,getWidth() - getHorizontalMargin()*2, getTrackHeight());
    }
    
    public double getPercentage(){return percentage;}
    
    public class SliderObject extends GuiComponent
    {
        private double width,height,maxHeight;
        private Point center;
        private Point[] points;
        private boolean isPressed = false;
        public SliderObject(double width, double height, double maxHeight)
        {
            super(Slider.this.getWidth()/2,Slider.this.getHeight()/2);
            //System.out.println(Slider.this.getX() + Slider.this.getWidth()+","+(Slider.this.getY() + Slider.this.getHeight()/2));
            this.width = width;
            this.height = height;
            this.maxHeight = maxHeight;
        }
        protected void looiStep()
        {
            super.looiStep();
            
            
            
            
            if(isPressed)
            {
                if(getInternalMouseX() < sliderMinX)
                {
                    setPosition(sliderMinX,Slider.this.getY() + Slider.this.getHeight()/2);
                }
                else if(getInternalMouseX() > sliderMaxX)
                {
                    setPosition(sliderMaxX,Slider.this.getY() + Slider.this.getHeight()/2);
                }
                else
                {
                    setPosition(getInternalMouseX(),Slider.this.getY() + Slider.this.getHeight()/2);
                }
            }
        }
        
        protected void looiPaint()
        {
            super.looiPaint();
            center = new Point(getX(),getY());
            points = findPoints();
            setColor(sliderColor);
            fillPolygon(points);
        }
        protected Point[] findPoints()
        {
            Point leftUp = new Point(center.getX()-width/2,center.getY()-height/2);
            Point up = new Point(center.getX(),center.getY()-maxHeight/2);
            Point rightUp = new Point(center.getX()+width/2,center.getY()-height/2);
            
            Point leftDown = new Point(center.getX()-width/2,center.getY()+height/2);
            Point down = new Point(center.getX(),center.getY()+maxHeight/2);
            Point rightDown = new Point(center.getX()+width/2,center.getY()+height/2);
            
            return new Point[] {leftUp,up,rightUp,rightDown,down,leftDown};
        }
        protected void mousePressed(MouseEvent e)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                if(new Point(getInternalMouseX(),getInternalMouseY()).insideConvexSpace(points))
                {
                    isPressed = true;
                }
            }
        }
        protected void mouseReleased(MouseEvent e)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                isPressed = false;
            }
        }
    }
    
}
