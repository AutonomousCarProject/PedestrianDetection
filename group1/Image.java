package group1;

import java.io.*;
import fly2cam.FlyCamera;

class Image implements IImage{
	int height;
	int width;
	int colorMargin = 30;
	public FlyCamera flyCam = new FlyCamera();
	public byte[] camBytes = null;
	
	Pixel[][] image;
	
	Image(int width, int height){
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			image[i][j] = new Pixel();
		}
		this.height = height;
		this.width = width;
	}
	
	void readCam(){
		
		flyCam.NextFrame(camBytes);

	}
}
