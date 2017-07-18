import java.io.*;
package group1;


class test{

	public static void main (String args[]){
 		/*
 		short red = 5;
		short green = 5;
		short blue = 5;

		IPixel pixel = new Pixel(red, green, blue);
		System.out.println("luma: "+pixel.getLuma()+" saturation: "+pixel.getSaturation()+" color: "+pixel.getColor()); */
		
		IImage dummy = new Dummy();
		int dummyHeight = dummy.getImage().length;
		int dummyWidth = dummy.getImage()[0].length;

		for(int i = 0; i < dummyHeight; i++) for(int j = 0; j < dummyWidth; j++){
			System.out.println("x: "+i+" y: "+j+" luma: "+dummy.getImage()[i][j].getLuma()+" sat: "+dummy.getImage()[i][j].getSaturation()+" color: "+dummy.getImage()[i][j].getColor());
		}

	}
}