/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi;

import java.awt.Color;
import java.io.Serializable;

/**
 * Texture is a color pattern that can be used when drawing various shapes. Textures can be used with 3D objects.
 * @author peter_000
 */
public class Texture implements Serializable
    {
        public static final Texture RED_WHITE_STRIPES = new Texture(new Color[][] {
                                                                    new Color[]{Color.white,Color.white,Color.white,Color.white,Color.white},
                                                                    new Color[]{Color.red,Color.red,Color.red,Color.red,Color.red},
                                                                    new Color[]{Color.white,Color.white,Color.white,Color.white,Color.white},
                                                                    new Color[]{Color.red,Color.red,Color.red,Color.red,Color.red},
                                                                    new Color[]{Color.white,Color.white,Color.white,Color.white,Color.white}
                                                                    });
        public static final Texture GRAY_GRADIENT = new Texture(new Color[][]{})
        {
            {
                Color[][] temp = new Color[5][5];
                for(int o = 0; o < 5; o++)
                {

                    for(int i = 0; i < 5; i++)
                        {
                            temp[o][i] = new Color(o*i*15,o*i*15,o*i*15);
                        }
                }
                setTexture(temp);
            }
            
        };

        
        private Color[][] colors;
        
        /**
         * Creates a new Texture out of an array of Colors
         * @param colors The array of Colors used
         */
        public Texture(Color[][] colors)
            {
                try
                {
                    this.colors = new Color[colors.length][colors[0].length];
                    for(int o = 0; o < colors.length; o++)//o stands for outer and i stands for inner. Outer is rows though, and inner is columns, when we eventually display the texture
                        {

                            for(int i = 0; i < colors[0].length; i++)
                                {
                                    this.colors[o][i] = colors[o][i];
                                }
                        }
                }
                catch(NullPointerException | ArrayIndexOutOfBoundsException e)
                {
                    this.colors = new Color[][]{};
                }
                
            }
        
        /**
         * Sets this Texture to a new pattern of colors
         * @param colors The new pattern of colors to be used
         */
        public void setTexture(Color[][] colors)
            {
                this.colors = new Color[colors.length][colors[0].length];
                for(int o = 0; o < colors.length; o++)//o stands for outer and i stands for inner. Outer is rows though, and inner is columns, when we eventually display the texture
                    {
                        
                        for(int i = 0; i < colors[0].length; i++)
                            {
                                this.colors[o][i] = colors[o][i];
                            }
                    }
            }
        
        /**
         * Returns the Colors of this Texture in the form of a Color[][]
         * @return The Texture in the form of a Color[][]
         */
        public Color[][] getColors()
            {
                return colors;
            }
        
        /**
         * Returns the number of rows
         * @return The number of rows
         */
        public int rows()
            {
                return colors.length;
            }
        /**
         * Returns the number of columns
         * @return The number of columns
         */
        public int columns()
            {
                return colors[0].length;
            }
    }