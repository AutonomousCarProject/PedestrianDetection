/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import com.looi.looi.Point;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class ScrollBox extends Rectangle
{
    public static final double DEFAULT_MOUSE_WHEEL_SCROLLING_COEFFICIENT = 5;
    public static final double DEFAULT_BOTTOM_ITEM_MARGIN = 15;
    
    private ArrayList<ScrollBoxObject> scrollBoxObjects = new ArrayList<>();
    private ScrollBar scrollBar;
    private double scrollPercentage = 0;//0-100
    private double virtualHeight;
    private double highestObjectY;
    private double lowestObjectYPlusItsHeight;
    private boolean scrollWithMouseWheel = true;
    private double mouseWheelScrollingCoefficient = DEFAULT_MOUSE_WHEEL_SCROLLING_COEFFICIENT;
    private double bottomItemMargin = DEFAULT_BOTTOM_ITEM_MARGIN;
    private boolean selected = false;
    private boolean deactivateNonvisibleComponents = false;
    
    public ScrollBox(double x, double y, double width, double height, Background background)
    {
        super(x,y,width,height,background);
        addStationary(scrollBar = createScrollBar());
    }
    
    public void mousePressed(MouseEvent e)
    {
        if(touchingMouse())
        {
            selected = true;
        }
        else
        {
            selected = false;
        }
    }
    public void select(){selected = true;}
    public void deselect(){selected = false;}
    public boolean isSelected(){return selected;}
    public void setMouseWheelScrollingCoefficient(double d){mouseWheelScrollingCoefficient = d;}
    public double getMouseWheelScrollingCoefficient(){return mouseWheelScrollingCoefficient;}
    public void setScrollWithMouseWheel(boolean b){scrollWithMouseWheel = b;}
    public boolean isScrollingWithMouseWheel(){return scrollWithMouseWheel;}
    public double getBottomItemMargin(){return bottomItemMargin;}
    public void setBottomItemMargin(double d){bottomItemMargin = d;}
    public ScrollBar getScrollBar(){return scrollBar;}
    public ArrayList<ScrollBoxObject> getScrollBoxObjects(){return scrollBoxObjects;}
    public boolean isDeactivatingNonvisibleComponents(){return deactivateNonvisibleComponents;}
    public void setDeactivateNonvisibleComponents(boolean b){deactivateNonvisibleComponents = b;}
    
    public void activate(Object...o)
    {
        super.activate(o);
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            s.thisComponent().activate(o);
        }
    }
    public void deactivate(Object...o)
    {
        super.deactivate(o);
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            s.thisComponent().deactivate(o);
        }
    }
    public void setLayer(double layer)
    {
        double difference = layer - getLayer();
        super.setLayer(layer);
        if(scrollBoxObjects == null)
            return;
        for(int i = 0; i < scrollBoxObjects.size(); i++)
        {
            scrollBoxObjects.get(i).thisComponent().setLayer(scrollBoxObjects.get(i).thisComponent().getLayer() + difference);
        }
    }
    protected ScrollBar createScrollBar()
    {
        return new ScrollBar();
    }
    public void setPosition(double x, double y)
    {
        double deltaX = x - getX();
        double deltaY = y - getY();
        
        super.setPosition(x,y);
        if(scrollBoxObjects == null)
            return;
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            s.setVirtualPosition(s.getVirtualX() + deltaX,s.getVirtualY() + deltaY);
        }
    }
    public void add(GuiComponent g)
    {
        addScrollBoxObject(new ScrollBoxObject(g));
    }
    public void remove(GuiComponent g)
    {
        for(ScrollBoxObject sbo : scrollBoxObjects)
        {
            if(sbo.thisComponent() == g)
            {
                removeScrollBoxObject(sbo);
                return;
            }
        }
    }
    protected void addScrollBoxObject(ScrollBoxObject g)
    {
        g.setVirtualPosition(g.getVirtualX() + getX(),g.getVirtualY() + getY());
        g.thisComponent().setLayer(getLayer() - 1); 
        scrollBoxObjects.add(g);
        g.thisComponent().setPaintBoundary(this); 
        
    }
    protected void removeScrollBoxObject(ScrollBoxObject g)
    {
        g.setVirtualPosition(g.getVirtualX() - getX(),g.getVirtualY() - getY());
        scrollBoxObjects.remove(g);
        g.thisComponent().setPaintBoundary(null); 
    }
    public void addStationary(GuiComponent g)
    {
        super.add(g);
    }
    public void removeStationary(GuiComponent g)
    {
        super.remove(g);
    }
    protected void looiStep()
    {
        findHighLowAndHeight();
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            s.thisComponent().setPosition(s.getVirtualX(),s.getVirtualY() - scrollPercentage/100.0 * (virtualHeight - getHeight()));
        }
        if(deactivateNonvisibleComponents)
        {
            hideAndShowScrollBoxObjects();
        }
        
    }
    protected void findHighLowAndHeight()
    {
        if(scrollBoxObjects.isEmpty())
        {
            virtualHeight = 0;
            highestObjectY = 0;
            lowestObjectYPlusItsHeight = 0;
            return;
        }
        highestObjectY = scrollBoxObjects.get(0).getVirtualY();
        lowestObjectYPlusItsHeight = scrollBoxObjects.get(0).getVirtualY();
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            if(s.getVirtualY() < highestObjectY)
            {
                highestObjectY = s.getVirtualY();
            }
            if(s.getVirtualY() > lowestObjectYPlusItsHeight)
            {
                lowestObjectYPlusItsHeight = s.getVirtualY() + bottomItemMargin;
            }
            if(s.thisComponent() instanceof Rectangle)
            {
                Rectangle r = (Rectangle)s.thisComponent();
                if(s.getVirtualY() + r.getHeight() > lowestObjectYPlusItsHeight)
                {
                    lowestObjectYPlusItsHeight = s.getVirtualY() + r.getHeight() + bottomItemMargin;
                }
            }
        }
        
        virtualHeight = lowestObjectYPlusItsHeight - getY();
        if(virtualHeight < getHeight())
        {
            virtualHeight = getHeight();
        }
        
    }
    public double getVirtualHeight()
    {
        return virtualHeight;
    }
    public double getScrollPercentage()
    {
        return scrollPercentage;
    }
    public void scrollUp(double percentage)
    {
        scrollPercentage -= percentage;
        if(scrollPercentage < 0)
        {
            scrollPercentage = 0;
        }
        if(scrollPercentage > 100)
        {
            scrollPercentage = 100;
        }
    }
    public void scrollDown(double percentage)
    {
        scrollPercentage += percentage;
        if(scrollPercentage > 100)
        {
            scrollPercentage = 100;
        }
        if(scrollPercentage < 0)
        {
            scrollPercentage = 0;
        }
    }
    public void scrollTo(double percentage)
    {
        scrollPercentage = percentage;
        if(scrollPercentage > 100)
        {
            scrollPercentage = 100;
        }
        if(scrollPercentage < 0)
        {
            scrollPercentage = 0;
        }
    }
    protected void hideAndShowScrollBoxObjects()
    {
        for(ScrollBoxObject s : scrollBoxObjects)
        {
            s.checkOutOfScrollBox();
        }
    }
    public class ScrollBoxObject
    {
        private GuiComponent thisComponent;
        private double virtualX,virtualY;
        public ScrollBoxObject(GuiComponent thisComponent)
        {
            this.thisComponent = thisComponent;
            virtualX = thisComponent.getX();
            virtualY = thisComponent.getY();
        }
        public double getVirtualX()
        {
            return virtualX;
        }
        public double getVirtualY()
        {
            return virtualY;
        }
        public void setVirtualPosition(double x, double y)
        {
            virtualX = x;
            virtualY = y;
        }
        public GuiComponent thisComponent()
        {
            return thisComponent;
        }
        public void checkOutOfScrollBox()
        {
            if(thisComponent() instanceof Rectangle)
            {
                Rectangle r = (Rectangle)thisComponent();
                if(r.getY()  + r.getHeight() > ScrollBox.this.getY() + ScrollBox.this.getHeight() || r.getY() < ScrollBox.this.getY())
                {
                    thisComponent().deactivate(this);
                }
                else
                {
                    thisComponent().activate(this);
                }
            }
            else
            {
                if(thisComponent().getY() > ScrollBox.this.getY() + ScrollBox.this.getHeight() || thisComponent().getY() < ScrollBox.this.getY())
                {
                    thisComponent().deactivate(this);
                }
                else
                {
                    thisComponent().activate(this);
                }
            }
            
        }
    }
    public class ScrollBar extends AstheticButton
    {
        public static final double DEFAULT_WIDTH = 35;
        private boolean scrolling = false;
        private Point offsetFromMouse;

        public ScrollBar()
        {
            this(Color.LIGHT_GRAY,DEFAULT_WIDTH);
        }
        public ScrollBar(Color myColor, double width)
        {
            super(ScrollBox.this.getWidth() - width,0,width,0,"",new Background(myColor)) ;
            setDepth(0);
        }
        
        @Override
        protected void action() {}
        protected void looiStep()
        {
            super.looiStep();
            double viewIsWhatFractionOfHeight = ScrollBox.this.getHeight() / ScrollBox.this.getVirtualHeight();
            if(viewIsWhatFractionOfHeight > 1)
            {
                viewIsWhatFractionOfHeight = 1;
            }
            double newHeight = viewIsWhatFractionOfHeight * ScrollBox.this.getHeight();
            setDimensions(getWidth(),newHeight);
            
            double totalScrollableDistance = ScrollBox.this.getHeight() - newHeight;
            setPosition(getX(),ScrollBox.this.getY() + ScrollBox.this.getScrollPercentage() / 100.0 * totalScrollableDistance);
            
            if(super.justPressed())
            {
                offsetFromMouse = new Point(getX() - getInternalMouseX(),getY() - getInternalMouseY());
                scrolling = true;
            }
            if(!mouseLeftEngaged())
            {
                scrolling = false;
            }
            if(scrolling())
            {
                if(totalScrollableDistance > 0)
                {
                    double scrollTo = (getInternalMouseY() + offsetFromMouse.getY() - ScrollBox.this.getY()) / totalScrollableDistance;
                    ScrollBox.this.scrollTo(scrollTo * 100);
                }   
            }
            if(this.getMouseWheelRotation() != 0 && isScrollingWithMouseWheel() && isSelected())
            {
                ScrollBox.this.scrollDown(getMouseWheelRotation() * getMouseWheelScrollingCoefficient()); 
                
            }
        }
        public boolean scrolling()
        {
            return scrolling;
        }
        public boolean isPressed()
        {
            return scrolling();
        }
    }
}
