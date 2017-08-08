package group1;


import fly2cam.FlyCamera;

import java.util.Arrays;

import static group1.Pixel.blackMargin;
import static group1.Pixel.greyMargin;
import static group1.Pixel.whiteMargin;

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

	private static final int WHITE_BAL_ITR_COUNT = 6;
	private static final int PIX_INC = 10;

	public HDRImage(int exposure, int shutter, int gain) {
		flyCam.Connect(frameRate, exposure, shutter, gain);

		int res = flyCam.Dimz();
		height = res >> 16;
		width = res & 0x0000FFFF;

		camBytes = new short[height * width * 4];
		images = new int[height][width][3];
		out = new IPixel[height][width];
		//tile = flyCam.PixTile();
		tile = 1;
		System.out.println("tile: "+tile+" width: "+width+" height: "+height);
		//auto white balance such that our greys are maximized at at stared
		autoWhiteBalance();
	}

	@Override
	public void setAutoFreq(int autoFreq) {}

	//this is where the HDR happens
	@Override
	public void readCam() {
		//step 1: all the HDR images
		flyCam.NextFrame(camBytes);
		byteConvert();

		medianFilter();

		//attempt optimized conversion
		for(int i=0; i<images.length; i++) {
			for (int j = 0; j < images[0].length; j++) {
				final int ave = images[i][j][0] + images[i][j][1] + images[i][j][2];
				final int r = images[i][j][0] * 3;
				final int g = images[i][j][1] * 3;
				final int b = images[i][j][2] * 3;

				int rdiff = r - ave;
				if (rdiff < 0)
				{
					rdiff = -rdiff;
				}

				int gdiff = g - ave;
				if (gdiff < 0)
				{
					gdiff = -gdiff;
				}

				int bdiff = b - ave;
				if (bdiff < 0)
				{
					bdiff = -bdiff;
				}

				if (rdiff < greyMargin && gdiff < greyMargin && bdiff < greyMargin)
				{ // if its not a distinct color
					if (r < blackMargin && g < blackMargin && b < blackMargin)
						out[i][j] = new Pixel(4); // black
					else if (r > whiteMargin && g > whiteMargin && b > whiteMargin)
						out[i][j] = new Pixel(5); // white
					else
						out[i][j] = new Pixel(3);
				}
				else if (r > g && r > b)
					out[i][j] = new Pixel(0);
				else if (g > r && g > b)
					out[i][j] = new Pixel(1);
				else if (b > r && b > g)
					out[i][j] = new Pixel(2);
				//uhhhh... red?
				else out[i][j] = new Pixel(0);

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

	private void byteConvert() {

		int pos = 0;
		if (tile == 1) {
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {
					images[i][j][0] = (camBytes[pos] & 0xffff) >> 4;
					images[i][j][1] = (camBytes[pos + 1] & 0xffff) >> 4;
					images[i][j][2] = (camBytes[pos + 1 + width * 2] & 0xffff) >> 4;
					pos += 2;

				}

				pos += width * 2;

			}
		} else if (tile == 3) {
			for (int i = 0; i < height; i++) {

				for (int j = 0; j < width; j++) {

					images[i][j][0] = (camBytes[pos + width * 2] & 0xffff) >> 4;
					images[i][j][1] = (camBytes[pos] & 0xffff) >> 4;
					images[i][j][2] = (camBytes[pos + 1] & 0xfff) >> 4;
					pos += 2;

				}

				pos += width * 2;

			}
		}
	}

	private void autoWhiteBalance() {
		int redBal = 512;
		int blueBal = 512;
		int inc = 256;
		for(int p = 0; p < WHITE_BAL_ITR_COUNT; p++){
			int totalSats[] = new int[2];
			//set white balance to manual, and enable it, first with first inc
			flyCam.SafeWriteRegister(0x80C, (1 << 25) | redBal - inc | (blueBal << 12), "white balance write failed");
			//get the image, and averaging saturation
			flyCam.NextFrame(camBytes);
			//iterate through most pixels cheaply
			totalSats[0] = cheapSaturationTotal(camBytes, width, height, PIX_INC);
			//shift the balance a sizable amount, then get saturation again
			flyCam.SafeWriteRegister(0x80C, (1 << 25) | redBal + inc | (blueBal << 12), "white balance write failed");
			flyCam.NextFrame(camBytes);
			//iterate through most pixels cheaply
			totalSats[1] = cheapSaturationTotal(camBytes, width, height, PIX_INC);
			//whichever one is lower, move the white balance there
			if(totalSats[0] < totalSats[1]) redBal -= inc;
			else if(totalSats[0] > totalSats[1]) redBal += inc;
			//or just keep it the same
			//and halve the increment
			inc >>= 1;
		}
		//and do it again for blueshift
		inc = 256;
		for(int p = 0; p < WHITE_BAL_ITR_COUNT; p++){
			int totalSats[] = new int[2];
			//set white balance to manual, and enable it, first with first inc
			flyCam.SafeWriteRegister(0x80C, (1 << 25) | redBal | (blueBal - inc << 12), "white balance write failed");
			//get the image, and averaging saturation
			flyCam.NextFrame(camBytes);
			//iterate through most pixels cheaply
			totalSats[0] = cheapSaturationTotal(camBytes, width, height, PIX_INC);
			//shift the balance a sizable amount, then get saturation again
			flyCam.SafeWriteRegister(0x80C, (1 << 25) | redBal | (blueBal + inc << 12), "white balance write failed");
			flyCam.NextFrame(camBytes);
			//iterate through most pixels cheaply
			totalSats[1] = cheapSaturationTotal(camBytes, width, height, PIX_INC);
			//whichever one is lower, move the white balance there
			if(totalSats[0] < totalSats[1]) blueBal -= inc;
			else if(totalSats[0] > totalSats[1]) blueBal += inc;
			//or just keep it the same
			//and halve the increment
			inc >>= 1;
		}
		//do the thing
		flyCam.SafeWriteRegister(0x80C, (1 << 25) | redBal | (blueBal << 12), "white balance write failed");
	}

	private static int cheapSaturationTotal(short[] camBytes, int width, int height, int pixInc) {
		int ret = 0;
		for (int i = 0; i < height; i+=pixInc)
		{
			for (int j = 0; j < width; j+=pixInc)
			{
				final int pos = j * 2 + i * width * 4;
				final int r = (camBytes[pos] & 0xffff) >> 4;
				final int g = (camBytes[pos + 1] & 0xffff) >> 4;
				final int b = (camBytes[pos + 1 + width * 2] & 0xffff) >> 4;
				ret += Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b));
			}
		}
		return ret;
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
