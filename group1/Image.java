import java.io.*;
import fly2cam.FlyCamera;

public FlyCamera flyCam = new FlyCamera();
public String fiName = "FlyCapped.By8";
public byte[] camBytes = null;

public Pixel[][] rgbPixels = null;
public int pos = null;


class Image extends IImage{
	int height;
	int width;
	int frameRate = 15;
	
	Pixel[][] image;
	
	Image(){
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			image[i][j] = new Pixel();
		}
	}
	
	void convert(){	//converts rgb to yuv format
		
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
	
}

