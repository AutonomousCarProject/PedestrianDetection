package group1;

import java.io.*;
//import fly2cam.FlyCamera;
import group1.fly0cam.FlyCamera;

//Defines image as an 2d array of pixels
public class Image implements IImage{
	

	public static final int height = 480;
	public static final int width = 640;

	private int frameRate = 3;
	private FlyCamera flyCam = new FlyCamera();
	
	//307200
	private byte[] camBytes = new byte[2457636];
	private IPixel[][] image = new Pixel[width][height];

	
	public IPixel[][] getImage(){
		return image;
	}
	

	//gets a single frame
	public void readCam(){

		flyCam.Connect(frameRate);
		//System.out.println(flyCam.errn);
		flyCam.NextFrame(camBytes);
		//System.out.println(flyCam.errn);
		byteConvert();

	}

	public void finish(){

		flyCam.Finish();

	}

	private void byteConvert(){

		int pos = 0;

		for(int i = 0 ; i < height * 2 ; i += 2){

			for(int j = 0 ; j < width * 2 ; j += 2){
				
				image[j/2][i/2] = new Pixel((short)(camBytes[pos]&255), (short)(camBytes[pos + 1]&255), (short)(camBytes[pos + 1 + width]&255));

				pos ++;

			}

			//pos += width;

		}

	}
	
}
