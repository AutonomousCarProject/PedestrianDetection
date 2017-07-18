/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package group5;

import group1.IImage;
import group1.IPixel;
import group2.IBlobDetection;

/**
 *
 * @author peter_000
 */
public class Group5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    public static IImage drawBoxes(IImage image, IBlobDetection iBlobs)
    {
        Point[] corners = findFourCorners(image,iBlobs);
        return drawLines(corners,image);
        //find the four corners
    }
    public static Point[] findFourCorners(IImage image, IBlobDetection iBlobs)
    {
        return new Point[] {};
    }
    
    public static class Point
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
    public static IImage drawLines(Point[] corners, IImage image)
    {
        return new IImage() {
            @Override
            public IPixel[][] getImage() {
                return new IPixel[0][];
            }
        };
    }
    
}
