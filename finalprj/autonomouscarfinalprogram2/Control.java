/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;
import group1.IPixel;
import group1.Pixel;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection3;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.awt.Color;



/**
 *
 * @author peter_000
 */
public class Control extends LooiObject
{
    private BlobDetection3 blobDetection;
    private MovingBlobDetection movingBlobDetection;
    private BlobFilter blobFilter;
    private IImageBoxDrawer boxDrawer;
    private FileImage currentImage;
    private Slider slider;
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
    
    private int previousFrame;
    private int currentFrame;
    public static boolean keepGoing;
    
    private ArrayDeque<IPixel[][]> frames;
    private ArrayList<IPixel[][]> frameList;
    
    public Control(int frameDelay)
    {
        blobDetection = new BlobDetection3();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new FileImage();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);
        slider = new Slider(10,10,300,30,new Background(Color.WHITE));
        
        previousFrame = 0;
        setCurrentFrame(1);
        keepGoing = true;
        
        currentImage.readCam();
        IPixel[][] firstFrame = currentImage.getImage();
        
        frames = new ArrayDeque<IPixel[][]>(5);
        
        frames.addFirst(firstFrame);
        
    }
    
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
    	if(keepGoing){
    		updateWhileUnpaused();
    	}  
    	else{
    		updateWhilePaused();
    	}
    }
    
    protected void updateWhileUnpaused(){
    	currentImage.readCam();
	        
        previousFrame++;
        
        if(currentImage.getFrameNo()==previousFrame){
        	previousFrame = 0;
        	currentImage.finish();
            currentImage = new FileImage();
        	blobDetection = new BlobDetection3();
            movingBlobDetection = new MovingBlobDetection();
            blobFilter = new BlobFilter();
            boxDrawer.setUsingBasicColors(true);
            currentImage.readCam();
        }
        
        List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
        
        boxDrawer.draw(currentImage,fmovingBlobs);
        
        IPixel[][] image = currentImage.getImage();
        IPixel[][] copy = new IPixel[image.length][image[0].length];
        for(int i=0;i<image.length;i++){
        	for(int j=0;j<image[0].length;j++){
        		copy[i][j] = image[i][j];
        	}
        }
    	frames.addFirst(copy);
    	
    	if(frames.size() >= 5){
    		frames.removeLast();
    	}
        
    }
    
    public void updateWhilePaused(){
		currentImage.setImage(frameList.get(currentFrame));
		
		List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
        
		boxDrawer.draw(currentImage, fmovingBlobs);
    }
    
    public void incrementCurrentFrame(int i){
    	setCurrentFrame(getCurrentFrame() + i);
    	if(currentFrame == frames.size()){
    		currentFrame = 0;
    	} else if(currentFrame <= 0){
    		currentFrame = frames.size()-1;
    	}
    }
    
    public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int frame){
    	currentFrame = frame;
    }
    
    public void pauseUnpause(){
    	keepGoing = !keepGoing;
    	if(!keepGoing){
    		frameList = new ArrayList<>(frames);
    		currentFrame = 0;
    	}
    }
    
    public boolean getPaused(){
    	return !keepGoing;
    }
    
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
