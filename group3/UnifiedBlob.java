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
		float top = 1000000;
		float bottom = 0;
		float left = 1000000;
		float right = 0;
		//find averages and other values
		for(MovingBlob movingBlob: movingBlobs){
			totalVelocityX += movingBlob.velocityX;
			totalVelocityY += movingBlob.velocityY;
			totalCenterX += movingBlob.x;
			totalCenterY += movingBlob.y;
			
			float blobRightSide = movingBlob.x + movingBlob.width/2;
			if (blobRightSide > right) right = blobRightSide;
			
			float blobLeftSide = movingBlob.x - movingBlob.width/2;
			if (blobLeftSide < left) left = blobLeftSide;
			
			float blobTop = movingBlob.y - movingBlob.height/2;
			if (blobTop < top) top = blobTop;
			
			float blobBottom = movingBlob.y + movingBlob.height/2;
			if (blobBottom > bottom) bottom = blobBottom;
		}
		System.out.println("top:" + top);
		System.out.println("bottom:" + bottom);
		System.out.println("left:" + left);
		System.out.println("right:" + right);

		this.velocityX = totalVelocityX/numBlobs;
		this.velocityY = totalVelocityY/numBlobs;
		this.x = (int) Math.round(totalCenterX/numBlobs);
		this.y = (int) Math.round(totalCenterY/numBlobs);
		
		this.width = (int)(right-left);
		this.height = (int)(bottom-top);
		
		this.updatePredictedPosition();
	}

}
