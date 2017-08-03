/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.event.MouseEvent;

/**
 *
 * @author peter_000
 */
public abstract class Button extends Rectangle
{
    public Button(double x, double y, double width, double height, Background background)
    {
        super(x,y,width,height,background);
    }
    protected void looiStep()
    {
        
        
    }
    protected void mouseReleased(MouseEvent k)
    {
        double mouseX = getInternalMouseX();
        double mouseY = getInternalMouseY();
        if(k.getButton() == MouseEvent.BUTTON1 && mouseX >= getX() && mouseX <= getX() + getWidth() && mouseY >= getY() && mouseY <= getY() + getHeight())
        {
            action();
        }
    }
    protected abstract void action();
}
