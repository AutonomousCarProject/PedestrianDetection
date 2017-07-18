/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import java.awt.List;

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
    public Control()
    {
        blobDetection = new BlobDetection();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new Image(640,480);
    }
    protected void looiStep()
    {
        currentImage.readCam();
        List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobs);
        boxDrawer.draw(currentImage,filteredBlobs);
        
    }
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
    }
}
