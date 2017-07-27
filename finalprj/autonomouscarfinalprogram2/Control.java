/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import com.looi.looi.gui_essentials.AstheticButton;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Button;
import com.looi.looi.gui_essentials.ScrollBox;
import com.looi.looi.gui_essentials.Slider;
import group1.IPixel;
import group1.Pixel;
import com.looi.looi.gui_essentials.TextBox;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.gui_essentials.ScrollBox.ScrollBoxObject;
import com.looi.looi.gui_essentials.Window.ExitButton;

import global.Constant;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection;
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
import java.awt.Font;




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
    
    private int yCoordinate;
    
    public Control(int frameDelay)
    {
        blobDetection = new BlobDetection();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new FileImage();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);

        previousFrame = 0;
        setCurrentFrame(1);
        keepGoing = true;
        
        currentImage.readCam();
        IPixel[][] firstFrame = currentImage.getImage();
        
        frames = new ArrayDeque<IPixel[][]>(5);
        
        frames.addFirst(firstFrame);
        
        
        
        yCoordinate = 10;
        
        sliderWindow = new DraggingWindow(100,100,500,500,new Background(Color.WHITE));
        sliderWindow.add(sliderWindow.new ExitButton()); 
        sliderWindow.add(scrollBox = new ScrollBox(25,100,450,375,new Background(new Color(250,250,255))));
        
        String text[] = {"Distance Limit X", "Distance Limit Y", "Max Time Off Screen", "Unify Velocity Limit X", 
        					"Unify Velocity Limit Y", "X Edge Distance Limit", "Y Edge Distance Limit", "Max Change Height",
        					"Max Change Width", "Max Velocity Change X", "Max Velocity Change Y", "Velocity Limit Increase X",  
        					"Velocity Limit Increase Y", "X Overlap Percent", "Y Overlap Percent",
        					"Age Min", "Max Height", "Max Width", /*"Max Width Height Ratio",*/ "Max Scaled Velocity X", 
        					"Max Scaled Velocity Y", "Velocity X Max", "Velocity Y Max"};

       Integer consVar[] = {Constant.DISTANCE_LIMIT_X, Constant.DISTANCE_LIMIT_Y, 
    		   			Constant.MAX_TIME_OFF_SCREEN, Constant.UNIFY_VELOCITY_LIMIT_X, Constant.UNIFY_VELOCITY_LIMIT_Y,
    		   			Constant.X_EDGE_DISTANCE_LIMIT, Constant.Y_EDGE_DISTANCE_LIMIT, Constant.MAX_CHANGE_HEIGHT, 
    		   			Constant.MAX_CHANGE_WIDTH};
       

       Float consVar2[] = {Constant.MAX_VELOCITY_CHANGE_X, Constant.MAX_VELOCITY_CHANGE_Y,
    		   			   Constant.VELOCITY_LIMIT_INCREASE_X, Constant.VELOCITY_LIMIT_INCREASE_Y,
    		   			   Constant.X_OVERLAP_PERCENT, Constant.Y_OVERLAP_PERCENT};

       Short consVar3[] = {Constant.AGE_MIN, Constant.MAX_HEIGHT, Constant.MAX_WIDTH, /*Constant.MAX_WIDTH_HEIGHT_RATIO,*/ 
    		   			   Constant.MAX_SCALED_VELOCITY_X, Constant.MAX_SCALED_VELOCITY_Y, Constant.VELOCITY_X_MAX,
    		   			   Constant.VELOCITY_Y_MAX};
       
       int maximumValues[] = {40, 40, 0, 30, 30, 20, 20, 100, 100, 35, 35, 1, 1, 1, 1, 7, 400, 400, 40, 40, 50, 50};
       
       
        // displays text
        for(int i = 0; i < text.length; i++) {                        
        	scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150, i*100+20, 100, 30, new Background(Color.WHITE), text[i])));
        }
      
        // displays sliders with int type
        for(int j = 0; j < consVar.length; j++) {
        	int i = j;
            scrollBox.add(scrollBox.new ScrollBoxObject(
            		new VariableSlider(10,j*100+20,100,20,
            				new Background(Color.WHITE),0,maximumValues[j],(a)->{
            	consVar[i] = (int)(double)a;
            })));
        }
        
        // displays sliders with float type
        for(int j = 0; j < consVar2.length; j++) {
        	int i = j;
            scrollBox.add(scrollBox.new ScrollBoxObject(
            		new VariableSlider(10,j*100+100*consVar.length+20,100,20,
            				new Background(Color.WHITE),0,maximumValues[j+consVar.length],(a)->{
            	consVar2[i] = (float)(double)a;
            })));
        }

        // displays sliders with short type
        for(int j = 0; j < consVar3.length; j++) {
        	int i = j;
            scrollBox.add(scrollBox.new ScrollBoxObject(
            		new VariableSlider(10,j*100+100*consVar.length+100*consVar2.length+20,100,20,
            				new Background(Color.WHITE),0,maximumValues[j+consVar.length+consVar2.length],(a)->{
            	consVar3[i] = (short)(double)a;
            })));
        }
       
        scrollBox.add(scrollBox.new ScrollBoxObject(new SaveButton(10,100*consVar.length+100*consVar2.length+100*consVar3.length,150,100,"Save",new Color(150,200,40))));
        
        toggleGraphics = new AstheticButton(10,100*consVar.length+100*consVar2.length+100*consVar3.length+100,135,100,"Toggle Graphics",Color.GRAY) 
        {
            @Override
            protected void action() 
            {
                boxDrawer.setUsingBasicColors(!boxDrawer.isUsingBasicColors()); 
            }
        };
        toggleGraphics.setLayer(-999);
        scrollBox.add(scrollBox.new ScrollBoxObject(toggleGraphics)); 
    
    }
    
    protected int yPos(){
    	yCoordinate += 100;
    	return yCoordinate-100;
    }
    
    protected int yPos2(){
    	return yCoordinate-100;
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
        
        System.out.println(Constant.DISTANCE_LIMIT_X);
        
        if(currentImage.getFrameNo()==previousFrame){
        	previousFrame = 0;
        	currentImage.finish();
            currentImage = new FileImage();
        	blobDetection = new BlobDetection();
            movingBlobDetection = new MovingBlobDetection();
            blobFilter = new BlobFilter();
            currentImage.readCam();
        }
        
        List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
        List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
        
        boxDrawer.draw2(currentImage,movingBlobs,fmovingBlobs);
        
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
        
    }
    
    public void updateWhilePaused(){
		currentImage.setImage(frameList.get(currentFrame));
		
        List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs();
        List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
		boxDrawer.draw2(currentImage, movingBlobs, fmovingBlobs);

	        
    }   
    
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
