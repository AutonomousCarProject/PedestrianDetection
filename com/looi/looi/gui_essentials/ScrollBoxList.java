/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

/**
 *
 * @author peter_000
 */
public class ScrollBoxList extends ScrollBox
{
    public static final double DEFAULT_TOP_MARGIN = 50;
    public static final double DEFAULT_LEFT_MARGIN = 25;
    public static final double DEFAULT_SPACING_BETWEEN_ITEMS = 15;
    private double topMargin = DEFAULT_TOP_MARGIN;
    private double leftMargin = DEFAULT_LEFT_MARGIN;
    private double spacingBetweenItems = DEFAULT_SPACING_BETWEEN_ITEMS;
    public ScrollBoxList(double x, double y, double width, double height, Background background) 
    {
        super(x, y, width, height, background);
    }
    
    public double getTopMargin(){return topMargin;}
    public double getLeftMargin(){return leftMargin;}
    public double getSpacingBetweenItems(){return spacingBetweenItems;}
    public void setTopMargin(double d){topMargin = d;}
    public void setLeftMargin(double d){leftMargin = d;}
    public void setSpacingBetweenItems(double d){spacingBetweenItems = d;}
    
    protected void looiStep()
    {
        double yPos = topMargin + getY();
        for(ScrollBoxObject s : getScrollBoxObjects())
        {
            if(s.thisComponent() instanceof Rectangle)
            {
                Rectangle r = (Rectangle)s.thisComponent();
                s.setVirtualPosition(getX() + leftMargin,yPos);
                yPos += r.getHeight() + spacingBetweenItems;
            }
        }
        super.looiStep();
    }
    
}
