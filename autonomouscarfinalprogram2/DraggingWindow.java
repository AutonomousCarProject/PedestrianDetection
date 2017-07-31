/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Window;
import java.awt.event.MouseEvent;

/**
 *
 * @author peter_000
 */
public class DraggingWindow extends Window
{
    public DraggingWindow(double x, double y, double width, double height, Background b)
    {
        super(x,y,width,height,b);
    }
    private boolean dragging = false;
    protected void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1)
        {
            if(getInternalMouseX() <= getX() + getWidth() - 100 && getInternalMouseX() >= getX() && getInternalMouseY() > getY() && getInternalMouseY() < getY() + 75)
            {
                dragging = true;
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
    protected void looiStep()
    {
        if(dragging)
            setPosition(getInternalMouseX() - getWidth()/2, getInternalMouseY() - 32);
    }
}
