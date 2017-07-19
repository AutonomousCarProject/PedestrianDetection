package group3;

import group2.Blob;

public class MovingBlob extends Blob {
	
	// The X and Y components of the MovingBlob's velocity.
    public float velocityX, velocityY;
    
	// The predicted X and Y coordinates of the MovingBlob in the next frame.
    public float predictedX, predictedY;
    
    // The time, in frames, that the MovingBlob has been on-screen.
    public int age;
    
    // The time, in frames, that the MovingBlob has been off-screen.
    public int ageOffScreen;
	
    /**
     * Creates a MovingBlob from a Blob. This is used when a previously unseen Blob
     * comes on-screen.
     * 
     * @param b The Blob that the MovingBlob will be created from.
     */
	public MovingBlob(Blob b){
		super(b.width, b.height, b.centerX, b.centerY, b.color);
		this.velocityX = 0;
		this.velocityY = 0;
		this.age = 15;
		this.ageOffScreen = 0;
		updatePredictedPosition();
	}
	
	/**
	 * Updates the predicted position of the MovingBlob based on its center and
	 * velocity.
	 */
	public void updatePredictedPosition(){
		predictedX = velocityX + centerX;
		predictedY = velocityY + centerY;
		System.out.println(predictedX);
		System.out.println(predictedY);
	}
	
	public String toString(){
		return "Moving blob: Color " + color.getColor() + " X: " + centerX + " Y: " + centerY;
	}
}