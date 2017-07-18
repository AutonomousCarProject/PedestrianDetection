package group3;
import group2.Blob;
import group2.TestBlobDetection;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MovingBlobDetection implements IMovingBlobDetection {
	private List<MovingBlob> movingBlobs;
	//maximum time before unmatched MovingBlob is deleted
	int maxTimeOffScreen = 30;
	
	public static void main(String[] args){
		TestBlobDetection test = new TestBlobDetection();
		MovingBlobDetection movingtest = new MovingBlobDetection();
		
		final long startTime = System.currentTimeMillis();
		List<MovingBlob> list = movingtest.getMovingBlobs(test.getBlobs(0, null));	
		
		for (int i = 0; i < 4; i++) {
			list = movingtest.getMovingBlobs(test.getBlobs(i+1, null));
			
		}

		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );
	}
	
	public MovingBlobDetection() {
		movingBlobs = new LinkedList<>();
	}
	
	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		updateMovingBlobs(blobList);
		return movingBlobs;
	}
	
	//test data is in "MovingBlobDetectionTest.java"
	
	private void updateMovingBlobs(List<Blob> blobList){
		//maximum distance where blobs can be matched
		int distanceLimit = 100;
		HashSet<MovingBlob> movingBlobSet = new HashSet<>(movingBlobs);
		HashSet<Blob> blobSet = new HashSet<>(blobList);
		//queue with smallest distance pairs in front
		PriorityQueue<BlobPair> queue = new PriorityQueue<>();
		for(Blob blob:blobList){
			for(MovingBlob movingBlob:movingBlobs){
				//creates pairs in queue of blobs & moving blobs with same color within 100 pixels
				if(blob.color.getColor()==movingBlob.color.getColor()){
					float distanceX = Math.abs(movingBlob.predictedX-blob.centerX);
					float distanceY = Math.abs(movingBlob.predictedY-blob.centerY);
					float distance = (float)Math.sqrt(distanceX*distanceX+distanceY*distanceY);
					if(distance<distanceLimit){
						queue.add(new BlobPair(distance, blob, movingBlob));
					}
				}
			}
		}
		//matches closest pairs until it runs out of movingBlobs, blobs, or pairs
		while(!movingBlobSet.isEmpty()&&!blobSet.isEmpty()&&!queue.isEmpty()){
			BlobPair pair = queue.peek();
			if(movingBlobSet.contains(pair.oldBlob)&&blobSet.contains(pair.newBlob)){
				matchBlob(pair.oldBlob, pair.newBlob);
				movingBlobSet.remove(pair.oldBlob);
				blobSet.remove(pair.newBlob);
				queue.remove();
			} else {
				queue.remove();
			}
		}
		//updates unmatched MovingBlobs
		for(MovingBlob movingBlob:movingBlobSet){
			updateUnmatched(movingBlob);
		}
		//creates new MovingBlobs for unmatched blobs
		for(Blob blob:blobSet){
			movingBlobs.add(new MovingBlob(blob));
		}
	}
	
	private void matchBlob(MovingBlob movingBlob, Blob newBlob){
		System.out.println(movingBlob);
		System.out.println(new MovingBlob(newBlob));
		System.out.println();
		
		//update information based on new position
		calculateVelocity(movingBlob, newBlob);
		movingBlob.centerX = newBlob.centerX;
		movingBlob.centerY = newBlob.centerY;
		movingBlob.age++;
		movingBlob.ageOffScreen=0;
		movingBlob.updatePredictedPosition();	
	}
	
	private void updateUnmatched(MovingBlob movingBlob){

		if(movingBlob.ageOffScreen>maxTimeOffScreen){
			movingBlobs.remove(movingBlob);
		} else {
			//update position based on velocity
			movingBlob.centerX += movingBlob.velocityX;
			movingBlob.centerY += movingBlob.velocityY;
			movingBlob.age++;
			movingBlob.ageOffScreen++;
			movingBlob.updatePredictedPosition();
		}
	}
	
	private void calculateVelocity(MovingBlob movingBlob, Blob newBlob){
		float movementX = newBlob.centerX - movingBlob.centerX;
		float movementY = newBlob.centerY - movingBlob.centerY;
		//finds average of previous velocity and velocity between last and current frame
		movingBlob.velocityX += movementX;
		movingBlob.velocityX /= 2;
		movingBlob.velocityY += movementY;
		movingBlob.velocityY /= 2;
	}
}
