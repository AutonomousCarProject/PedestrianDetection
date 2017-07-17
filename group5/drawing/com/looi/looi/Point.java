/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi;

import java.io.Serializable;

/**
 * This class represents a point with an x and a y coordinate in 2D space
 * @author peter_000
 */
public class Point implements Serializable
{
    private double x,y;
        public Point(double x, double y)
        {
            setXYZ(x,y);
        }
        public Point(double... coordinates)
        {
            this(coordinates[0],coordinates[1]);
        }
        public void setXYZ(double x, double y)
        {
            this.x = x;
            this.y = y;
        }
        public void setXYZ(double... coordinates)
        {
            setXYZ(coordinates[0],coordinates[1]);
        }
        public void setX(double x){this.x = x;}
        public void setY(double y){this.y = y;}
        public void move(double xInc, double yInc)
        {
            x += xInc;
            y += yInc;
        }
        
        
        public double[] getXY(){return new double[] {x,y};}
        public double getX(){return x;}
        public double getY(){return y;}
        
        
        
        
        
        
        
        public boolean insideConvexSpace(Point...others)
        {
            double[] angles = new double[others.length];
            //find all the angles
            for(int i = 0; i < others.length; i++)
                {
                    angles[i] = Calculations.simplifyAngle(this.getCartesianDirection(others[i]));
                }
            //sort array
            angles = Calculations.mergeSort(angles);
            
            //Check if two adjacent angles are more than 180 apart
            for(int i = 0; i < angles.length - 1; i++)
                {
                    if(angles[i] + 180 < angles[i + 1])
                        {
                            // if two adjacent angles are more than 180 apart then the point in question is outside
                            return false;
                        }
                }
            //check last to first angles, which are also adjacent, but they wont be covered in the for loop so ill check them now
            if(angles[angles.length-1]-360+180 < angles[0] )
                {
                    return false;
                }
            return true;
        }
        public Point middle(Point other)
        {
            double x = (this.x + other.x)/2.0;
            double y = (this.y + other.y)/2.0;
            return new Point(x,y);
        }
        public double getGraphicDirection(Point other)
        {
            if(this.x == other.x)
                {
                    if(this.y == other.y)
                        return 0;
                    if(this.y > other.y)
                        return 90;
                    if(this.y < other.y)
                        return 270;
                }
            double answerInRadians = Math.atan(-(other.y-this.y)/(other.x-this.x));
            double answerInDegrees = Math.toDegrees(answerInRadians);

            if(other.x < this.x)
                {
                    answerInDegrees += 180;
                }
            answerInDegrees = Calculations.simplifyAngle(answerInDegrees);
            return answerInDegrees;
        }
        public double getCartesianDirection(Point other)
        {
            double direction = -getGraphicDirection(other);
            direction = Calculations.simplifyAngle(direction);
            return direction;
        }
        public double getDistance(Point other)
        {
            return Math.sqrt(Math.pow(other.x-x,2) + Math.pow(other.y-y,2));
        }
        public Point moveTowards(Point other, double distance)
        {
            double direction = getCartesianDirection(other);
            double xResulting = x + Calculations.cos(direction)*distance;
            double yResulting = y + Calculations.sin(direction)*distance;
            return new Point(xResulting,yResulting);
        }
        public Point clone()
        {
            return new Point(x,y);
        }
        public String toString()
        {
            return "Point... x = " + x + ", y = " + y;
        }
        public static Point getCenter(Point...points)
        {
            double totalX = 0;
            double totalY = 0;
            for(Point point : points)
                {
                    totalX += point.getX();
                    totalY += point.getY();
                }
            double avgX = totalX/points.length;
            double avgY = totalY/points.length;
            return new Point(avgX,avgY);
        }
}
