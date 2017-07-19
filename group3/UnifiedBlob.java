package group3;

import java.util.Collection;
import java.util.Set;

import group2.Blob;

public class UnifiedBlob extends MovingBlob {

	public UnifiedBlob(Set<MovingBlob> movingBlobs) {
		super();
		int numBlobs = movingBlobs.size();
		float totalVelocityX = 0;
		float totalVelocityY = 0;
		float totalCenterX = 0;
		float totalCenterY = 0;
		int top = 1000000;
		int bottom = 0;
		int left = 1000000;
		int right = 0;
		//find averages and other values
		for(MovingBlob movingBlob: movingBlobs){
			totalVelocityX +=movingBlob.velocityX;
			totalVelocityY +=movingBlob.velocityY;
			totalCenterX +=movingBlob.centerX;
			totalCenterY +=movingBlob.centerY;
			
		}
	}

}
