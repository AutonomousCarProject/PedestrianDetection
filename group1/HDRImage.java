package group1;


import fly2cam.FlyCamera;

import java.awt.*;
import java.util.Arrays;

/**
 * Image class for HDR images, handles the sequence of four images sent by the camera using HDR
 */

public class HDRImage implements IImage {
	public int height;
	public int width;

	public int frameRate = 4;
	private FlyCamera flyCam = new FlyCamera();

	private byte[] camBytes;
	private short[][][][] images;
	private double[][] tempHue;
	private IPixel[][] out;

	private int tile;

	public static final int GAIN_MIN = 256;
	public static final int GAIN_MAX = 814;
	public static final int SHUTTER_MIN = 1;
	public static final int SHUTTER_MAX = 966;
	public static final double SHUTTER_MS_PER_INC = .0000339;

	private static final float SAT_MIN = 0.2f;
	private static final float LUM_BLACK = 0.3f;
	private static final float LUM_WHITE = 0.7f;

	private static final double K = 0.25;

	public HDRImage(int exposure, long[] HDRShutters, long[] HDRGains) {
		flyCam.Connect(frameRate, exposure, 0, 0);

		flyCam.SetHDR(HDRShutters, HDRGains);

		int res = flyCam.Dimz();
		height = res >> 16;
		width = res & 0x0000FFFF;

		camBytes = new byte[height * width * 4];
		images = new short[4][height][width][3];
		tempHue = new double[height][width];
		out = new IPixel[height][width];
		tile = flyCam.PixTile();
		System.out.println("tile: "+tile+" width: "+width+" height: "+height);
	}

	@Override
	public void setAutoFreq(int autoFreq) {}

	//this is where the HDR happens
	@Override
	public void readCam() {
		//step 1: all the HDR images
		for(int i = 0; i < images.length; i++){
			flyCam.NextFrame(camBytes);
			byteConvert(i);
		}
		//ow, that's a lot of time
		//step 2: HDR
		fuseImages();
		//step 3: median filter
		//meanFilter();
		medianFilter();
		//step 4; MEAN SHIFT BITCHES WOOOOOOO
		//tempHue = MeanShiftImage.meanShift(tempHue, 10);
		//step 5: convert doubles to color
		for(int i = 0; i < tempHue.length; i++){
			for(int o = 0; o < tempHue[0].length; o++){
				if(tempHue[i][o] < 0) out[i][o] = new Pixel(-(int) tempHue[i][o]);

				else {

					final Color thing = new Color(Color.HSBtoRGB((float)tempHue[i][o], 1, 1));
					out[i][o] = new Pixel((short)thing.getRed(), (short)thing.getGreen(), (short)thing.getBlue());
					//*/
					/*
					//convert hue to our posterized colors
					if(tempHue[i][o] < .125 || tempHue[i][o] > .875) out[i][o] = new Pixel(0); //red
					else if(tempHue[i][o] < 0.5) out[i][o] = new Pixel(1); //green
					else out[i][o] = new Pixel(2); //blue */

				}
			}
		}
	}

	@Override
	public IPixel[][] getImage(){
		return out;
	}

	@Override
	public void finish() {
		flyCam.Finish();
	}

	private void byteConvert(int index)
	{

		int pos = 0;
		if(tile == 1){
			for (int i = 0; i < height; i++)
			{

				for (int j = 0; j < width; j++)
				{
					images[index][i][j][0] = (short) (camBytes[pos] & 255);
					images[index][i][j][1] = (short) (camBytes[pos + 1] & 255);
					images[index][i][j][2] = (short) (camBytes[pos + 1 + width * 2] & 255);
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

					images[index][i][j][0] = (short) (camBytes[pos +  width * 2] & 255);
					images[index][i][j][1] = (short) (camBytes[pos] & 255);
					images[index][i][j][2] = (short) (camBytes[pos + 1] & 255);
					pos += 2;

				}

				pos += width * 2;

			}
		}
	}

	private static int getFancyS(short r, short g, short b){
		return Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b));
	}

	private static float getFancyL(short r, short g, short b){
		return 0.212f * r + 0.7152f * g + 0.0722f * b;
	}

	private void fuseImages() {
		//find saturation, then weighted average
		//for every pixel
		for(int i = 0; i < images[0].length; i++){
			for(int j = 0; j < images[0][0].length; j++){
				//take weighted average with saturation and value
				double sumHA = 0;
				double sumHB = 0;
				double sumV = 0;
				boolean sumCheck = false;

				for(int m = 0; m < images.length; m++) {
					final float[] HSV = Color.RGBtoHSB(images[m][i][j][0], images[m][i][j][1], images[m][i][j][2], null);
					//if the hue is saturated, take the average of it
					if (HSV[1] >= SAT_MIN && HSV[2] >= 0.1f) {
						sumHA += Math.sin(HSV[0] * 2 * Math.PI) * HSV[1];
						sumHB += Math.cos(HSV[0] * 2 * Math.PI) * HSV[1];
						sumCheck = true;
					}
					//else try the average of the value
					else if(!sumCheck) {
						sumV += HSV[2];
					}
				}

				//if we got any hue average value, make that the color
				if(sumCheck){
					double hue = Math.atan2(sumHA, sumHB) / (2 * Math.PI);
					if(hue < 0) hue += 1;

					tempHue[i][j] = hue;
				}
				//else use the average value as the color (e.g. greyscale)
				else {
					if(sumV < LUM_BLACK * images.length) tempHue[i][j] = -4;
					else if(sumV > LUM_WHITE * images.length) tempHue[i][j] = -5;
					else tempHue[i][j] = -3;
				}

			}
		}
	}

	private int frameNo = 0;
	@Override
	public int getFrameNo()
	{
		return frameNo++;
	}

	private static double getWeight(double color, double exposureNum, double numExposures) {
		//final double c = Math.pow(exposureNum / numExposures, 0.3);
		return Math.exp(-Math.pow((color), 2) / K);
	}

	public void medianFilter(){
		final int windowSize = 3;

		double[][] justOnce = new double[tempHue.length][tempHue[0].length];

		for(int i=0; i<tempHue.length; i++){
			for(int j=0; j<tempHue[0].length; j++){
				if(i>tempHue.length-windowSize || j>tempHue[0].length-windowSize){
					//tempHue[i][j] = new Pixel((short)0, (short)0, (short)0);
				}
				else{
					double[] hues = new double[windowSize*windowSize];
					int count = 0;

					for(int w=0; w<windowSize; w++){
						for(int q=0; q<windowSize; q++){
							if(tempHue[i+w][j+q] >= 0) {
								hues[w*windowSize + q] = tempHue[i+w][j+q];
								count++;
							}
						}
					}
					if(count > 0){
						Arrays.sort(hues);
						final int half = count>>1;
						justOnce[i][j] = hues[half];
					}
					else {
						justOnce[i][j] = tempHue[i][j];
					}
				}
			}
		}
		tempHue = justOnce;
	}

	public void meanFilter(){
		final int windowSize = 3;

		double[][] justOnce = new double[tempHue.length][tempHue[0].length];

		for(int i=0; i<tempHue.length; i++){
			for(int j=0; j<tempHue[0].length; j++){
				if(i>tempHue.length-windowSize || j>tempHue[0].length-windowSize){
					//tempHue[i][j] = new Pixel((short)0, (short)0, (short)0);
				}
				else{
					double sum = 0;

					for(int w=0; w<windowSize; w++){
						for(int q=0; q<windowSize; q++){
							sum += tempHue[i+w][j+q];
						}
					}

					justOnce[i][j] = sum / 9.0;
				}
			}
		}
		tempHue = justOnce;
	}
}
