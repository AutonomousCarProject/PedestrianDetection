package group3;

import group2.Blob;

public class MovingBlob extends Blob {
    public float velocityX, velocityY;
    public int age;
    public int ageOffScreen;
    public int predictedX;
    public int predictedY;
    
	public MovingBlob(){
		this.velocityX = 0;
		this.velocityY = 0;
		this.age = 0;
		this.ageOffScreen = 0;
	}
}