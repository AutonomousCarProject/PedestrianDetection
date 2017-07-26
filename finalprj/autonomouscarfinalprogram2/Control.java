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
import com.looi.looi.gui_essentials.TextBox;
import com.looi.looi.gui_essentials.Window;
import global.Constant;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.awt.image.BufferedImage;
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
    public static boolean keepGoing;
    
    private int frameDelayInMS;
    
    public Control(int frameDelay)
    {
        blobDetection = new BlobDetection();
        movingBlobDetection = new MovingBlobDetection();
        blobFilter = new BlobFilter();
        currentImage = new FileImage();
        boxDrawer = new IImageBoxDrawer();
        boxDrawer.setUsingBasicColors(true);
        toggleGraphics = new AstheticButton(10,2010,135,100,"Toggle Graphics",Color.GRAY) 
        {
            @Override
            protected void action() 
            {
                boxDrawer.setUsingBasicColors(!boxDrawer.isUsingBasicColors()); 
            }
        };
        toggleGraphics.setLayer(-999);
        sliderWindow = new DraggingWindow(100,100,500,500,new Background(Color.WHITE));
        sliderWindow.add(sliderWindow.new ExitButton()); 
        sliderWindow.add(scrollBox = new ScrollBox(25,100,450,375,new Background(new Color(250,250,255))));
        scrollBox.add(scrollBox.new ScrollBoxObject(toggleGraphics)); 
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,10,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.AGE_MIN = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,10,100,30,new Background(Color.WHITE),"Age min")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,110,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.DISTANCE_LIMIT = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,110,100,30,new Background(Color.WHITE),"Distance Limit")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,210,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_HEIGHT = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,210,100,30,new Background(Color.WHITE),"Max Height")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,310,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_SCALED_VELOCITY_X = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,310,100,30,new Background(Color.WHITE),"Max Scaled Velocity X")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,410,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_SCALED_VELOCITY_Y = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,410,100,30,new Background(Color.WHITE),"Max_Scaled_Velocity_Y")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,510,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_TIME_OFF_SCREEN = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,510,100,30,new Background(Color.WHITE),"Max Time Off Screen")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,610,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_VELOCITY_CHANGE_X = (float)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,610,100,30,new Background(Color.WHITE),"Max Velocity Change X")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,710,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_VELOCITY_CHANGE_Y = (float)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,710,100,30,new Background(Color.WHITE),"Max Velocity Change Y")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,810,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.MAX_WIDTH = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,810,100,30,new Background(Color.WHITE),"Max Width")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,910,100,20,new Background(Color.WHITE),0,40,(a)->{Constant.MAX_WIDTH_HEIGHT_RATIO = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,910,100,30,new Background(Color.WHITE),"Max Width Height Ratio")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1010,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.UNIFY_VELOCITY_LIMIT_X = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1010,100,30,new Background(Color.WHITE),"Unify Velocity Limit X")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1110,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.UNIFY_VELOCITY_LIMIT_Y = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1110,100,30,new Background(Color.WHITE),"Unify Velocity Limit Y")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1210,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.VELOCITY_LIMIT_INCREASE_X = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1210,100,30,new Background(Color.WHITE),"Velocity Limit Increase X")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1310,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.VELOCITY_LIMIT_INCREASE_Y = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1310,100,30,new Background(Color.WHITE),"Velocity Limit Increase Y")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1410,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.VELOCITY_X_MAX = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1410,100,30,new Background(Color.WHITE),"Velocity X Max")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1510,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.VELOCITY_Y_MAX = (short)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1510,100,30,new Background(Color.WHITE),"Velocity Y Max")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1610,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.X_EDGE_DISTANCE_LIMIT = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1610,100,30,new Background(Color.WHITE),"X Edge Distance Limit")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1710,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.X_OVERLAP_PERCENT = (float)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1710,100,30,new Background(Color.WHITE),"X Overlap Percent")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1810,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.Y_EDGE_DISTANCE_LIMIT = (int)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1810,100,30,new Background(Color.WHITE),"Y Edge Distance Limit")));
        scrollBox.add(scrollBox.new ScrollBoxObject(new VariableSlider(10,1910,100,20,new Background(Color.WHITE),10,40,(a)->{Constant.Y_OVERLAP_PERCENT = (float)(double)a;})));
        scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150,1910,100,30,new Background(Color.WHITE),"Y Overlap Percent")));
    }
    
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
    	if(keepGoing){
	        currentImage.readCam();
	        
	        previousFrame++;
	        
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
	        //System.out.println(movingBlobs.size());
	        //List<MovingBlob> filteredBlobs = blobFilter.reduce(movingBlobDetection.getUnifiedBlobs(blobFilter.filterMovingBlobs(movingBlobs)));
	        boxDrawer.draw(currentImage,fmovingBlobs);
	        
    	}   
    }
    
    public static void pauseUnpause(){
    	keepGoing = !keepGoing;
    }
    
    protected void looiPaint()
    {
        drawString(Constant.AGE_MIN,300,300);
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());
        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());
    }
}
