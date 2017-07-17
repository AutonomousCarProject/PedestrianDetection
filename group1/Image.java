package group1;

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
	int colorMargin = 30;
	
	Pixel[][] image;
	
	Image(int width, int height){
		for(int i = 0; i < height; i++) for(int j = 0; j < weight; j++){
			image[i][j] = new Pixel();
		}
		this.height = height;
		this.width = width;
	}
	
}
>>>>>>> 3adad10fdae8dbe47da94a3d948cde492bef5f23
