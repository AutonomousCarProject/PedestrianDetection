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
import group1.IPixel;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.gui_essentials.ScrollBox.ScrollBoxObject;
import com.looi.looi.gui_essentials.Window.ExitButton;
import com.sun.org.apache.xpath.internal.functions.FuncUnparsedEntityURI;

import global.Constant;
import group1.FileImage;
import group1.IImage;
import group1.Image;
import group2.Blob;
import group2.BlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
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

	private LoadTextBox ltb;

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


		/*
        yCoordinate = 10;

        sliderWindow = new DraggingWindow(100,100,500,500,new Background(Color.WHITE));
        sliderWindow.add(sliderWindow.new ExitButton()); 
        sliderWindow.add(scrollBox = new ScrollBox(25,100,450,375,new Background(new Color(250,250,255))));

        String text[] = {"Max Time Off Screen", "Distance Limit X", "Distance Limit Y", "Max Change Width", "Max Change Height",
        					"X Edge Distance Limit", "Y Edge Distance Limit", "X Overlap Percent", "Y Overlap Percent", 
        					"Unify Velocity Limit X", "Unify Velocity Limit Y", "Velocity Limit Increase X",  
        					"Velocity Limit Increase Y", "Age Min","Velocity X Max", "Velocity Y Max",
        					 "Max Velocity Change X", "Max Velocity Change Y", "Max Width Height Ratio", "Max Width",
        					 "Max Height", "Max Scaled Velocity X", "Max Scaled Velocity Y", };

       int maximumValues[] = {5, 40, 40, 100, 100, 20, 20, 1, 1, 35, 35, 3, 3, 7, 100, 100, 100, 100, 2, 1000, 1000, 100, 100};


        // displays text
        for(int i = 0; i < text.length; i++) {                        
        	scrollBox.add(scrollBox.new ScrollBoxObject(new Text(150, i*100+20, 100, 30, new Background(Color.WHITE), text[i])));
        }

        // displays sliders
        for(int j = 0; j < text.length; j++) {
        	int i = j+1;
                VariableSlider sld = new VariableSlider<Double>(10,j*100+20,100,20,
            				new Background(Color.WHITE),0,maximumValues[j],(a)->{
            	Constant.setVariable(i, a);
            }, () -> {return Constant.getVariable(i);});
                variableSliders.add(sld);

            scrollBox.add(scrollBox.new ScrollBoxObject(  sld  )); 
        }

        scrollBox.add(scrollBox.new ScrollBoxObject(new SaveButton(10,100*text.length,150,100,"Save",new Color(150,200,40))));

        toggleGraphics = new AstheticButton(10,100*text.length+100,135,100,"Toggle Graphics",Color.GRAY) 
        {
            @Override
            protected void action() 
            {
                boxDrawer.setUsingBasicColors(!boxDrawer.isUsingBasicColors()); 
            }
        };
        toggleGraphics.setLayer(-999);
        scrollBox.add(scrollBox.new ScrollBoxObject(toggleGraphics)); 
        ltb = new LoadTextBox(10,3010,300,40,new Background(Color.WHITE),"File Name", new Font("",Font.PLAIN,16),true,Color.BLACK,10,5,0);
        scrollBox.add(scrollBox.new ScrollBoxObject(ltb));
        ltb.addSliders(variableSliders); 
		 */
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
			blobDetection = new BlobDetection();
			movingBlobDetection = new MovingBlobDetection();
			blobFilter = new BlobFilter();
			currentImage.readCam();
		}

		List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
		List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
		List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
		List<MovingBlob> unifiedBlobs = movingBlobDetection.getUnifiedBlobs(fmovingBlobs);
		List<MovingBlob> funifiedBlobs = blobFilter.filterUnifiedBlobs(unifiedBlobs);
		List<MovingBlob> matchedUnifiedBlobs =  movingBlobDetection.getFilteredUnifiedBlobs(funifiedBlobs);
		List<MovingBlob> fmatchedUnifiedBlobs = blobFilter.filterFilteredUnifiedBlobs(matchedUnifiedBlobs);
		//boxDrawer.draw2(currentImage, unifiedBlobs, fmovingBlobs);
		//boxDrawer.draw(currentImage, funifiedBlobs);
		//boxDrawer.draw2(currentImage, unifiedBlobs, fmovingBlobs);
		boxDrawer.draw(currentImage, funifiedBlobs);


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

		List<Blob> knownBlobs = blobDetection.getBlobs(currentImage);
		List<MovingBlob> movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
		List<MovingBlob> fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
		List<MovingBlob> unifiedBlobs = movingBlobDetection.getUnifiedBlobs(fmovingBlobs);
		List<MovingBlob> funifiedBlobs = blobFilter.filterUnifiedBlobs(unifiedBlobs);
		List<MovingBlob> matchedUnifiedBlobs =  movingBlobDetection.getFilteredUnifiedBlobs(funifiedBlobs);
		List<MovingBlob> fmatchedUnifiedBlobs = blobFilter.filterFilteredUnifiedBlobs(matchedUnifiedBlobs);

		//boxDrawer.draw2(currentImage, unifiedBlobs, fmovingBlobs);
		 boxDrawer.draw(currentImage, fmatchedUnifiedBlobs);
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
