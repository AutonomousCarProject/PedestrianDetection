package group1;
import java.util.Random;


class Dummy implements IImage{
	
	public int height = 240;
	public int width = 320;
	Random myRand = new Random();
	Pixel[][] image = new Pixel[320][240];
	
	
	Dummy(){
		
		for(int i = 0 ; i < height ; i++){
			
			for(int j = 0 ; j < width ; j++){
				
				image[j][i] = new Pixel((short)myRand.nextInt(255), (short)myRand.nextInt(255), (short)myRand.nextInt(255));
				
			}
			
		}
		
	}
	
	public IPixel[][] getImage(){
		return image;
	}
	
}