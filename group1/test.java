package group1;

import java.io.*;

class test{

	public static void main (String args[]){
 		/*
 		short red = 110;
		short green = 119;
		short blue = 100;

		IPixel pixel = new Pixel(red, green, blue);
		System.out.println("luma: "+pixel.getLuma()+" saturation: "+pixel.getSaturation()+" color: "+pixel.getColor());
		*/
		/*
		IImage dummy = new Dummy();
		int dummyHeight = dummy.getImage().length;
		int dummyWidth = dummy.getImage()[0].length;

		for(int i = 0; i < dummyHeight; i++) for(int j = 0; j < dummyWidth; j++){
			System.out.println("x: "+i+" y: "+j+" luma: "+dummy.getImage()[i][j].getLuma()+" sat: "+dummy.getImage()[i][j].getSaturation()+" color: "+dummy.getImage()[i][j].getColor());
		}*/

		IImage image = new Image();
		image.readCam();
		int dummyHeight = image.getImage().length;
		int dummyWidth = image.getImage()[0].length;
		for(int i = 0; i < dummyHeight; i++){ System.out.println("New line");
			for(int j = 0; j < dummyWidth; j++){
				System.out.println(image.getImage()[i][j].getRed()+" "+image.getImage()[i][j].getGreen()+" "+image.getImage()[i][j].getBlue()+" ");
			}
		}

	}
}