package group1;

import group1.fly0cam.FlyCamera;

import java.util.Arrays;

import global.Constant;

//Defines image as an 2d array of pixels
public class FileImage implements IImage
{
    public int height;
    public int width;

    private final int frameRate = 3;
    private FlyCamera flyCam = new FlyCamera();
    private final float greyRatio = Constant.GREY_RATIO;
    private final int blackRange = Constant.BLACK_RANGE;
    private final int whiteRange = Constant.WHITE_RANGE;

    private int tile;
    private int autoFreq = 15;
    private int autoCount = autoFreq+1;

    // 307200
    // private byte[] camBytes = new byte[2457636];
    private byte[] camBytes;
    private IPixel[][] image;

    public FileImage()
    {
        flyCam.Connect(frameRate);
        int res = flyCam.Dimz();
        height = (res & 0xFFFF0000) >> 16;
        width = res & 0x0000FFFF;

        camBytes = new byte[height * width * 4];
        image = new Pixel[height][width];

        tile = flyCam.PixTile();
        System.out.println("tile: "+tile+" width: "+width+" height: "+height);

    }

    @Override
    public void setAutoFreq(int autoFreq){  //How many frames are loaded before the calibrate is called (-1 never calls it)
        this.autoFreq = autoFreq;
    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }

    // gets a single frame
    @Override
    public void readCam()
    {
        autoCount++;
        //System.out.println("TILE: " + flyCam.PixTile());
        // System.out.println(flyCam.errn);
        flyCam.NextFrame(camBytes);
        // System.out.println(flyCam.errn);


        if(autoCount > autoFreq && autoFreq > -1) {
        	autoConvertWeighted();
            //System.out.println("Calibrating");
            autoCount = 0;
        }
        else{
        	byteConvert();
        }
        image = convertImage(getMedianFilteredImage());
    }
    
    public IPixel[][] convertImage(Pixel[][] imageToConvert){
    	IPixel[][] newImage = new Pixel[imageToConvert.length][imageToConvert[0].length];
		for(int b1=0;b1<imageToConvert.length;b1++){
			for(int b2=0;b2<imageToConvert[0].length;b2++){
				Pixel p = imageToConvert[b1][b2];
				p.simpleConvert();
				newImage[b1][b2] = p;
			}
		}
		return newImage;
    }
    
    
    
    public Pixel[][] getMedianFilteredImage(){
       
    	Pixel[][] filteredImage = new Pixel[image.length][image[0].length];
    	int windowSize = 3;
    	
    	for(int i=0; i<filteredImage.length; i++){
    		for(int j=0; j<filteredImage[0].length; j++){
    			if(i>filteredImage.length-windowSize || j>filteredImage[0].length-windowSize){
    				filteredImage[i][j] = new Pixel((short)0, (short)0, (short)0);
    			}
    			else{
    				short[] reds = new short[windowSize*windowSize];
    				short[] greens = new short[windowSize*windowSize];
    				short[] blues = new short[windowSize*windowSize];
	    			
	    			
	    			for(int w=0; w<windowSize; w++){
	    				for(int q=0; q<windowSize; q++){
	    					reds[w*windowSize + q] = image[i+w][j+q].getRed();
	    					greens[w*windowSize + q] = image[i+w][j+q].getGreen();
	    					blues[w*windowSize + q] = image[i+w][j+q].getBlue();
	    				}
	    			}
	    			
	    			Arrays.sort(reds);
	    			Arrays.sort(greens);
	    			Arrays.sort(blues);
	    			
	    			int half = windowSize*windowSize/2;
	    			Pixel pixel = new Pixel(reds[half], greens[half], blues[half]);
	    			filteredImage[i][j] = pixel;
    			}	
    		}
    	}
    	return filteredImage;
    }
    
    public Pixel[][] getGaussianBlurredImage(IPixel[][] rImage){
    	Pixel[][] blurImage = new Pixel[rImage.length][rImage[0].length];
    	float[][] blurMatrix = new float[][]{{1f/9, 1f/9, 1f/9},
    										{1f/9, 1f/9, 1f/9},
    										{1f/9, 1f/9, 1f/9}};
    	
    	int edge = (int)blurMatrix.length/2;
    	for(int i=0; i<blurImage.length; i++){
    		for(int j=0; j<blurImage[0].length; j++){
    			if(i<edge || j<edge || i>blurImage.length-edge-1 || j>blurImage[0].length-edge-1){
    				blurImage[i][j] = new Pixel((short)0, (short)0, (short)0);
    			}
    			else{
    				short red = 0;
	    			short green = 0;
	    			short blue = 0;
	    			
	    			int half = blurMatrix.length/2;
	    			IPixel[][] pixelSquare = new IPixel[blurMatrix.length][blurMatrix.length];
	    			for(int i1=0; i1<blurMatrix.length; i1++){
	    				for(int i2=0; i2<blurMatrix.length; i2++){
	    					pixelSquare[i1][i2] = rImage[i+i1-half][j+i2-half];
	    				}
	    			}
	    			
	    			
	    			for(int w=0; w<blurMatrix.length; w++){
	    				for(int q=0; q<blurMatrix.length; q++){
	    					red += (short)(pixelSquare[w][q].getRed()*blurMatrix[w][q]);
	    					green += (short)(pixelSquare[w][q].getGreen()*blurMatrix[w][q]);
	    					blue += (short)(pixelSquare[w][q].getBlue()*blurMatrix[w][q]);
	    				}
	    			}
	    			
	    			blurImage[i][j] = new Pixel(red, green, blue);
    			}	
    		}
    	}
    	
    	return blurImage;
    										 
    }

    public void finish()
    {
        flyCam.Finish();
    }

    public int getFrameNo(){
        return flyCam.frameNo;
    }
    
    public void setImage(IPixel[][] i){
    	image = i;
    }

    private void byteConvert()
    {


        int pos = 0;
        if(tile == 1){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255),
                            (short) (camBytes[pos + 1 + width * 2] & 255));
                    pos += 2;

                }

                pos += width * 2;

            }
        }
        else if(tile == 3){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos +  width * 2] & 255) , (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    pos += 2;

                }

                pos += width * 2;

            }
        }

    }

    private void autoConvert()
    {
        int average = 0;    //0-255
        int average2;   //0-765
        int variation = 0;
        final int divisor = (width*height);

        int pos = 0;
        if(tile == 1){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255),
                            (short) (camBytes[pos + 1 + width * 2] & 255));
                    pos += 2;

                    average += image[i][j].getRed() + image[i][j].getGreen()+ image[i][j].getBlue();

                }

                pos += width * 2;

            }
        }
        else if(tile == 3){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos +  width * 2] & 255) , (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    pos += 2;

                    average += image[i][j].getRed() + image[i][j].getGreen()+ image[i][j].getBlue();

                }

                pos += width * 2;

            }
        }

        average2 = average / divisor;
        average = average2/3;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++) {

                IPixel temp = image[i][j];
                int rVar = temp.getRed()-average;
                if(rVar < 0){
                    rVar = -rVar;
                }

                int gVar = temp.getGreen()-average;
                if(gVar < 0){
                    gVar = -rVar;
                }

                int bVar = temp.getBlue()-average;
                if(bVar < 0) {
                    bVar = -bVar;
                }

                variation += rVar + gVar + bVar;
            }

        }

        variation = variation / divisor;
        Pixel.greyMargin = (int)(variation * greyRatio);
        Pixel.blackMargin = average2 - blackRange;
        Pixel.whiteMargin = average2 + whiteRange;
        System.out.println("Variation: "+variation+" greyRatio: "+greyRatio);
        System.out.println("greyMargin: " + Pixel.greyMargin + " blackMargin: " + Pixel.blackMargin + " whiteMargin: " + Pixel.whiteMargin);

    }
    
    
    private void autoConvertWeighted() {

        int average;    //0-255
        int average2;   //0-765
        int variation = 0;
        final int divisor = (width * height);


        //autoThreshold variables
        int threshold = 381;
        int avg; //0-765
        int r, b, g;
        int lesserSum = 0;
        int greaterSum = 0;
        int lesserCount = 0;
        int greaterCount = 0;
        int lesserMean;
        int greaterMean;

        int pos = 0;
        if (tile == 1) {
            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    image[i][j] = new Pixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255), (short) (camBytes[pos + 1 + width * 2] & 255));
                    pos += 2;

                    r = image[i][j].getRed();
                    b = image[i][j].getBlue();
                    g = image[i][j].getGreen();

                    avg = (r + b + g);

                    if (avg < threshold) {

                        lesserSum += avg;
                        lesserCount++;

                    } else {

                        greaterSum += avg;
                        greaterCount++;

                    }


                }

                pos += width << 1;

            }


        } else if (tile == 3) {
            for (int i = 0; i < height; i++) {

                for (int j = 0; j < width; j++) {

                    image[i][j] = new Pixel((short) (camBytes[pos + width * 2] & 255), (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    pos += 2;

                    r = image[i][j].getRed();
                    b = image[i][j].getBlue();
                    g = image[i][j].getGreen();

                    avg = (r + b + g);

                    if (avg < threshold) {

                        lesserSum += avg;
                        lesserCount++;

                    } else {

                        greaterSum += avg;
                        greaterCount++;

                    }

                }

                pos += width << 1;

            }


        }

        lesserMean = lesserSum / lesserCount;
        greaterMean = greaterSum / greaterCount;
        threshold = (lesserMean + greaterMean) >> 1;

        average2 = threshold;
        average = average2 / 3;


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                IPixel temp = image[i][j];
                int rVar = temp.getRed() - average;
                if (rVar < 0) {
                    rVar = -rVar;
                }

                int gVar = temp.getGreen() - average;
                if (gVar < 0) {
                    gVar = -rVar;
                }

                int bVar = temp.getBlue() - average;
                if (bVar < 0) {
                    bVar = -bVar;
                }

                variation += rVar + gVar + bVar;
            }

        }

        variation = variation / divisor;
        Pixel.greyMargin = (int) (variation * greyRatio);
        Pixel.blackMargin = average2 - blackRange;
        Pixel.whiteMargin = average2 + whiteRange;

    }

}
