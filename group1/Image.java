
<<<<<<< HEAD

import java.io.*;
import fly2cam.FlyCamera;

public FlyCamera flyCam = new FlyCamera();
public byte[] camBytes = null;



void readCam(){

	flyCam.NextFrame(camBytes);

}

=======
class Image extends IImage{
	int height;
	int width;
	
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
	
}
>>>>>>> 3adad10fdae8dbe47da94a3d948cde492bef5f23
