package group1;

import java.io.*;
import fly2cam.FlyCamera;

class Image implements IImage{
	int height;
	int width;

	public FlyCamera flyCam = new FlyCamera();
	public byte[] camBytes = null;
	
	IPixel[][] image;
	
	Image(int width, int height){
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			image[i][j] = new Pixel();
		}
		this.height = height;
		this.width = width;
	}
	
	IPixel[][] getImage(){
		return image;
	}
	
	void readCam(){
		
		flyCam.NextFrame(camBytes);

	}
}
