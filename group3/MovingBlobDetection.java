package group3;
import group2.Blob;
import group2.TestBlobDetection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class MovingBlobDetection implements IMovingBlobDetection {
	//list of all moving blobs that have been recently tracked
	private List<MovingBlob> movingBlobs;
	//maximum time before unmatched MovingBlob is deleted
	int maxTimeOffScreen = 40;
	//maximum distance in pixels between blobs that can be matched
	int distanceLimit = 30;
	//maximum distance between edges to unify
	int unifyDistanceLimitX = 25;
	int unifyDistanceLimitY = 60;

	//maximum difference in velocity to unify
	int unifyVelocityLimitX = 20;
	int unifyVelocityLimitY = 40;


	public MovingBlobDetection() {
		movingBlobs = new LinkedList<>();
	}

	public List<MovingBlob> getUnifiedBlobs(List<MovingBlob> movingBlobs){
		HashSet<BlobPair> pairs = new HashSet<>();
		for(MovingBlob movingBlob1:movingBlobs){
			for(MovingBlob movingBlob2:movingBlobs){
				float distanceX;
				float distanceY;
				if(movingBlob1.x>movingBlob2.x){
					distanceX = movingBlob1.x-(movingBlob2.x+movingBlob2.width);
				} else {
					distanceX = movingBlob2.x-(movingBlob1.x+movingBlob1.width);
				}
				if(movingBlob1.y>movingBlob2.y){
					distanceY = movingBlob1.y-(movingBlob2.y+movingBlob2.height);
				} else {
					distanceY = movingBlob2.y-(movingBlob1.y+movingBlob1.height);
				}
				float velocityDifferenceX = Math.abs(movingBlob1.velocityX-movingBlob2.velocityX);
				float velocityDifferenceY = Math.abs(movingBlob1.velocityY-movingBlob2.velocityY);

				if(distanceX<unifyDistanceLimitX && distanceY<unifyDistanceLimitY &&
						velocityDifferenceX<unifyVelocityLimitX && velocityDifferenceY<unifyVelocityLimitY){
					pairs.add(new BlobPair(0, movingBlob1, movingBlob2));
				}
			}
		}
		HashMap<MovingBlob, UnifiedBlob> map = new HashMap<>();
		for(BlobPair pair:pairs){
			MovingBlob blob1 = pair.oldBlob;
			MovingBlob blob2 = (MovingBlob) pair.newBlob;
			MovingBlob unifiedBlob1 = map.get(blob1);
			if(unifiedBlob1==null){
				unifiedBlob1 = blob1;
			}
			MovingBlob unifiedBlob2 = map.get(blob2);
			if(unifiedBlob2==null){
				unifiedBlob2 = blob2;
			}
			if(unifiedBlob1!=unifiedBlob2){
				HashSet<MovingBlob> blobSet = new HashSet<>();
				blobSet.add(unifiedBlob1);
				blobSet.add(unifiedBlob2);
				UnifiedBlob newUnifiedBlob = new UnifiedBlob(blobSet);
				map.put(blob1,newUnifiedBlob);
				map.put(blob2,newUnifiedBlob);
			}
		}
		HashSet<UnifiedBlob> unifiedBlobSet = new HashSet<>();
		for(MovingBlob blob:map.values()){
			unifiedBlobSet.add((UnifiedBlob) blob);
		}
		return new LinkedList<>(unifiedBlobSet);
	}

	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		updateMovingBlobs(blobList);
		return movingBlobs;
	}

	private void updateMovingBlobs(List<Blob> blobList){
		//set of unmatched movingblobs (all are unmatched at start of frame)
		HashSet<MovingBlob> movingBlobSet = new HashSet<>(movingBlobs);
		//set of unmatched blobs
		HashSet<Blob> blobSet = new HashSet<>(blobList);
		//queue with shortest distance pairs of movingblobs and blobs in front
		PriorityQueue<BlobPair> queue = new PriorityQueue<>();
		for(Blob blob:blobList){
			for(MovingBlob movingBlob:movingBlobs){
				//creates pairs in queue of blobs & moving blobs with same color within 100 pixels
				if(blob.color.getColor()==movingBlob.color.getColor()){
					float distanceX = Math.abs(movingBlob.predictedX-blob.x);
					float distanceY = Math.abs(movingBlob.predictedY-blob.y);
					float distance = (float)Math.sqrt(distanceX*distanceX+distanceY*distanceY);
					if(distance<distanceLimit){
						queue.add(new BlobPair(distance, blob, movingBlob));
					}
				}
			}
		}
		//matches closest pairs until it runs out of movingBlobs, blobs, or pairs
		while(!movingBlobSet.isEmpty()&&!blobSet.isEmpty()&&!queue.isEmpty()){
			//finds shortest pair in queue
			BlobPair pair = queue.peek();
			//checks if neither blobs are matched already
			if(movingBlobSet.contains(pair.oldBlob)&&blobSet.contains(pair.newBlob)){
				//matches blobs and updates sets and queue
				matchBlob(pair.oldBlob, pair.newBlob);
				movingBlobSet.remove(pair.oldBlob);
				blobSet.remove(pair.newBlob);
				queue.remove();
			} else {
				//if either blob is matched, removes pair from queue
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
		//update information based on new position
		calculateVelocity(movingBlob, newBlob);
		movingBlob.x = newBlob.x;
		movingBlob.y = newBlob.y;
		movingBlob.age++;
		movingBlob.ageOffScreen=0;
		movingBlob.updatePredictedPosition();	
	}

	private void updateUnmatched(MovingBlob movingBlob){

		if(movingBlob.ageOffScreen>maxTimeOffScreen){
			//removes blob if it has been gone too long
			movingBlobs.remove(movingBlob);
		} else {
			//update position based on most recent velocity
			movingBlob.x += movingBlob.velocityX;
			movingBlob.y += movingBlob.velocityY;

      movingBlob.age++;
			movingBlob.ageOffScreen++;
			movingBlob.updatePredictedPosition();
		}
	}

	private void calculateVelocity(MovingBlob movingBlob, Blob newBlob){
		float movementX = newBlob.x - movingBlob.x;
		float movementY = newBlob.y - movingBlob.y;
		//finds average of previous velocity and velocity between last and current frame
		movingBlob.velocityX += movementX;
		movingBlob.velocityX /= 2;
		movingBlob.velocityY += movementY;
		movingBlob.velocityY /= 2;
	}
}
