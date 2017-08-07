/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 *
 * @author peter_000
 */
public class PatternBackground extends Background
{
    private double repeatWidth;
    private double repeatHeight;
    public PatternBackground(Image image, double repeatWidth, double repeatHeight) 
    {
        super(image);
        this.repeatHeight = repeatHeight;
        this.repeatWidth = repeatWidth;
    }
    public Image getImage(double width, double height)
    {
        int iWidth = (int)width;
        int iHeight = (int)height;
        BufferedImage b = new BufferedImage(iWidth,iHeight,BufferedImage.TYPE_INT_ARGB);
        Graphics g = b.createGraphics();
        for(int x = 0; x < width; x += repeatWidth)
        {
            for(int y = 0; y < height; y += repeatHeight)
            {
                g.drawImage(getImage(),x,y,(int)repeatWidth,(int)repeatHeight,null);
            }
        }
        return b;
    }
}
