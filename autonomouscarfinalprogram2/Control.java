/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//*****
package autonomouscarfinalprogram2;

import AIGroup.SimpleOptim;
import ai_data_creation.BlobSelector;
import com.looi.looi.LooiObject;
import com.looi.looi.gui_essentials.*;
import com.looi.looi.gui_essentials.Button;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.gui_essentials.Window.DecorationBar;
import group1.IPixel;

import global.Constant;
import group1.FileImage;
import group2.Blob;
import group2.BlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group5.IImageBoxDrawer;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
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
    private FileImage currentImage;
    
    private Button toggleGraphics;
    private Button pedestrianAccuracy;
    private ScrollBox scrollBox;
    private Window aiWindow;
    private Window dataCreationWindow;
    
    
    
    private int previousFrame;
    private int currentFrame;
    private boolean keepGoing;
    
    private ArrayDeque<IPixel[][]> frames;
    private ArrayList<IPixel[][]> frameList;
    
    private int yCoordinate;
    private SimpleOptim simpleOptim;
    
    List<Blob> knownBlobs;
    List<MovingBlob> movingBlobs;
    List<MovingBlob> fmovingBlobs;
    List<MovingBlob> unifiedBlobs;
    
    private BlobSelector blobSelector;
    public static final String DATA_CREATION_MODE = "data_creation_mode";
    public static final String AI_MODE = "ai_mode";
    private String mode = DATA_CREATION_MODE;
    private Window loadWindow;
    private static String text[] = {"Age Min","Velocity X Max", "Velocity Y Max",
                "Max Velocity Change X", "Max Velocity Change Y", "Max Width Height Ratio", "Max Width",
                "Max Height", "Max Scaled Velocity X", "Max Scaled Velocity Y"};
    private ConstantEditor[] constantEditors = new ConstantEditor[text.length];
    
    public Control(int frameDelay, boolean useCamera)
    {
        simpleOptim = new SimpleOptim("");
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
        //blobSelector = new BlobSelector(boxDrawer,this);

        aiWindow = new Window(100, 100, 500, 500, new Background(Color.WHITE));
        DecorationBar d;
        aiWindow.add(d = aiWindow.new DecorationBar()); 
        d.add(aiWindow.new ExitButton());
        aiWindow.add(scrollBox = new ScrollBox(25, 100, 450, 375, new Background(new Color(250, 250, 255))));

        


        // displays text
        for(int i = 0; i < text.length; i++) {
            //displays text
            scrollBox.add(new Text(150, i*100+20, 100, 30, new Background(Color.WHITE), text[i]));
            //displays Constant Editors
            scrollBox.add(
                                    constantEditors[i] = new ConstantEditor(10,i*100+20,100,50, "" + Constant.getVariable(i+14)
                                                        )); //constantIndex is i + 14 to start at BlobFilter constants in Constant class
        }

        scrollBox.add(new SaveButton(10,100*text.length,150,100,"Save",new Color(150,200,40)));

        toggleGraphics = new AstheticButton(10,100*text.length+100,135,100,"Toggle Graphics",Background.DARK_GRAY_BACKGROUND)
        {
            @Override
            protected void action()
            {
                boxDrawer.setUsingBasicColors(!boxDrawer.isUsingBasicColors());
            }
        };
        toggleGraphics.setLayer(-999);
        scrollBox.add(toggleGraphics);
        

        pedestrianAccuracy = new AstheticButton(10, 100*text.length + 200, 150, 100, "Blob Accuracy", Background.BLUE_BACKGROUND) {
            @Override
            protected void action() {
                //scrollBox.add(new Text(180, 100*text.length + 240, 100, 50, new Background(Color.WHITE), "" + simpleOptim.getScore()));
                simpleOptim.setFrame();
                float[] score = simpleOptim.getScore();
                Window w = new Window(500,500,200,200,Background.WHITE_BACKGROUND);
                DecorationBar d;
                w.add(d = w.new DecorationBar());  
                d.add(w.new ExitButton()); 
                w.add(new TextBox(10,60,180,130,"score: " + score[0] + " accuracy: " + score[1] + " miss accuracy: " + score[2],false));   
            }
        };
        pedestrianAccuracy.setLayer(-999);
        scrollBox.add(new AstheticButton(10,pedestrianAccuracy.getY() + toggleGraphics.getHeight(),130,100,"Swithch to data creation mode",Background.RED_BACKGROUND)
        {
            public void action()
            {
                mode = Control.DATA_CREATION_MODE;
            }
        });
        scrollBox.add(pedestrianAccuracy);
        scrollBox.add(new AstheticButton(10,3000,130,100,"Load",Background.WHITE_BACKGROUND)
        {
            public void action()
            {
                loadWindow.activate();
            }
        });
        scrollBox.add(new AstheticButton(10,2500,130,100,"Evolve",Background.WHITE_BACKGROUND)
        {
            public void action()
            {
                simpleOptim.runForce();
//                Window w = new Window(500,500,300,300,Background.WHITE_BACKGROUND);
//                DecorationBar d;
//                w.add(d = w.new DecorationBar());
//                d.add(w.new ExitButton());
                //System.out.println(simpl);
                for(int i = 0; i < constantEditors.length; i++)
                {
                    constantEditors[i].setText(""+Constant.<Double>getVariable(i+1)); 
                    System.out.println("");
                }
                
            }
        });
        loadWindow = new Window(0,0,500,500,Background.WHITE_BACKGROUND);
        ScrollBox loadFiles = new ScrollBox(50,75,400,400,Background.LIGHT_GRAY_BACKGROUND);
        loadWindow.add(loadFiles); 
        DecorationBar dd;
        loadWindow.add(dd = loadWindow.new DecorationBar());
        dd.add(loadWindow.new ExitButton()); 
        
        
        
        dataCreationWindow = new Window(400,400,500,500,Background.WHITE_BACKGROUND);
        DecorationBar d2;
        dataCreationWindow.add(d2=dataCreationWindow.new DecorationBar());
        d2.add(dataCreationWindow.new ExitButton());
        dataCreationWindow.add(new AstheticButton(150,100,130,100,"Switch to ai mode",Background.RED_BACKGROUND)
        {
            public void action()
            {
                mode = AI_MODE;
            }
        }); 
        blobSelector = new BlobSelector(boxDrawer,this,dataCreationWindow);
        try
        {
            FileInputStream fis = new FileInputStream(blobSelector.getFileDocumentName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            double y = 10;
            while((line = br.readLine()) != null)
            {
                String line2 = line;
                loadFiles.add(new AstheticButton(10,y,400,50,line,Background.WHITE_BACKGROUND)
                {
                    public void action()
                    {
                        simpleOptim.setFileName(line2); 
                    }
                });
                y+=50;
                
            }
        }
        catch(Exception e)
        {

        }
    }
    
    
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
        
        
        if(mode.equals(AI_MODE))
        {
            aiWindow.activate();
            dataCreationWindow.deactivate();
        }
        if(mode.equals(DATA_CREATION_MODE))
        {
            aiWindow.deactivate();
            dataCreationWindow.activate();
        }
        
        
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
        
        List<MovingBlob> loaded = simpleOptim.getBlobs();
        if(!getPaused())
        {
            knownBlobs = blobDetection.getBlobs(currentImage);
            movingBlobs = movingBlobDetection.getMovingBlobs(knownBlobs);
            for(MovingBlob b : movingBlobs)
            {
                b.setAsPedestrian(false); 
            }
            fmovingBlobs = blobFilter.filterMovingBlobs(movingBlobs);
            
            unifiedBlobs = movingBlobDetection.getUnifiedBlobs(fmovingBlobs);
            //List<MovingBlob> funifiedBlobs = blobFilter.filterUnifiedBlobs(unifiedBlobs);
            //boxDrawer.draw2(currentImage,fmovingBlobs,funifiedBlobs);
            
            
            
        }
        if(mode.equals(DATA_CREATION_MODE)) 
        {
            boxDrawer.blobsToRectangles(currentImage,movingBlobs);
        }
        if(mode.equals(AI_MODE))
        {
            boxDrawer.blobsToRectangles(currentImage,loaded);
        }
        
        /*System.out.println(simpleOptim.getBlobs().size());
        for(MovingBlob b : loaded)
        {
            System.out.print("X: " + b.x + " Y: " + b.y + " Width: " + b.width + " Height: " + b.height);
            System.out.println("");
        }*/
        boxDrawer.draw(currentImage);
    }
    public List<MovingBlob> getUnifiedBlobs()
    {
        return unifiedBlobs;
    }
    public List<MovingBlob> getMovingBlobs()
    {
        return movingBlobs;
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
        List<MovingBlob> funifiedBlobs = blobFilter.filterUnifiedBlobs(unifiedBlobs);
		//boxDrawer.draw2(currentImage, movingBlobs, fmovingBlobs);
        boxDrawer.draw2(currentImage,fmovingBlobs,funifiedBlobs);
	        
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
