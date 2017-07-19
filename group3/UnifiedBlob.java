package group3;

import java.util.Collection;
import java.util.Set;

import group2.Blob;

public class UnifiedBlob extends MovingBlob {
	
	// The X and Y components of the MovingBlob's velocity.
    public float velocityX, velocityY;
    
	// The predicted X and Y coordinates of the MovingBlob in the next frame.
    public float predictedX, predictedY;
    
    // The time, in frames, that the MovingBlob has been on-screen.
    public int age;
    
    // The time, in frames, that the MovingBlob has been off-screen.
    public int ageOffScreen;
    
    public int width, height;
    public float centerX, centerY;

	public UnifiedBlob(Set<MovingBlob> movingBlobs) {
		super();
		int numBlobs = movingBlobs.size();
		float totalVelocityX;
		float totalVelocityY;
		float totalCenterX;
		float totalCenterY;
		//find average
		
		
	}

}
