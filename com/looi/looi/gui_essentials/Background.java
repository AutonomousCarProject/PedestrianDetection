/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;
import java.awt.Image;

/**
 *
 * @author peter_000
 */
public class Background 
{
    public static final Background 
            WHITE_BACKGROUND = new Background(Color.WHITE),
            BLACK_BACKGROUND = new Background(Color.BLACK),
            LIGHT_GRAY_BACKGROUND = new Background(Color.LIGHT_GRAY),
            RED_BACKGROUND = new Background(Color.RED),
            DARK_GRAY_BACKGROUND = new Background(Color.DARK_GRAY),
            BLUE_BACKGROUND = new Background(Color.BLUE);
    private Color color;
    private Image image;
    public Background(Color color)
    {
        this.color = color;
    }
    public Background(Image image)
    {
        this.image = image;
    }
    public boolean ofColor()
    {
        return color != null;
    }
    public boolean ofImage()
    {
        return image != null;
    }
    public Color getColor()
    {
        return color;
    }
    public Image getImage()
    {
        return image;
    }
}
