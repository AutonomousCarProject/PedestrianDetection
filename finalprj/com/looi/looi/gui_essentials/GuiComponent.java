/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import com.looi.looi.LooiObject;

/**
 *
 * @author peter_000
 */
public abstract class GuiComponent extends LooiObject
{
    private double x;
    private double y;
    public GuiComponent()
    {
        x = 0;
        y = 0;
    }
    public GuiComponent(double x, double y)
    {
        setPosition(x,y);
        goToFront();
    }
    public double getX()
    {
        return x;
    }
    public double getY()
    {
        return y;
    }
    public void setPosition(double x, double y)
    {
        double deltaX = x - this.x;
        double deltaY = y - this.y;
        
        this.x = x;
        this.y = y;
        
        for(int i = 0; i < getNumComponents(); i++)
        {
            LooiObject l = getComponent(i);
            if(l instanceof GuiComponent)
            {
                GuiComponent g = (GuiComponent)l;
                g.setPosition(g.getX() + deltaX,g.getY() + deltaY);
            }
        }
    }
    public void add(GuiComponent g)
    {
        super.add(g);
        g.setPosition(x+g.getX(),y+g.getY());
        g.setLayer(getLayer()-1);
        
    }
    public void remove(GuiComponent g)
    {
        super.remove(g);
        g.setPosition(g.getX() - x,g.getY() - y);
    }
    public void setLayer(double layer)
    {
        double difference = layer - getLayer();
        super.setLayer(layer);
        
        for(int i = 0; i < getNumComponents(); i++)
        {
            getComponent(i).setLayer(getComponent(i).getLayer() + difference);
        }
    }
}
