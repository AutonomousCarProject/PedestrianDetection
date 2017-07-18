import java.io.*;

class test{
	public static void main (String args[]){
		
		IImage image = new Image(320, 240);
		image.readCam();
		image.byteConvert();


	}
}