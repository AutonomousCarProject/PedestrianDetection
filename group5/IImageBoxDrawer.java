/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//*****
package group5;

import com.looi.looi.LooiWindow;
import com.looi.looi.Point;
import group1.IImage;
import group1.IPixel;
import group2.Blob;
import group3.MovingBlob;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
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
    public static final int DEFAULT_LINE_THICKNESS = 1;
    public static final Color DEFAULT_COLOR = Color.YELLOW;
    public static final double DEFAULT_MAX_VELOCITY = 5;
    
    
    private BufferedImage currentImage;
    private boolean useBasicColors = false;
    private Color rectangleColor;
    private double maxVelocity;
    private boolean drawAdvancedInformation = false;
    private Rectangle[] rectangles = new Rectangle[0];
    private int lineThickness;
    public IImageBoxDrawer(Color rectangleColor, double maxVelocity, int lineThickness)
    {
        this.rectangleColor = rectangleColor;
        this.maxVelocity = maxVelocity;
        this.lineThickness = lineThickness;
    }
    public IImageBoxDrawer()
    {
        this(DEFAULT_COLOR,DEFAULT_MAX_VELOCITY,DEFAULT_LINE_THICKNESS);
    }
    public boolean isDrawingAdvancedInformation()
    {
        return drawAdvancedInformation;
    }
    public void setDrawAdvancedInformation(boolean b)
    {
        drawAdvancedInformation = b;
    }
    public BufferedImage getCurrentImage()
    {
        return currentImage;
    }
    
    public boolean isUsingBasicColors()
    {
        return useBasicColors;
    }
    public void setUsingBasicColors(boolean b)
    {
        useBasicColors = b;
    }
    public Color getRectangleColor(){return rectangleColor;}
    public void blobsToRectangles(IImage image, List<? extends Blob> iBlobs)
    {
        rectangles = findRectangles(image,iBlobs);
    }
    public void draw(IImage image)
    {
        
        BufferedImage b = new BufferedImage(image.getImage()[0].length,image.getImage().length,BufferedImage.TYPE_INT_ARGB);
        setPixels(b,image.getImage());
        
        
        drawLines(rectangles,b);
        currentImage = b;
    }
    public void draw2(IImage image, List<MovingBlob> iBlobs,List<MovingBlob> iBlobs2)
    {
    	 Rectangle[] rectangles = findRectangles(image,iBlobs);
    	 Rectangle[] rectangles2 = findRectangles(image, iBlobs2);
         BufferedImage b = new BufferedImage(image.getImage()[0].length,image.getImage().length,BufferedImage.TYPE_INT_ARGB);
         setPixels(b,image.getImage());
         
         
         drawLines(rectangles,b);
         drawLines(rectangles2, b);
         currentImage = b;
    }
    protected Color findColor(Color min, Color max, double percent)
    {
        int deltaRed = max.getRed() - min.getRed();
        int deltaGreen = max.getGreen() - min.getGreen();
        int deltaBlue = max.getBlue() - min.getBlue();
        
        int changeRed = (int)(deltaRed * percent);
        int changeGreen = (int)(deltaGreen * percent);
        int changeBlue = (int)(deltaBlue * percent);
        
        int newRed = min.getRed() + changeRed;
        int newGreen = min.getGreen() + changeGreen;
        int newBlue = min.getBlue() + changeBlue;
        
        if(newRed > 255)newRed = 255;
        if(newRed < 0) newRed = 0;
        if(newGreen > 255)newGreen = 255;
        if(newGreen < 0) newGreen = 0;
        if(newBlue > 255)newBlue = 255;
        if(newBlue < 0) newBlue = 0;
        return new Color(newRed,newGreen,newBlue);
    }
    protected Rectangle[] findRectangles(IImage image, List<? extends Blob> iBlobs)
    {
        //...
        Rectangle[] rectangles = new Rectangle[iBlobs.size()];
        for(int i = 0; i < iBlobs.size(); i++)
        {
            Blob b = iBlobs.get(i);
            Point[] points = new Point[4];

            points[0] = new Point(b.x,b.y);
            points[1] = new Point(b.x+b.width,b.y);
            points[2] = new Point(b.x+b.width,b.y+b.height);
            points[3] = new Point(b.x,b.y+b.height);
            
            rectangles[i] = new Rectangle(points,b,this.rectangleColor,lineThickness);
        }
        return rectangles;
    }
    
    public int getLineThickness()
    {
        return lineThickness;
    }
    public Rectangle[] getRectangles()
    {
        return rectangles;
    }
    protected void setPixels(BufferedImage b, IPixel[][] pixels)
    {
        //int[] pixelColors1D = new int[pixels.length * pixels[0].length];
        //int i = 0;
        //System.out.println("rows: " + pixels.length + " cols: " + pixels[0].length);
        //System.out.println("BI: rows: " + b.getHeight() + );
        for(int r = 0; r < pixels.length; r++)
        {
            if(useBasicColors)
            {
                for(int c = 0; c < pixels[0].length; c++)
                {

                    try
                    {

                        int colorNumber = pixels[r][c].getColor();
                        Color theColor;
                        if(colorNumber == 0)
                        {
                            theColor = Color.RED;
                        }
                        else if(colorNumber == 1)
                        {
                            theColor = Color.GREEN;
                        }
                        else if(colorNumber == 2)
                        {
                            theColor = Color.BLUE;
                        }
                        else if(colorNumber == 3)
                        {
                            theColor = Color.GRAY;
                        }
                        else if(colorNumber == 4)
                        {
                            theColor = Color.BLACK;
                        }
                        else if(colorNumber == 5)
                        {
                            theColor = Color.WHITE;
                        }
                        else
                        {
                            throw new RuntimeException()
                            {
                                public String toString()
                                {
                                    return "you are stupid";
                                }
                            };
                        }



                        int p = (255/*alpha*/ << 24) | (theColor.getRed() << 16) | (theColor.getGreen() << 8) | theColor.getBlue();
                        b.setRGB(c,r,p);
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {

                    }


                }
            }
            else
            {
                for(int c = 0; c < pixels[0].length; c++)
                {

                    try
                    {


                        short red = pixels[r][c].getRed();
                        short green = pixels[r][c].getGreen();
                        short blue = pixels[r][c].getBlue();



                        int p = (255/*alpha*/ << 24) | (red << 16) | (green << 8) | blue;
                        b.setRGB(c,r,p);
                    }
                    catch(ArrayIndexOutOfBoundsException e)
                    {

                    }


                }
            }
            
        }
        
    }
    protected void drawLines(Rectangle[] rects, BufferedImage image)
    {
        Graphics2D g = (Graphics2D)image.getGraphics();
        
        for(Rectangle r : rects)
        {
            BasicStroke bs = new BasicStroke(r.getThickness());
            g.setFont(new Font("",Font.PLAIN,10));
            if(drawAdvancedInformation)
            {
                g.setColor(Color.BLACK); 
                g.drawString("Age: " + r.getMovingBlob().age+"",(int)(r.getPoints()[0].getX()-2),(int)(r.getPoints()[0].getY() - 2));
                g.setColor(Color.BLACK); 
                g.drawString("AgeOffScreen: " + r.getMovingBlob().ageOffScreen+"",(int)(r.getPoints()[0].getX()-2),(int)(r.getPoints()[0].getY() - 12));
            }
            
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
                
                double velocity = Math.sqrt( (r.getMovingBlob().velocityX)*(r.getMovingBlob().velocityX) + (r.getMovingBlob().velocityY)*(r.getMovingBlob().velocityY) );
                
                //Color lineColor = findColor(still,moving,r.getBlob().age/maxVelocity);
                Color lineColor = r.getColor();
                
                g.setColor(lineColor);
                g.drawLine((int)start.getX(),(int)start.getY(),(int)end.getX(),(int)end.getY());
                
            }
        }
    }
    
    
    /*public class Point
    {
        private float x,y;
        public Point(float x, float y)
        {
            this.x = x;
            this.y = y;
        }
        public float getX(){return x;}
        public float getY(){return y;}
    }*/
    public class Rectangle
    {
        private Point[] points;
        private Blob b;
        private Color color;
        private int thickness;
        public Rectangle(Point[] points, Blob b, Color color, int thickness)
        {
            this.points = points;
            this.b = b;
            this.color = color;
            this. thickness = thickness;
        }
        public Point[] getPoints()
        {
            return points;
        }
        public Blob getBlob()
        {
            
            return b;
        }
        public MovingBlob getMovingBlob()
        {
            if(b instanceof MovingBlob)
                return (MovingBlob)b;
            return new MovingBlob(b);
        }
        public int getThickness()
        {
            return thickness;
        }
        public void setThickness(int thickness)
        {
            this.thickness = thickness;
        }
        public Color getColor()
        {
            return color;
        }
        public void setColor(Color c)
        {
            this.color = c;
        }
        public Point[] getScaledPoints()
        {
            
            Point[] points = new Point[this.points.length];
            for(int i = 0; i < this.points.length; i++)
            {
                points[i] = new Point(this.points[i].getX()/currentImage.getWidth() * LooiWindow.getMainWindow().getInternalWidth(),this.points[i].getY()/currentImage.getHeight() * LooiWindow.getMainWindow().getInternalHeight());
                //System.out.println(this.points[i].getX()/currentImage.getWidth()*LooiWindow.getMainWindow().getInternalWidth() + " | " + LooiWindow.getMainWindow().getInternalMouseX());
            }
            return points;
        }
    }
    
    
}
