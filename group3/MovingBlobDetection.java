package group3;
import group2.Blob;
import java.util.LinkedList;
import java.util.List;

public class MovingBlobDetection implements IMovingBlobDetection {
	private LinkedList<MovingBlob> movingBlobs;

	public List<MovingBlob> getMovingBlobs(){
		return movingBlobs;
	}
	
	public void updateMovingBlobs(List<Blob> blobList){
		//match blobs with moving blobs
		
	}
	
	private void calculateVelocity(MovingBlob movingBlob, Blob newBlob){
		int movementX = newBlob.centerX - movingBlob.centerX;
		int movementY = newBlob.centerY - movingBlob.centerY;
		
		movingBlob.movementX += movementX;
		movingBlob.movementX /= 2;
		movingBlob.movementY += movementY;
		movingBlob.movementY /= 2;
	}
}
