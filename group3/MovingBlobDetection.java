package group3;
import group2.Blob;
import java.util.LinkedList;
import java.util.List;

public class MovingBlobDetection implements IMovingBlobDetection {
	private LinkedList<MovingBlob> movingBlobs;

	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		updateMovingBlobs(blobList);
		return movingBlobs;
	}
	
	private void updateMovingBlobs(List<Blob> blobList){
		//match blobs with moving blobs
		
	}
	
	private void calculateVelocity(MovingBlob movingBlob, Blob newBlob){
		int movementX = newBlob.centerX - movingBlob.centerX;
		int movementY = newBlob.centerY - movingBlob.centerY;
		
		movingBlob.velocityX += movementX;
		movingBlob.velocityX /= 2;
		movingBlob.velocityY += movementY;
		movingBlob.velocityY /= 2;
	}
}
