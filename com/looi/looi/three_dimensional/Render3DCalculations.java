/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

import com.looi.looi.Calculations;
import static com.looi.looi.Calculations.cos;
import static com.looi.looi.Calculations.sin;

/**
 * These methods allow a double[3] to act as a Point3D. We want to use double[3] because it is faster to create many arrays than to create many 
 * Point3D objects
 * Also, this class contains other useful static methods that help with 3D rendering
 * @author peter_000
 */
public class Render3DCalculations 
{
    /**
     * Precondition: p1 and p2 are both double[3]s
     * @param p1 Point 1
     * @param p2 Point 2
     */
    public static double getHorizontalDistance(double[] p1, double[] p2)
    {
        return Math.sqrt((p1[0]-p2[0])*(p1[0]-p2[0]) + (p1[2]-p2[2])*(p1[2]-p2[2]));
    }
    public static double getHorizontalDirection(double[] p1, double[] p2)
    {
        if(p1[0] == p2[0])
        {
            if(p1[2] < p2[2])
            {
                return 90;
            }
            else
            {
                return 270;
            }
        }
        double answer = Math.toDegrees(Math.atan((p2[2] - p1[2])/(p2[0] - p1[0])));
        if(p2[0] < p1[0])
        {
            answer = answer + 180;
        }
        return answer;
        
    }
    
    /**
     * Rotate double cylinder. One cylinder is vertical. One is horizontal, and 
     * surrounds the x-axis
     * @param rotatingPoint A 3d point
     * @param center Another 3d point
     * @param hr
     * @param vr
     * @return 
     */
    public static double[] rotateDoubleCylinder(double[] rotatingPoint, double[] center, double hr, double vr)
    {
        double horDistance = getHorizontalDistance(rotatingPoint,center);
        
        double oldHR = getHorizontalDirection(center,rotatingPoint);
        double newHR = oldHR + hr;
        double newX = center[0] + cos(newHR)*horDistance;
        double newZ = center[2] + sin(newHR)*horDistance;
        //Point3D horizontalTransformation = new Point3D(newX,rotatingPoint[1],newZ);

        //Point3D pointOnXAxisInLine = new Point3D(newX,center[1],center[2]);
        double distance2 = Math.sqrt((rotatingPoint[1] - center[1])*(rotatingPoint[1] - center[1]) + (newZ - center[2])*(newZ - center[2]));
        double oldCylVr;
        if(center[2] == newZ)
        {
            if(center[1] > rotatingPoint[1])
            {
                oldCylVr = 90;
            }
            else
            {
                oldCylVr = 270;
            }
        }
        else
        {
            oldCylVr = Math.toDegrees(Math.atan((center[1] - rotatingPoint[1])/(Math.abs(center[2] - newZ)))); 
        }
        
        
        // the horizontalTransformation Point is behind us, make sure the angle indicates that
        if(newZ < center[2])
        {
            oldCylVr = -(oldCylVr+180);
        }
        double newCylVr = oldCylVr + vr;
        double newZ2 = center[2] + cos(newCylVr)*distance2;
        double newY = center[1] - sin(newCylVr)*distance2;
        /*
        System.out.println("x " + newX);
        System.out.println("y " + newY);
        System.out.println("z " + newZ2);
        System.out.println(hr);
        */
        return new double[] {newX,newY,newZ2};
    }
    public static double[] rotateAroundZAxis(double[] point, double rotation)
    {
        double distanceFromAxis = Math.sqrt(point[0]*point[0] + point[1]*point[1]);
        double oldAngle;
        if(point[0] == 0)
        {
            if(point[1] > 0)
            {
                oldAngle = 270;
            }
            else
            {
                oldAngle = 90;
            }  
        }
        else
        {
            if(point[0] > 0)
            {
                oldAngle = Math.toDegrees(Math.atan((-point[1])/(point[0])));
            }
            else
            {
                oldAngle = Math.toDegrees(Math.atan((-point[1])/(point[0]))) + 180;
            }
        }
        double newAngle = oldAngle + rotation;
        double newY = -Calculations.sin(newAngle) * distanceFromAxis;
        double newX = Calculations.cos(newAngle) * distanceFromAxis;
        
        return new double[] {newX,newY,point[2]};
    }
    public static double divide3D(double scaleDepth, double z)//divides a by b, and then takes absolute value
    {
        double minimumDivisor = .03;
        if(z > minimumDivisor)
        {
            return scaleDepth/z;
        }
        return scaleDepth/minimumDivisor;
            
    }
}
