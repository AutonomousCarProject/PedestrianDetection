/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import com.looi.looi.utilities.InputBoolean;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class Window extends Rectangle
{
    
    public Window()
    {
        super();
        init();
    }
    public Window(double x, double y, double width, double height, Background background)
    {
        super(x,y,width,height,background);
        init();
    }
    private void init()
    {
        
    }
    
    
    public interface DecorationItem
    {
        public double getX();
        public double getY();
        public double getWidth();
        public double getHeight();
    }
    
    public class ExitButton extends AstheticButton implements DecorationItem
    {
        public static final double DEFAULT_MARGIN_TO_EDGE = 5;
        public static final double DEFAULT_WIDTH = 50;
        public static final double DEFAULT_HEIGHT = 50;
        public static final String DEFAULT_TEXT = "    X";
        
        
        public ExitButton()
        {
            super(
                    Window.this.getWidth() - DEFAULT_WIDTH - DEFAULT_MARGIN_TO_EDGE,
                    DEFAULT_MARGIN_TO_EDGE,
                    DEFAULT_WIDTH,DEFAULT_HEIGHT,DEFAULT_TEXT,Color.RED);
            
        }
        protected void action()
        {
            Window.this.deactivate(this);
        }
        protected void looiStep()
        {
            super.looiStep();
        }
    }
    public static final InputBoolean<DecorationBar> DEFAULT_DECORATIONBAR_CAN_DRAG_CONDITION = (bar) ->
    {
        if(bar.thisWindow().getInternalMouseX() >= bar.getX() && bar.thisWindow().getInternalMouseX() <= bar.getX() + bar.getWidth() && bar.thisWindow().getInternalMouseY() >= bar.getY() && bar.thisWindow().getInternalMouseY() <= bar.getY() + bar.getHeight())
        // if mouse is on top of the bar
        {
            for(DecorationItem d : bar.getDecorationItems())
            {
                if(bar.thisWindow().getInternalMouseX() >= d.getX() && bar.thisWindow().getInternalMouseX() <= d.getX() + d.getWidth() && bar.thisWindow().getInternalMouseY() >= d.getY() && bar.thisWindow().getInternalMouseY() <= d.getY() + d.getHeight())
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    };
    public class DecorationBar extends Rectangle
    {
        public static final double DEFAULT_HEIGHT = 60;
        private boolean dragging = false;
        private double dragXOffsetFromMouse;
        private double dragYOffsetFromMouse;
        private InputBoolean<DecorationBar> canDrag;
        private ArrayList<DecorationItem> decorationItems = new ArrayList<>();
        public DecorationBar(double x, double y, double width, double height, Background background, InputBoolean<DecorationBar> canDrag)
        {
            super(x,y,width,height,background);
            this.canDrag = canDrag;
        }
        public DecorationBar(Background background, InputBoolean<DecorationBar> canDrag)
        {
            this(0,0,Window.this.getWidth(),DEFAULT_HEIGHT,background,canDrag);
        }
        public DecorationBar(Background background)
        {
            this(background,DEFAULT_DECORATIONBAR_CAN_DRAG_CONDITION);
            
        }
        protected void mousePressed(MouseEvent e)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                if(canDrag.check(this) == true)
                {
                    dragging = true;
                    dragXOffsetFromMouse = Window.this.getX() - getInternalMouseX();
                    dragYOffsetFromMouse = Window.this.getY() - getInternalMouseY();
                }
            }
        }
        protected void mouseReleased(MouseEvent e)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                dragging = false;
            }
        }
        public ArrayList<DecorationItem> getDecorationItems()
        {
            return decorationItems;
        }
        protected void looiStep()
        {
            if(dragging)
                Window.this.setPosition(getInternalMouseX() + dragXOffsetFromMouse, getInternalMouseY() + dragYOffsetFromMouse);
        }
        public void add(GuiComponent g)
        {
            super.add(g);
            if(g instanceof DecorationItem)
            {
                decorationItems.add((DecorationItem)g);
            }
        }
        public void remove(GuiComponent g)
        {
            super.remove(g);
            if(g instanceof DecorationItem)
            {
                decorationItems.remove((DecorationItem)g);
            }
        }
        
    }
}
