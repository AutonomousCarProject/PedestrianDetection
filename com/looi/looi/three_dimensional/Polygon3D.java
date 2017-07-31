/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

import com.looi.looi.LooiObject;
import com.looi.looi.LooiWindow;
import com.looi.looi.Point;
import com.looi.looi.utilities.Supplier;
import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class Polygon3D extends LooiObject implements Visual
{
    
    
    
    
    
    private Point3D[] points;
    private Point3D center = new Point3D(0,0,0); //Array of XYZ values for the center of the Quadrilateral
    
    private Point3D[] pointsPaint;
    private Point3D centerPaint = new Point3D(0,0,0);
    
    
    private Color c;
    
    
    private Supplier<View> view;
    private View.ReadOnlyView viewPaint;

    /**
     * Creates a default Quadrilateral3D
     * @param view
     */
    public Polygon3D(Supplier<View> view)
        {
            this(Color.BLACK,view,new Point3D(0,0,0),new Point3D(0,0,0),new Point3D(0,0,0));
            center();
        }
    /**
     * Creates Quadrilateral3D with default window and activation
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param c 
     */
    public Polygon3D(Color c, Supplier<View> view, Point3D...points)
        {
            this(c,false,view,points);
        }
    /**
     * Creates Quadrilateral 3D with default window
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param c
     * @param activate 
     */
    public Polygon3D(Color c, boolean activate, Supplier<View> view, Point3D...points)
        {
            this(c,LooiWindow.getMainWindow(),activate,view,points);
        }
    /**
     * Creates a new Quadrilateral3D with four specified points and one specified color
     * @param p1 Point one
     * @param p2 Point two
     * @param p3 Point three
     * @param p4 Point four
     * @param c The Color
     */
    public Polygon3D(Color c, LooiWindow w, boolean activate, Supplier<View> view, Point3D...points)
        {
            super(w,activate);
            this.view = view;
            this.points = points.clone();
            this.c = c;
            center();
        }

    /**
     * Paints this at every step
     */
    protected void looiPaint()
        {
            //What this code does: If the Quadrilateral is infront of the player, draw the Quadrilateral.
            
            
            if(centerPaint != null && pointsPaint != null && pointsPaint.length > 2 && centerPaint.relativeTo(viewPaint)[2] > 0)
                {
                    Point[] pointsOnScreen = new Point[pointsPaint.length];
                    for(int i = 0; i < pointsPaint.length; i++)
                        {
                            
                            pointsOnScreen[i] = pointsPaint[i].flattenToScreen(viewPaint,thisWindow());
                            
                        }
                    setColor(c);
                    fillPolygon(pointsOnScreen);
                }
        }
    
    /**
     * Does this at every step
     */
    protected void looiStep()
        {
            center();
            setLayer(view.get().get3DDistance(center));
        }
    protected void savePaintInformation()
        {
            pointsPaint = new Point3D[points.length];
            for(int i = 0; i < points.length; i++)
                {
                    pointsPaint[i] = points[i].clone();
                }
            centerPaint = center.clone();
            viewPaint = view.get().getSnapshot();
        }
    /**
     * Changes the Color of this Quadrilateral3D
     * @param c The desired Color
     */
    public void useColor(Color c)
        {
            this.c = c;
        }
    /**
     * Returns the current color 
     * @return The current color 
     */
    public Color getColor()
        {
            return c;
        }
    /**
     * Finds the center
     */
    public void center()
        {
            double avgX = 0;
            double avgY = 0;
            double avgZ = 0;
            for(Point3D p : points)
                {
                    avgX += p.getX();
                    avgY += p.getY();
                    avgZ += p.getZ();
                }
            avgX /= points.length;
            avgY /= points.length;
            avgZ /= points.length;
            center.setXYZ(avgX,avgY,avgZ);
        }
    /**
     * Changes the four points of this Quadrilateral3D
     * @param p1 Point one (needs an x, y, and z in indices [0], [1], and [2] respectively)
     * @param p2 Point two (needs an x, y, and z in indices [0], [1], and [2] respectively)
     * @param p3 Point three (needs an x, y, and z in indices [0], [1], and [2] respectively)
     * @param p4 Point four (needs an x, y, and z in indices [0], [1], and [2] respectively)
     */
    public void setPoints(Point3D...points)
        {
            this.points = points;
        }
    /**
     * Returns an array of points with the same locations as the points on this
     * Polygon3D. Modifying these points will not modify this 
     * Polygon3D. Modifying the array will not modify this Polygon3D.
     * @return A Point[] containing the points of this Polygon3D
     */
    public Point3D[] viewPoints()
        {
            Point3D[] points = new Point3D[this.points.length];
            for(int i = 0; i < points.length; i++)
                {
                    points[i] = this.points[i].clone();
                }
            return points;
        }
    /**
     * Returns an array that contains the points of this Polygon3D. Modifying 
     * these points will modify the state of this Polygon3D. Modifying the array
     * will not modify this Polygon3D.
     * @return 
     */
    public Point3D[] getPoints()
        {
            return points.clone();
        }
    /**
     * Returns the points of this quadrilateral drawn on-screen
     * @return The points of this quadrilateral drawn on-screen
     */
    public Point[] getPointsOnScreen()
        {
            Point[] pointsOnScreen = new Point[pointsPaint.length];
            for(int i = 0; i < pointsPaint.length; i++)
                {
                    pointsOnScreen[i] = pointsPaint[i].flattenToScreen(viewPaint,thisWindow());
                }
            return pointsOnScreen;    
        }
    public Point3D getCenter()
        {
            return centerPaint.clone();
        }
    public View.ReadOnlyView getCurrentView()
        {
            return viewPaint;
        }
    public Supplier<View> getView()
        {
            return view;
        }
    
    
}
