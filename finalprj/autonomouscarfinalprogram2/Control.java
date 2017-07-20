/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
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
    public Control()
    {
        blobDetection = new BlobDetection();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new Image();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);
    }
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
        currentImage.readCam();
        
        List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        //System.out.println(movingBlobs.size());
        List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobs);
        boxDrawer.draw(currentImage,filteredBlobs);
        
        System.out.println(currentImage.getFrameNo());
        if(currentImage.getFrameNo() == 8){
        	currentImage.finish();
        	blobDetection = new BlobDetection();
            movingBlobDetection = new MovingBlobDetection();
            blobFilter = new BlobFilter();
            currentImage = new Image();
            boxDrawer = new IImageBoxDrawer();
            boxDrawer.setUsingBasicColors(true);
        }
        
        
        
    }
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
