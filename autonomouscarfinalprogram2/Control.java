/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//*****
package autonomouscarfinalprogram2;

import ai_data_creation.BlobSelector;
import com.looi.looi.LooiObject;
import com.looi.looi.Point;
import com.looi.looi.gui_essentials.Button;
import com.looi.looi.gui_essentials.Rectangle;
import com.looi.looi.gui_essentials.ScrollBox;
import group1.IPixel;
import com.looi.looi.gui_essentials.Window;

import global.Constant;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseEvent;




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
    private FileImage currentImage;
    
    private Button toggleGraphics;
    private ScrollBox scrollBox;
    private Window sliderWindow;
    
    
    
    
    private int previousFrame;
    private int currentFrame;
    private boolean keepGoing;
    
    private ArrayDeque<IPixel[][]> frames;
    private ArrayList<IPixel[][]> frameList;
    
    private int yCoordinate;
    
    private LoadTextBox ltb;
    
    List<Blob> knownBlobs;
    List<MovingBlob> movingBlobs;
    List<MovingBlob> fmovingBlobs;
    List<MovingBlob> unifiedBlobs;
    
    private Point lassoStart;
    private Point lassoEnd;
    private Point[] lassoSelection;
    
    private BlobSelector blobSelector;
    public Control(int frameDelay, boolean useCamera)
    {
        ArrayList<VariableSlider> variableSliders = new ArrayList<>();
        blobDetection = new BlobDetection();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        if(!useCamera)
        {
            currentImage = new FileImage();
        }
        else
        {
            //currentImage = new Image(1,1,1); 
        }
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);

        previousFrame = 0;
        setCurrentFrame(1);
        keepGoing = true;
        
        currentImage.readCam();
        IPixel[][] firstFrame = currentImage.getImage();
        
        frames = new ArrayDeque<IPixel[][]>(5);
        
        frames.addFirst(firstFrame);
        blobSelector = new BlobSelector(boxDrawer,this);
        
       
        
    }
    
    
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
        if(!getPaused())
        {

            IPixel[][] image = currentImage.getImage();
            IPixel[][] copy = new IPixel[image.length][image[0].length];
            for(int i=0;i<image.length;i++){
                    for(int j=0;j<image[0].length;j++){
                            copy[i][j] = image[i][j];
                    }
            }
        
            frames.addFirst(copy);
    	
            if(frames.size() >= 8){
                    frames.removeLast();
            }

            currentImage.readCam();
            previousFrame++;
        }
    	if(currentImage.getFrameNo()==previousFrame){
        	previousFrame = 0;
        	currentImage.finish();
            currentImage = new FileImage();
        	blobDetection = new BlobDetection();
            movingBlobDetection = new MovingBlobDetection();
            blobFilter = new BlobFilter();
            currentImage.readCam();
        }
        
        if(!getPaused())
        {
            knownBlobs = blobDetection.getBlobs(currentImage);
            movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
            fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
            unifiedBlobs = movingBlobDetection.getUnifiedBlobs(fmovingBlobs);
            //List<MovingBlob> funifiedBlobs = blobFilter.filterUnifiedBlobs(unifiedBlobs);
            //boxDrawer.draw2(currentImage,fmovingBlobs,funifiedBlobs);
            boxDrawer.blobsToRectangles(currentImage, movingBlobs);
        }
        boxDrawer.draw(currentImage, unifiedBlobs);
        
    	
    }
    public List<MovingBlob> getUnifiedBlobs()
    {
        return unifiedBlobs;
    }
    protected void updateWhileUnpaused()
    {
        
    	
        
        
        
    }
    
    /*public void updateWhilePaused(){
		currentImage.setImage(frameList.get(currentFrame));
		
		List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
        List<MovingBlob> unifiedBlobs = movingBlobDetection.getUnifiedBlobs(fmovingBlobs);

		//boxDrawer.draw2(currentImage, movingBlobs, fmovingBlobs);
        boxDrawer.draw2(currentImage, unifiedBlobs, movingBlobs);
	        
    }   */
    
    public void incrementCurrentFrame(int i){
    	setCurrentFrame(getCurrentFrame() + i);
    	if(currentFrame == frames.size()){
    		currentFrame = frames.size()-1;
    	} else if(currentFrame <= 0){
    		currentFrame = 0;
    	}
    }
    
    public int getCurrentFrame() {
		return currentFrame;
	}

    public void setCurrentFrame(int frame){
    	currentFrame = frame;
    }
    /**
     * This methed pauses and unpauses the frame animation
     * This method is called by Hotkeys
     */
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
        drawString(Constant.AGE_MIN,300,300);
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
