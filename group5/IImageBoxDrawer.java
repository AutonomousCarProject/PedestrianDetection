/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group5;

import group1.IImage;
import group1.IPixel;
import group2.IBlobDetection;
import group2.Blob;
import group3.MovingBlob;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Create only ONE IImageBoxDrawer and as long as you send all draw(..) method 
 * calls to this one object, you should be fine. If you create a million IImage 
 * Box Drawers, you're fked because each one is a jframe
 * @author peter_000
 */
public class IImageBoxDrawer implements IImageDrawing
{
    public static final int DEFAULT_LINE_THICKNESS = 3;
    public static final Color DEFAULT_LINE_COLOR = Color.YELLOW;
    
    private BufferedImage currentImage;
    
    public BufferedImage getCurrentImage()
    {
        return currentImage;
    }
    
    
    public void draw(IImage image, List<MovingBlob> iBlobs)
    {
        Rectangle[] rectangles = findRectangles(image,iBlobs);
        BufferedImage b = new BufferedImage(image.getImage().length,image.getImage()[0].length,BufferedImage.TYPE_INT_ARGB);
        setPixels(b,image.getImage());
        drawLines(rectangles,b,DEFAULT_LINE_COLOR,DEFAULT_LINE_THICKNESS);
        currentImage = b;
    }
    protected Rectangle[] findRectangles(IImage image, List<MovingBlob> iBlobs)
    {
        //...
        Rectangle[] rectangles = new Rectangle[iBlobs.size()];
        for(int i = 0; i < iBlobs.size(); i++)
        {
            Blob b = iBlobs.get(i);
            Point[] points = new Point[4];
            points[0] = new Point(b.centerX-b.width/2,b.centerY-b.height/2);
            points[1] = new Point(b.centerX+b.width/2,b.centerY-b.height/2);
            points[2] = new Point(b.centerX+b.width/2,b.centerY+b.height/2);
            points[3] = new Point(b.centerX-b.width/2,b.centerY+b.height/2);
            rectangles[i] = new Rectangle(points);
        }
        return rectangles;
    }
    protected void setPixels(BufferedImage b, IPixel[][] pixels)
    {
        //int[] pixelColors1D = new int[pixels.length * pixels[0].length];
        //int i = 0;
        for(int r = 0; r < pixels.length; r++)
        {
            for(int c = 0; c < pixels[0].length; c++)
            {
                try
                {
                    short red = pixels[r][c].getRed();
                    short green = pixels[r][c].getGreen();
                    short blue = pixels[r][c].getBlue();

                    int p = (255/*alpha*/ << 24) | (red << 16) | (green << 8) | blue;
                    b.setRGB(r,c,p);
                }
                catch(ArrayIndexOutOfBoundsException e)
                {
                    
                }
                
                
            }
        }
        
    }
    protected void drawLines(Rectangle[] rects, BufferedImage image, Color lineColor, int lineThickness)
    {
        Graphics2D g = (Graphics2D)image.getGraphics();
        BasicStroke bs = new BasicStroke(lineThickness);
        for(Rectangle r : rects)
        {
            for(int i = 0; i < 4; i++)
            {
                int indexOfNextPoint;
                if(i == 3)
                {
                    indexOfNextPoint = 0;
                }
                else
                {
                    indexOfNextPoint = i+1;
                }
                Point start = r.getPoints()[i];
                Point end = r.getPoints()[indexOfNextPoint];
                
                g.setStroke(bs);
                g.setColor(lineColor);
                g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
            }
        }
    }
    
    
    public class Point
    {
        private float x,y;
        public Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
        public float getX(){return x;}
        public float getY(){return y;}
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