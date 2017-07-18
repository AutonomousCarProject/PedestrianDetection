/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

import static com.looi.looi.Calculations.cos;
import static com.looi.looi.Calculations.sin;
import com.looi.looi.LooiWindow;
import com.looi.looi.Point;
import java.io.Serializable;

/**
 *
 * @author peter_000
 */
public class Point3D implements Serializable
{
    private double x,y,z;
    public Point3D(double x, double y, double z)
    {
        setXYZ(x,y,z);
    }
    public Point3D(double[] coordinates)
    {
        this(coordinates[0],coordinates[1],coordinates[2]);
    }
    public void setXYZ(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public void setXYZ(double[] coordinates)
    {
        setXYZ(coordinates[0],coordinates[1],coordinates[2]);
    }
    public void setXYZ(Point3D p)
    {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }
    public void setX(double x){this.x = x;}
    public void setY(double y){this.y = y;}
    public void setZ(double z){this.z = z;}
    public void move(double xInc, double yInc, double zInc)
    {
        x += xInc;
        y += yInc;
        z += zInc;
    }
    
    //no methods below this point modify state
    
    
    public Point3D copyMove(double xInc, double yInc, double zInc)
    {
        return new Point3D(x + xInc,y + yInc,z + zInc);
    }


    public double[] getXYZ(){return new double[] {x,y,z};}
    public double getX(){return x;}
    public double getY(){return y;}
    public double getZ(){return z;}





    public Point3D middle(Point3D other)
    {
        double x = (this.x + other.x)/2.0;
        double y = (this.y + other.y)/2.0;
        double z = (this.z + other.z)/2.0;
        return new Point3D(x,y,z);
    }
    /**
     * Brings the two points to the same horizontal plane - returns the horizontal direction in the plane, where z gets bigger as you go forward. Horizontal angles are the unit circle looking at the horizontal plane from above
     * @param other This point points to Point other.
     * @return the horizontal direction
     */
    public double getHorizontalDirection(Point3D other)
    {
        Point me = new Point(x,z);
        Point otr = new Point(other.x,other.z);
        return me.getCartesianDirection(otr);
    }
    /**
     * returns vertical direction assuming that y is more negative as you go up (flipped, graphical)
     * @param other
     * @return 
     */
    public double getVerticalDirection(Point3D other)
    {
        double horizontalDistance = getHorizontalDistance(other);
        double verticalDistance = other.y-y;//flipped, graphical
        Point otr = new Point(horizontalDistance,verticalDistance);
        return new Point(0,0).getGraphicDirection(otr);
    }
    public double getHorizontalDistance(Point3D other)
    {
        Point me = new Point(x,z);
        Point otr = new Point(other.x,other.z);
        return me.getDistance(otr);
    }
    public double get3DDistance(Point3D other)
    {
        return Math.sqrt(Math.pow(other.x-x,2) + Math.pow(other.y-y,2) + Math.pow(other.z-z,2));
    }
    public Point3D moveTowards3D(Point3D other, double distance)
    {
        double hr = getHorizontalDirection(other);
        double vr = getVerticalDirection(other);

        double x = this.x + distance*cos(hr)*cos(vr);
        double z = this.z + distance*sin(hr)*cos(vr);
        double y = this.y - distance*sin(vr);

        return new Point3D(x,y,z);
    }
    /**
     * Imagines this point and the other point on a plane at y = 0. Then, moves this point towards the other point
     * @param distance
     * @param other
     * @return 
     */
    public Point3D moveTowardsHorizontal(Point3D other, double distance)
    {
        Point me = new Point(x,z);
        Point otr = new Point(other.x,other.z);

        Point result = me.moveTowards(otr,distance);
        Point3D result3D = new Point3D(result.getX(),0,result.getY());
        return result3D;
    }
    public Point3D rotateDoubleCylinder(Point3D center, double hr, double vr)
    {
        double horDistance = center.getHorizontalDistance(this);
        double oldHR = center.getHorizontalDirection(this);
        double newHR = oldHR + hr;
        double newX = center.x + cos(newHR)*horDistance;
        double newZ = center.z + sin(newHR)*horDistance;
        Point3D horizontalTransformation = new Point3D(newX,y,newZ);

        Point3D pointOnXAxisInLine = new Point3D(newX,center.y,center.z);
        double distance2 = pointOnXAxisInLine.get3DDistance(horizontalTransformation);
        double oldCylVr = pointOnXAxisInLine.getVerticalDirection(horizontalTransformation);
        // the horizontalTransformation Point is behind us, make sure the angle indicates that
        if(horizontalTransformation.z < pointOnXAxisInLine.z)
        {
            oldCylVr = -(oldCylVr+180);
        }
        double newCylVr = oldCylVr + vr;
        double newZ2 = pointOnXAxisInLine.z + cos(newCylVr)*distance2;
        double newY = pointOnXAxisInLine.y - sin(newCylVr)*distance2;
        
        return new Point3D(newX,newY,newZ2);
    }
    
    public Point3D clone()
    {
        return new Point3D(x,y,z);
    }
    
    public double[] relativeTo(View view)
    {
        double x = this.x;
        double y = this.y;
        double z = this.z;
        
        x -= view.getX();
        y -= view.getY();
        z -= view.getZ();
        
        double[] newPoint = Render3DCalculations.rotateDoubleCylinder(new double[] {x,y,z},new double[] {0,0,0},-view.getHR()+90,-view.getVR());
        if(view.getOR() > 0)
        {
            newPoint = Render3DCalculations.rotateAroundZAxis(newPoint,-view.getOR());
        }
        //return new Point3D(newPoint);
        return newPoint;
    }
    public Point flattenToScreen(View view, LooiWindow window)
    {
        double x = this.x;
        double y = this.y;
        double z = this.z;
        
        
        x -= view.getX();
        y -= view.getY();
        z -= view.getZ();
        
        double[] newPoint = Render3DCalculations.rotateDoubleCylinder(new double[] {x,y,z},new double[] {0,0,0},-view.getHR()+90,-view.getVR());
        if(view.getOR() > 0)
        {
            newPoint = Render3DCalculations.rotateAroundZAxis(newPoint,-view.getOR());
        }
        
        double screenX = newPoint[0];
        double screenY = newPoint[1];
        
        
        screenX *= Render3DCalculations.divide3D(view.getScaleDepth(),newPoint[2]);
        screenY *= Render3DCalculations.divide3D(view.getScaleDepth(),newPoint[2]);
        screenX += window.getInternalWidth()/2;
        screenY += window.getInternalHeight()/2;
        
        return new Point(screenX,screenY);
    }
    public String toString()
    {
        return "Point3D... x = " + x + ", y = " + y + ", z = " + z;
    }
    public static Point3D getCenter(Point3D...points)
    {
        double totalX = 0;
        double totalY = 0;
        double totalZ = 0;
        for(Point3D point : points)
            {
                totalX += point.getX();
                totalY += point.getY();
                totalZ += point.getZ();
            }
        double avgX = totalX/points.length;
        double avgY = totalY/points.length;
        double avgZ = totalZ/points.length;
        return new Point3D(avgX,avgY,avgZ);
    }
    

}
