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
public class Font extends java.awt.Font
{
    public static final int DEFAULT_STYLE = Font.PLAIN;
    public static final Font _8_PT_PLAIN = new Font("",Font.PLAIN,8);
    public static final Font _16_PT_PLAIN = new Font("",Font.PLAIN,16);
    public static final Font _32_PT_PLAIN = new Font("",Font.PLAIN,32);
    public static final Font _64_PT_PLAIN = new Font("",Font.PLAIN,64);
    public static final Font _128_PT_PLAIN = new Font("",Font.PLAIN,128);
    public Font(String name, int style, int size)
    {
        super(name,style,size);
    }
    public Font(int style, int size)
    {
        this("",style,size);
    }
    public Font(int size)
    {
        this(DEFAULT_STYLE,size);
    }
    
}
