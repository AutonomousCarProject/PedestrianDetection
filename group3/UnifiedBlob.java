package group3;

import java.util.Collection;
import java.util.Set;

import group2.Blob;

public class UnifiedBlob extends MovingBlob {

	public UnifiedBlob(Set<MovingBlob> movingBlobs) {
		super();
		int numBlobs = movingBlobs.size();
		int minAgeOffScreen = 100000;
		int maxAge = 0;
		float totalVelocityX = 0;
		float totalVelocityY = 0;
		float top = 1000000;
		float bottom = 0;
		float left = 1000000;
		float right = 0;
		//find averages and other values
		for(MovingBlob movingBlob: movingBlobs){
			minAgeOffScreen = Math.min(minAgeOffScreen, movingBlob.ageOffScreen);
			maxAge = Math.min(age, movingBlob.age);
			
			totalVelocityX += movingBlob.velocityX;
			totalVelocityY += movingBlob.velocityY;
			
			float blobRightSide = movingBlob.x + movingBlob.width;
			if (blobRightSide > right) right = blobRightSide;
			
			float blobLeftSide = movingBlob.x;
			if (blobLeftSide < left) left = blobLeftSide;
			
			float blobTop = movingBlob.y;
			if (blobTop < top) top = blobTop;
			
			float blobBottom = movingBlob.y + movingBlob.height;
			if (blobBottom > bottom) bottom = blobBottom;
		}

		this.age = maxAge;
		this.ageOffScreen = minAgeOffScreen;


		this.velocityX = totalVelocityX/numBlobs;
		this.velocityY = totalVelocityY/numBlobs;
		this.x = (int) (left);
		this.y = (int) (top);
		
		this.width = (int)(right-left);
		this.height = (int)(bottom-top);
		
		this.updatePredictedPosition();
	}

}
