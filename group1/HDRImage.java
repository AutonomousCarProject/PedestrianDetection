package group1;

import group1.fly0cam.FlyCamera;

import java.awt.*;

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
	private IPixel[][] out;

	private long[] HDRShutters;

	private int tile;

	public static final int GAIN_MIN = 256;
	public static final int GAIN_MAX = 814;
	public static final int SHUTTER_MIN = 1;
	public static final int SHUTTER_MAX = 966;
	public static final double SHUTTER_MS_PER_INC = .0000339;

	private static final double K = 0.25;

	public HDRImage(int exposure, long[] HDRShutters, long[] HDRGains) {
		flyCam.Connect(frameRate);

		//flyCam.SetHDR(HDRShutters, HDRGains);
		this.HDRShutters = HDRShutters;

		int res = flyCam.Dimz();
		height = res >> 16;
		width = res & 0x0000FFFF;

		camBytes = new byte[height * width * 4];
		images = new short[4][height][width][3];
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

				for(int m = 0; m < images.length; m++) {
					final float[] HSV = Color.RGBtoHSB(images[m][i][j][0], images[m][i][j][1], images[m][i][j][2], null);
					sumHA += Math.sin(HSV[0] * 2 * Math.PI) * HSV[1] * HSV[2];
					sumHB += Math.cos(HSV[0] * 2 * Math.PI) * HSV[1] * HSV[2];
				}

				float hue = (float)(Math.atan2(sumHA, sumHB) / (2 * Math.PI));
				if(hue < 0) hue += 1;
				final Color color = new Color(Color.HSBtoRGB(hue, 1, 1));


				out[i][j] = new Pixel((short)color.getRed(), (short)color.getGreen(), (short)color.getBlue());
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
}
