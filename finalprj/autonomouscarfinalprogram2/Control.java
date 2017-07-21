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

    private static double getPhotoshopRGBVar(double rvar, double gvar, double bvar, double rmean, double gmean, double bmean){
    	return  (1.0 / 3.0) * (rvar + gvar + bvar)
			    + (2.0 / 9.0) * (rmean * rmean + gmean * gmean + bmean * bmean)
			    + (2.0 / 9.0) * (rmean * gmean + rmean * bmean + gmean * bmean);
    	//what the actual fuck
    }

    private static double getPhotoshopLumaVar(double rvar, double gvar, double bvar){
    	return 0.299 * 0.299 * rvar + 0.578 * 0.578 * gvar + 0.114 * 0.114 * bvar;
    	//seriously, this is almost too easy but how
    }

    private static double getMagicVar(double rvar, double gvar, double bvar, double rmean, double gmean, double bmean){
    	return  0.299 * rvar + 0.587 * gvar + 0.114 * bvar
			    + (2.0 / 3.0) * (0.299 * rmean * rmean + 0.587 * gmean * gmean + 0.114 * bmean * bmean)
			    - (2.0 / 3.0) * (0.486 * rmean * gmean + 0.214 * rmean * bmean + 0.300 * gmean * bmean);
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
        final double thresh = 15000;

        IPixel[][] image = currentImage.getImage();
        List<int[]> indexes = new LinkedList<>();

        long start = System.currentTimeMillis();

        for (int i = 0; i < image.length - box_size; i += box_increment){
            for (int j = 0; j < image[0].length - box_size; j += box_increment){
                //calculate variance
                double rmean = 0;
                double rvar = 0;
                double gmean = 0;
                double gvar = 0;
                double bmean = 0;
                double bvar= 0;

                for (int k = i; k < i + box_size; k++){
                    for (int l = j; l < j + box_size; l++){
                        rmean += image[k][l].getRed();
                        rvar += image[k][l].getRed() * image[k][l].getRed();
                        gmean += image[k][l].getGreen();
                        gvar += image[k][l].getGreen() * image[k][l].getGreen();
                        bmean += image[k][l].getBlue();
                        bvar += image[k][l].getBlue() * image[k][l].getBlue();
                    }
                }

                //compute mean
                rmean /= box_size * box_size;
                gmean /= box_size * box_size;
                bmean /= box_size * box_size;

                //compute varience
                rvar = rvar / (box_size * box_size) - (rmean * rmean);
                gvar = gvar / (box_size * box_size) - (gmean * gmean);
                bvar = bvar / (box_size * box_size) - (bmean * bmean);

                //System.out.println(rvar);

                //magic formulas?
	            double var = getPhotoshopRGBVar(rvar, gvar, bvar, rmean, gmean, bmean);
	            //double var = getPhotoshopLumaVar(rvar, gvar, bvar);
	            //double var = getMagicVar(rvar, gvar, bvar, rmean, bmean, gmean);

	            if(Math.abs(var) < thresh) indexes.add(new int[] {i, j});
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
