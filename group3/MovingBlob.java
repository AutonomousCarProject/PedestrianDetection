package group3;

import group2.Blob;

public class MovingBlob extends Blob {
    public float velocityX, velocityY;
    public float predictedX, predictedY;
    
    public int age;
    public int ageOffScreen;
	
	public MovingBlob(Blob b){
		super(b.width, b.height, b.centerX, b.centerY, b.color);
		this.velocityX = 0;
		this.velocityY = 0;
		this.age = 0;
		this.ageOffScreen = 0;
		updatePredictedPosition();
	}
	
	public void updatePredictedPosition(){
		predictedX = velocityX + centerX;
		predictedY = velocityY + centerY;
	}
}