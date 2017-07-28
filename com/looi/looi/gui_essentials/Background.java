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
