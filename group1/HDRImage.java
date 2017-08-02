package group1;

import fly2cam.FlyCamera;

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
		flyCam.Connect(frameRate, exposure, 0, 0);

		flyCam.SetHDR(HDRShutters, HDRGains);
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
				// Do fancy math
				// http://s3.amazonaws.com/academia.edu.documents/34722931/05936115.pdf?AWSAccessKeyId=AKIAIWOWYYGZ2Y53UL3A&Expires=1501706715&Signature=V1h%2BEMGNlikLGbP%2F5TM83zUuGiA%3D&response-content-disposition=inline%3B%20filename%3DMultiple_Exposure_Fusion_for_High_Dynami.pdf

				double[] sumUp = new double[3];
				double[] sumDown = new double[3];

				for(int n = 0; n < images.length; n++){
					sumUp[0] += getWeight(images[n][i][j][0] / 255.0, n, images.length) * images[n][i][j][0];
					sumUp[1] += getWeight(images[n][i][j][1] / 255.0, n, images.length) * images[n][i][j][1];
					sumUp[2] += getWeight(images[n][i][j][2] / 255.0, n, images.length) * images[n][i][j][2];

					sumDown[0] += getWeight(images[n][i][j][0] / 255.0, n, images.length);
					sumDown[1] += getWeight(images[n][i][j][1] / 255.0, n, images.length);
					sumDown[2] += getWeight(images[n][i][j][2] / 255.0, n, images.length);
				}

				//System.out.println(sumUp[0] / sumDown[0]);

				out[i][j] = new Pixel((short)((sumUp[0] / sumDown[0])), (short)((sumUp[1] / sumDown[1])), (short)((sumUp[2] / sumDown[2])));
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
