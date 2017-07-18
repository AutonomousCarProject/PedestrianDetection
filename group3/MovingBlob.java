package group3;

import group2.Blob;

public class MovingBlob extends Blob {
    public float velocityX, velocityY;
    public int age;
    public int ageOffScreen;
    public int predictedX;
    public int predictedY;
	
	public MovingBlob(Blob b){
		this.centerX = b.centerX;
		this.centerY = b.centerY;
		this.width = b.width;
		this.height = b.height;
		this.color = b.color;
		
		this.velocityX = 0;
		this.velocityY = 0;
		this.age = 0;
		this.ageOffScreen = 0;
	}
}