package group1;

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