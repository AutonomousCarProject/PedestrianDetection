package group1;

import fly2cam.FlyCamera;
//import group1.fly0cam.FlyCamera;

//Defines image as an 2d array of pixels
public class Image implements IImage
{
    public int height;
    public int width;

    private int frameRate = 3;
    private FlyCamera flyCam = new FlyCamera();
    
    private int tile;

    // 307200
    // private byte[] camBytes = new byte[2457636];
    private byte[] camBytes;
    private IPixel[][] image;

    public Image()
    {
        flyCam.Connect(frameRate);
        int res = flyCam.Dimz();
        height = res >> 16;
        width = res & 0x0000FFFF;
        
        camBytes = new byte[height * width * 4];
        image = new Pixel[height][width];
        tile = flyCam.PixTile();
        System.out.println("tile: "+tile+" width: "+width+" height: "+height);
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

        //System.out.println("TILE: " + flyCam.PixTile());
        // System.out.println(flyCam.errn);
        flyCam.NextFrame(camBytes);
        // System.out.println(flyCam.errn);
        byteConvert();

    }

    public void finish()
    {
        flyCam.Finish();
  	}
    

    /*
	public int getFrameNo(){
		return flyCam.frameNo;
	}
	*/
	
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

}
