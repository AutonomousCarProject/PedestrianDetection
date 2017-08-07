package group1;


import fly2cam.FlyCamera;

import javax.swing.table.AbstractTableModel;
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

	private short[] camBytes;
	private int[][][] images;
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

	private static final int BIT_MIN = 8;

	private static final double K = 0.25;

	public HDRImage(int exposure, long[] HDRShutters, long[] HDRGains) {
		flyCam.Connect(frameRate, exposure, 0, 0);

		//flyCam.SetHDR(HDRShutters, HDRGains);

		int res = flyCam.Dimz();
		height = res >> 16;
		width = res & 0x0000FFFF;

		camBytes = new short[height * width * 4];
		images = new int[height][width][3];
		out = new IPixel[height][width];
		//tile = flyCam.PixTile();
		tile = 1;
		System.out.println("tile: "+tile+" width: "+width+" height: "+height);
	}

	@Override
	public void setAutoFreq(int autoFreq) {}

	//this is where the HDR happens
	@Override
	public void readCam() {
		//step 1: all the HDR images
		flyCam.NextFrame(camBytes);
		byteConvert();

		//ow, that's a lot of time
		//step 2: HDR
		//fuseImages();
		//step 3: median filter
		meanFilter();
		// medianFilter();
		//step 4; MEAN SHIFT BITCHES WOOOOOOO
		//tempHue = MeanShiftImage.meanShift(tempHue, 10);
		//step 5: convert doubles to color
		for(int i = 0; i < images.length; i++){
			for(int o = 0; o < images[0].length; o++){
				//search pixel for first high bit, and use the next eight bits after
				//will hopefully increase gain dynamically in the image, bringing out hue in desaturated areas
				int bit = 11;
				//this is completly unreadable
				//searches through every bit to see if it's on, then breaks if so
				while (images[i][o][0] < (1 << bit) && images[i][o][1] < (1 << bit) && images[i][o][2] < (1 << bit) && bit-- > BIT_MIN);
				//then shifts the color such that the bit found it the highest bit
				images[i][o][0] = (images[i][o][0] >> (bit - 6)) & 255;
				images[i][o][1] = (images[i][o][1] >> (bit - 6)) & 255;
				images[i][o][2] = (images[i][o][2] >> (bit - 6)) & 255;
			}
		}

		//medianFilter();

		for(int i = 0; i < images.length; i++){
			for(int o = 0; o < images[0].length; o++){
				out[i][o] = new Pixel((short)images[i][o][0], (short)images[i][o][1], (short)images[i][o][2]);
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

	private void byteConvert()
	{

		int pos = 0;
		if(tile == 1){
			for (int i = 0; i < height; i++)
			{

				for (int j = 0; j < width; j++)
				{
					images[i][j][0] = (camBytes[pos] & 0xffff) >> 4;
					images[i][j][1] = (camBytes[pos + 1] & 0xffff) >> 4;
					images[i][j][2] = (camBytes[pos + 1 + width * 2] & 0xffff) >> 4;
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

					images[i][j][0] = (camBytes[pos +  width * 2] & 0xffff) >> 4;
					images[i][j][1] = (camBytes[pos] & 0xffff) >> 4;
					images[i][j][2] = (camBytes[pos + 1] & 0xfff) >> 4;
					pos += 2;

				}

				pos += width * 2;

			}
		}
	}

	/*
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
	}*/

	public void medianFilter(){
		final int windowSize = 3;

		int[][][] justOnce = new int[images.length][images[0].length][3];

		for(int i=0; i<images.length; i++){
			for(int j=0; j<images[0].length; j++){
				if(i>images.length-windowSize || j>images[0].length-windowSize){
					//tempHue[i][j] = new Pixel((short)0, (short)0, (short)0);
				}
				else{
					int[][] set = new int[3][windowSize*windowSize];

					for(int w=0; w<windowSize; w++) {
						for (int q = 0; q < windowSize; q++) {
							set[0][w * windowSize + q] = images[i + w][j + q][0];
							set[1][w * windowSize + q] = images[i + w][j + q][1];
							set[2][w * windowSize + q] = images[i + w][j + q][2];
						}
					}
					Arrays.sort(set[0]);
					Arrays.sort(set[1]);
					Arrays.sort(set[2]);

					final int half = set[0].length / 2;

					justOnce[i][j] = new int[]{ set[0][half], set[1][half], set[2][half] };
				}
			}
		}
		images = justOnce;
	}

	public void meanFilter(){
		final int windowSize = 3;

		int[][][] justOnce = new int[images.length][images[0].length][3];

		for(int i=0; i<images.length; i++){
			for(int j=0; j<images[0].length; j++){
				if(i>images.length-windowSize || j>images[0].length-windowSize){
					//tempHue[i][j] = new Pixel((short)0, (short)0, (short)0);
				}
				else{
					int[] total = new int[3];

					for(int w=0; w<windowSize; w++) {
						for (int q = 0; q < windowSize; q++) {
							total[0] += images[i + w][j + q][0];
							total[1] += images[i + w][j + q][1];
							total[2] += images[i + w][j + q][2];
						}
					}

					justOnce[i][j] = new int[]{ (int)(total[0] * (1.0 / (windowSize * windowSize))), (int)(total[1] * (1.0 / (windowSize * windowSize))), (int)(total[2] * (1.0 / (windowSize * windowSize))) };
				}
			}
		}
		images = justOnce;
	}

	/*
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
	*/
}
