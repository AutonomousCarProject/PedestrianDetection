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

	private int tile;

	public static final int GAIN_MIN = 256;
	public static final int GAIN_MAX = 814;
	public static final int SHUTTER_MIN = 1;
	public static final int SHUTTER_MAX = 966;

	public HDRImage(int exposure, long[] HDRShutters, long[] HDRGains) {
		flyCam.Connect(frameRate, exposure, 0, 0);

		flyCam.SetHDR(HDRShutters, HDRGains);

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

	private void fuseImages() {
		//find saturation, then weighted average
		//for every pixel
		for(int i = 0; i < images[0].length; i++){
			for(int j = 0; j < images[0][0].length; j++){
				//weighted average of the saturation
				int sums[] = new int[3];
				for(int p = 0; p < images.length; p++){
					float sat = getFancyS(images[p][i][j][0], images[p][i][j][1], images[p][i][j][2]);
					sums[0] += images[p][i][j][0] *  sat;
					sums[1] += images[p][i][j][1] *  sat;
					sums[2] += images[p][i][j][2] *  sat;
				}
				out[i][j] = new Pixel(  (short)Math.floor((float)sums[0] / (float)(images.length * 255)),
										(short)Math.floor((float)sums[1] / (float)(images.length * 255)),
										(short)Math.floor((float)sums[1] / (float)(images.length * 255)));
			}
		}
	}

	private int frameNo = 0;
	@Override
	public int getFrameNo()
	{
		return frameNo++;
	}
}
