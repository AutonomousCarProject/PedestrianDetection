package group1;

import java.util.Random;

//Generates dummy data
public class Dummy implements IImage{
	
	public int height = 240;
	public int width = 320;
	private Random myRand = new Random();
	private Pixel[][] image = new Pixel[320][240];
	
	//fills image array
	public void readCam(){
		for(int i = 0 ; i < height ; i++){

			for(int j = 0 ; j < width ; j++){

				image[j][i] = new Pixel((short)myRand.nextInt(255), (short)myRand.nextInt(255), (short)myRand.nextInt(255));

			}

		}
	}
	
	public IPixel[][] getImage(){
		return image;
	}

	public void autoColor(){};

	public void finish(){}
}
