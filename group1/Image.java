package group1;

import java.io.*;
import fly2cam.FlyCamera;

class Image implements IImage{
	
	int height;
	int width;

	int colorMargin = 30;
	int frameRate = 15;
	public FlyCamera flyCam = new FlyCamera();
	public byte[] camBytes = null;
	public Pixel[][] rgbPixels = null;
	public int pos = null;
	Pixel[][] image;

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
		
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			
			Pixel current = image[i][j];
			short lum = current.red + current.green + current.blue;
			lum = lum/765;		
			
		}
	}

	void readCam(){

		flyCam.Connect(frameRate);
		flyCam.NextFrame(camBytes);

	}

	void byteConvert(){

		pos = 0;

		for(int i = 0 ; i < height * 2 ; =+ 2){


			for(int j = 0 ; j < width * 2 ; j =+ 2){

				rgbPixels[j/2][i/2].red = camBytes[pos];
				rgbPixels[j/2][i/2].green = camBytes[pos + 1];
				rgbPixels[j/2][i/2].blue = camBytes[pos + 1 + width * 2];
				pos =+ 2;

			}

			pos + width * 2;

		}

	}
	
	void saveImage(String filename){
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
			
		} catch( IOException e){
			System.out.println("Error Opening File");
		}
	}
	
}
