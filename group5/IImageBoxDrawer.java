/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group5;

import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class IImageBoxDrawer 
{
    public void draw(IImage image, IBlobDetection iBlobs)
    {
        
    }
    protected Rectangle[] findRectangles(IImage image, IBlobDetection iBlobs)
    {
        //...
    }
    protected IImage drawLines(Rectangle[] corners, IImage image, Color lineColor, int lineThickness)
    {
        
    }
            
            
    public class Point
    {
        private int x,y;
        public Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
        public int getX(){return x;}
        public int getY(){return y;}
    }
    public class Rectangle
    {
        private Point[] points;
        public Rectangle(Point[] points)
        {
            this.points = points;
        }
        public Point[] getPoints()
        {
            return points;
        }
    }
    
    
}
