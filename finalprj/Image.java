package autonomouscarfinalprogram;

import java.io.*;
//import fly2cam.FlyCamera;

class Image implements IImage{
	
	int height;
	int width;

	int colorMargin = 30;
	int frameRate = 3;
	public FlyCamera flyCam = new FlyCamera();
	
	//307200
	public byte[] camBytes = new byte[307200*4];
	public Pixel[][] rgbPixels = null;
	public int pos = 0;
	public IPixel[][] image;

	Image(int width, int height){

		this.height = height;
		this.width = width;

	}

	
	public IPixel[][] getImage(){
		return image;
	}
	

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

		for(int i = 0 ; i < height * 2 ; i =+ 2){


			for(int j = 0 ; j < width * 2 ; j =+ 2){

				image[j/2][i/2] = new Pixel(camBytes[pos], camBytes[pos + 1], camBytes[pos + 1 + width * 2]);
				pos =+ 2;

			}

			pos =+ width * 2;

		}

	}
	
	void saveImage(String filename){
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(filename))){
			
		} catch( IOException e){
			System.out.println("Error Opening File");
		}
	}
	
}