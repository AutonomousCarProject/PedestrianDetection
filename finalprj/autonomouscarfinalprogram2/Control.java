/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprj.autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;
import group1.FileImage;
import group1.IPixel;
import group2.Blob;
import group2.BlobDetection;
import group2.IBlobDetection;
import group3.IMovingBlobDetection;
import group3.MovingBlob;
import group3.MovingBlobDetection;
import group4.BlobFilter;
import group4.IMovingBlobReduction;
import group5.IImageBoxDrawer;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class Control extends LooiObject
{
	private int SATURATION_MIN = 10;
	private int WINDOW_SIZE = 30 / 2;

	private double histogram[] = new double[255];

	private FileImage image = new FileImage();
	private IImageBoxDrawer boxDrawer = new IImageBoxDrawer();
	private IBlobDetection blobDetect = new BlobDetection();
	private IMovingBlobDetection blobMove = new MovingBlobDetection();
	private IMovingBlobReduction blobFilter = new BlobFilter();
	private Slider sSlide = new Slider(10, 10, 640, 10, new Background(Color.GREEN));
	private Slider wSlide = new Slider(10, 30, 640, 10, new Background(Color.GREEN));

	//gets hue, saturation using some fancy ass formula I found online
	private static double getFancyH(int r, int g, int b){
		//staggering beauty
		return Color.RGBtoHSB(r, g, b, null)[0];
	}

	private static double getFancyS(double r, double g, double b){
		return Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b));
	}
    
    private int previousFrame = -1;
    public static boolean keepGoing;
    
    public Control()
    {
        
    }
    /**
     * This method runs 60 timer per sec
     */
    protected void looiStep()
    {
    	//step 0: get slider values
	    WINDOW_SIZE = (int)wSlide.getPercentage();
	    SATURATION_MIN = (int)sSlide.getPercentage() / 2;

	    System.out.println(WINDOW_SIZE + " " + SATURATION_MIN);

	    //step 1: get the image
	    image.readCam();
	    IPixel[][] ray = image.getImage();

	    previousFrame++;

	    if(image.getFrameNo()==previousFrame) {
		    previousFrame = -1;
		    image.finish();
		    image = new FileImage();
	    }

	    //step 2: throw out any grays b/c the hues on them will be weird
	    //and also create histogram

	    histogram = new double[255];

	    //for now we'll hue them to thier own number
	    for(int i = 0; i < ray.length; i++){
		    for(int o = 0; o < ray[0].length; o++){
			    if(getFancyS(ray[i][o].getRed(), ray[i][o].getGreen(), ray[i][o].getBlue()) < SATURATION_MIN) ray[i][o].setColor((short)256);
			    else{
			    	double sat = getFancyS(ray[i][o].getRed(), ray[i][o].getGreen(), ray[i][o].getBlue()) / 255.0;
			    	ray[i][o].setColor((short)(getFancyH(ray[i][o].getRed(), ray[i][o].getGreen(), ray[i][o].getBlue()) * 255));
			    	histogram[ray[i][o].getColor()] += 2 * sat;
			    }
		    }
	    }

	    int[] shiftResults = new int[255];
	    //step 3: mean shift every bin, and but the results in an array
	    for(int i = 0; i < 255; i++){
	    	int lastwm;
	    	int wm = i;
	    	do{
	    		lastwm = wm;
			    double suma = 0;
			    double sumb = 0;
			    double total = 0;
			    for(int o = lastwm - WINDOW_SIZE; o < lastwm + WINDOW_SIZE; o++){
			    	int index = o;
			    	if(index < 0) index += 255;
			    	else if(index >= 255) index -= 255;
				    suma += Math.cos(index / 255.0 * 2.0 * Math.PI) * histogram[index];
				    sumb += Math.sin(index / 255.0 * 2.0 * Math.PI) * histogram[index];
				    total += histogram[index];
			    }

			    wm = (int)Math.round(Math.atan2(sumb / total, suma / total) * (255.0 / (2.0 * Math.PI)));
			    if(wm < 0) wm += 255;
		    } while(wm != lastwm);
	    	shiftResults[i] = wm;
	    }

	    //step 4: assign them bins to them hawt pixels
	    for(int i = 0; i < ray.length; i++){
	    	for(int o = 0; o < ray[0].length; o++){
	    		if(ray[i][o].getColor() != 256) ray[i][o].setColor((short)shiftResults[ray[i][o].getColor()]);
		    }
	    }

	    //step 70: do the drawing
	    image.setImage(ray);

	    //blobl stuff
	    List<MovingBlob> thing = blobFilter.reduce(blobMove.getMovingBlobs(blobDetect.getBlobs(image)));


	    boxDrawer.draw(image, thing);

    }
    
    public static void pauseUnpause(){
    	keepGoing = !keepGoing;
    }
    
    protected void looiPaint()
    {
        drawImage(boxDrawer.getCurrentImage(),0,0,getInternalWidth(),getInternalHeight());

        //drawImage(testBI,0,0,getInternalWidth(),getInternalHeight());

	    //histogram
	    for(int i = 0; i < histogram.length; i++){
	    	//set color
		    setColor(new Color(Color.HSBtoRGB((float)i / 255.0f, 1.0f, 1.0f)));
		    //draw rect
		    drawRect(i, getInternalHeight(), 1, -histogram[i]);
	    }
    }
}
