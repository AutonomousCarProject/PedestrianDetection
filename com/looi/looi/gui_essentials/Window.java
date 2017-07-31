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
    public class ExitButton extends AstheticButton
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
    }
}
