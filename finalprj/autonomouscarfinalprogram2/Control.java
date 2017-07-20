/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprj.autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import group1.IPixel;
import group1.Image;
import group2.Blob;
import group2.BlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author peter_000
 */
public class Control extends LooiObject
{
    private BlobDetection blobDetection;
    private MovingBlobDetection movingBlobDetection;
    private BlobFilter blobFilter;
    private IImageBoxDrawer boxDrawer;
    private Image currentImage;
    private BufferedImage testBI = new BufferedImage(10,10,BufferedImage.TYPE_INT_ARGB);

    private static class MovingBlobConstruct extends MovingBlob {
        MovingBlobConstruct(int x, int y){
            super();
            this.centerX = x;
            this.centerY = y;
            this.width = 1;
            this.height = 1;
        }
    }

    private int previousFrame;
    {
        for(int r = 0; r < testBI.getHeight(); r++)
        {
            for(int c = 0; c < testBI.getWidth(); c++)
            {
                int p = (255/*alpha*/ << 24) | (255 << 16) | (255 << 8) | 255;
                testBI.setRGB(c,r,p);
            }
        }
    }

    private static double calcHue(int r, int g, int b){
        //staggering beauty
        r /= 256.0;
        g /= 256.0;
        b /= 256.0;
        int max = (r > g) ? ((r > b) ? r : b) : ((g > b) ? g : b);
        int min = (r < g) ? ((r < b) ? r : b) : ((g < b) ? g : b);
        if(r == max) return (g - b) / (max - min);
        if(g == max) return 2.0 + (b - r) / (max - min);
        else return 4.0 + (r - g) / (max - min);
    }

    public Control()
    {
        //blobDetection = new BlobDetection();
        //movingBlobDetection = new MovingBlobDetection();
        //blobFilter = new BlobFilter();
        currentImage = new Image();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);
        
        previousFrame = 0;
    }
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
        currentImage.readCam();
        
        //List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        
        //List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        //System.out.println(movingBlobs.size());
        ///List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobs);

        //do our own thing

        final double box_size = 3;
        final int box_increment = 1;
        final double thresh = 0.10;

        IPixel[][] image = currentImage.getImage();
        List<int[]> indexes = new LinkedList<>();

        long start = System.currentTimeMillis();

        for (int i = 0; i < image.length - box_size; i += box_increment){
            for (int j = 0; j < image[0].length - box_size; j += box_increment){
                //calculate variance
                double rtotal = 0;
                double gtotal = 0;
                double btotal = 0;

                for (int k = i; k < i + box_size; k++){
                    for (int l = j; l < j + box_size; l++){
                        int total = image[k][l].getRed() + image[k][l].getGreen() + image[k][l].getBlue();
                        rtotal += image[k][l].getRed() * 3 - total;
                        gtotal += image[k][l].getGreen() * 3 - total;
                        btotal += image[k][l].getBlue() * 3 - total;
                    }
                }

                rtotal /= box_size * box_size * 3;
                gtotal /= box_size * box_size * 3;
                btotal /= box_size * box_size * 3;

                double distTotal = 0;

                for (int k = i; k < i + box_size; k++){
                    for (int l = j; l < j + box_size; l++){
                        int total = (image[k][l].getRed() + image[k][l].getGreen() + image[k][l].getBlue()) / 3;
                        distTotal += Math.pow(rtotal - (image[k][l].getRed() - total), 2) + Math.pow(gtotal - (image[k][l].getGreen() - total), 2) + Math.pow(btotal - (image[k][l].getBlue() - total), 2);
                    }
                }

                double var = distTotal / (box_size * box_size);
                if(var < thresh) indexes.add(new int[] {i, j});
            }
        }

        long end = System.currentTimeMillis();

        System.out.println("Took: " + (end - start));

        List<MovingBlob> ray = new LinkedList<MovingBlob>();
        for(int i = 0; i < indexes.size(); i++){
            int[] thing = indexes.get(i);
            ray.add(new MovingBlobConstruct(thing[0], thing[1]));
        }

        boxDrawer.draw(currentImage, ray);
        
        previousFrame++;
        
        if(currentImage.getFrameNo()==previousFrame){
        	previousFrame = -1;
        	currentImage.finish();
            currentImage = new Image();
        	//blobDetection = new BlobDetection();
            //movingBlobDetection = new MovingBlobDetection();
            //blobFilter = new BlobFilter();
            boxDrawer.setUsingBasicColors(false);
        }
        
        
    }
    
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
