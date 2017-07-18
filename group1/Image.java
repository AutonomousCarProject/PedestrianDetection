package group1;

import java.io.*;
//import fly2cam.FlyCamera;
import group1.fly0cam.FlyCamera;

class Image implements IImage{
	
	int height;
	int width;

	int colorMargin = 30;
	int frameRate = 3;
	public FlyCamera flyCam = new FlyCamera();
	
	//307200
	public byte[] camBytes = new byte[2457636];
	public Pixel[][] rgbPixels = null;
	public int pos = 0;
	public IPixel[][] image;

	Image(int width, int height){

		this.height = height;
		this.width = width;
		image = new Pixel[width][height];


	}

	
	public IPixel[][] getImage(){
		return image;
	}
	
	/*
	void readCam(){
		
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			
			Pixel current = image[i][j];
			short lum = current.red + current.green + current.blue;
			lum = lum/765;		
			
		}
	}
	*/

	void readCam(){

		System.out.println(flyCam.Connect(frameRate));
		System.out.println(flyCam.errn);
		flyCam.NextFrame(camBytes);
		System.out.println(flyCam.errn);

	}

	void finish(){

		flyCam.Finish();

	}

	void byteConvert(){

		pos = 0;
		System.out.println("Position: " + pos);

		for(int i = 0 ; i < height * 2 ; i += 2){


			for(int j = 0 ; j < width * 2 ; j += 2){

				System.out.println(camBytes[pos]+"  "+camBytes[pos+1]+"  "+camBytes[pos + 1 + width * 2]);
				image[j/2][i/2] = new Pixel((short)(camBytes[pos]+128), (short)(camBytes[pos + 1]+128), (short)(camBytes[pos + 1 + width * 2]+128));

				pos += 2;

			}

			pos += width * 2;

		}

	}
	
}
