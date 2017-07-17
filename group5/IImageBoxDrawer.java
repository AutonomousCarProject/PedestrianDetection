/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group5;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

/**
 *
 * @author peter_000
 */
public class IImageBoxDrawer extends JFrame
{
    public static final int DEFAULT_LINE_THICKNESS = 10;
    public static final Color DEFAULT_LINE_COLOR = Color.YELLOW;
    
    private BufferedImage currentImage;
    public IImageBoxDrawer()
    {
        
    }
    
    
    public void draw(IImage image, IBlobDetection iBlobs)
    {
        Rectangle[] rectangles = findRectangles(image,iBlobs);
        BufferedImage b = new BufferedImage(image.getYUVImage().length,image.getYUVImage()[0].length,BufferedImage.TYPE_INT_ARGB);
        setPixels(b,image.getYUVImage());
        drawLines(rectangles,b,DEFAULT_LINE_COLOR,DEFAULT_LINE_THICKNESS);
        currentImage = b;
    }
    protected Rectangle[] findRectangles(IImage image, IBlobDetection iBlobs)
    {
        //...
        
    }
    protected void setPixels(BufferedImage b, IPixel[][] pixels)
    {
        int[] pixelColors1D = new int[pixels.length * pixels[0].length];
        int i = 0;
        for(int r = 0; r < pixels.length; r++)
        {
            for(int c = 0; c < pixels[0].length; c++)
            {
                short red = pixels[r][c].getRed();
                short green = pixels[r][c].getGreen();
                short blue = pixels[r][c].getBlue();
                
                int p = (255/*alpha*/ << 24) | (red << 16) | (green << 8) | blue;
                pixelColors1D[i] = p;
                
                i++;
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
                g.drawLine(start.getX(),start.getY(),end.getX(),end.getY());
                
                
            }
        }
    }
    protected void drawHorizontalLine(IPixel[][] iPixels, Point start, int distance, int thickness, Color color)
    {
        int topY = start.getY() - thickness/2;
        int botY = start.getY() + thickness/2;
        for(int y = topY; y < botY + 1; y++)
        {
            for(int x = start.getX(); x < start.getX() + distance + 1; x++)
            {
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                
                iPixels[y][x] = new IPixel()
                {
                    @Override
                    public short getLuma() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public short getSaturation() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public short getColor() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public short getRed() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public short getGreen() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public short getBlue() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    
                };
            }
        }
           
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
