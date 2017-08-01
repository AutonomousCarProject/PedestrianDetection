package group3;
import group2.Blob;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import global.Constant;

public class MovingBlobDetection implements IMovingBlobDetection {
	Constant c = new Constant();
	
	//list of all moving blobs that have been recently tracked
	private List<MovingBlob> movingBlobs;
	//maximum time before unmatched MovingBlob is deleted
	int maxTimeOffScreen = c.MAX_TIME_OFF_SCREEN;
	//maximum distance in pixels between blobs that can be matched
	int distanceLimitX = c.DISTANCE_LIMIT_X;
	int distanceLimitY = c.DISTANCE_LIMIT_Y;
	int widthChangeLimit = c.MAX_CHANGE_WIDTH;
	int heightChangeLimit = c.MAX_CHANGE_HEIGHT;
	
	//maximum distance between edges to unify
	int xEdgeDistanceLimit = c.X_EDGE_DISTANCE_LIMIT;
	int yEdgeDistanceLimit = c.Y_EDGE_DISTANCE_LIMIT;
	float xOverlapPercent = c.X_OVERLAP_PERCENT;
	float yOverlapPercent = c.Y_OVERLAP_PERCENT;

	//maximum difference in velocity to unify
	int unifyVelocityLimitX = c.UNIFY_VELOCITY_LIMIT_X;
	int unifyVelocityLimitY = c.UNIFY_VELOCITY_LIMIT_Y;
	float velocityLimitIncreaseX = c.VELOCITY_LIMIT_INCREASE_X;
	float velocityLimitIncreaseY = c.VELOCITY_LIMIT_INCREASE_Y;

	public MovingBlobDetection() {
		movingBlobs = new LinkedList<>();
	}

	public List<MovingBlob> getUnifiedBlobs(List<MovingBlob> movingBlobs){
		//pairs that should be unified
		HashSet<MovingBlob[]> pairs = new HashSet<>();
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
				//checks if distance and velocity differences are under thresholds
				if(((distanceX<xEdgeDistanceLimit && distanceY<-yOverlapPercent*Math.min(movingBlob1.height, movingBlob2.height)) 
						|| (distanceY<yEdgeDistanceLimit && distanceX<-xOverlapPercent*Math.min(movingBlob1.width, movingBlob2.width)))&&
						velocityDifferenceX<unifyVelocityLimitX+
						velocityLimitIncreaseX*Math.max(movingBlob1.velocityX, movingBlob2.velocityX) &&
						velocityDifferenceY<unifyVelocityLimitY+
						velocityLimitIncreaseY*Math.max(movingBlob1.velocityY, movingBlob2.velocityY)){
					MovingBlob[] pair = {movingBlob1,movingBlob2};
					pairs.add(pair);
				}
			}
		}
		HashMap<MovingBlob, HashSet<MovingBlob>> map = new HashMap<>();
		for(MovingBlob[] pair:pairs){
			MovingBlob blob1 = pair[0];
			MovingBlob blob2 = pair[1];
			HashSet<MovingBlob> set1 = map.get(blob1);
			HashSet<MovingBlob> set2 = map.get(blob2);
			if(set1==null&&set2==null){
				HashSet<MovingBlob> newSet = new HashSet<>();
				newSet.add(blob1);
				newSet.add(blob2);
				map.put(blob1, newSet);
				map.put(blob2, newSet);
			} else if(set1==null){
				set2.add(blob1);
				map.put(blob1,set2);
			} else if(set2==null){
				set1.add(blob2);
				map.put(blob2,set1);
			} else {
				set1.addAll(set2);
				map.put(blob2,set1);
			}
		}
		HashSet<MovingBlob> unifiedBlobSet = new HashSet<>();
		for(HashSet<MovingBlob> blobSet:map.values()){
			unifiedBlobSet.add(new UnifiedBlob(blobSet));
		}
		for(MovingBlob blob:movingBlobs){
			if(map.get(blob)==null) unifiedBlobSet.add(blob);
		}
		return new LinkedList<>(unifiedBlobSet);
	}

	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		updateMovingBlobs(blobList);
		return movingBlobs;
	}

	private void updateMovingBlobs(List<Blob> blobList){
		//set of unmatched movingblobs (all are unmatched at start of frame)
		HashSet<MovingBlob> movingBlobSet = new HashSet<>(getMovingBlobs());
		//set of unmatched blobs
		HashSet<Blob> blobSet = new HashSet<>(blobList);
		//queue with shortest distance pairs of movingblobs and blobs in front
		PriorityQueue<BlobPair> queue = new PriorityQueue<>();
		for(Blob blob:blobList){
			for(MovingBlob movingBlob:getMovingBlobs()){
				//creates pairs in queue of blobs & moving blobs with same color within 100 pixels
				if(blob.color.getColor()==movingBlob.color.getColor()){
					float distanceX = Math.abs(movingBlob.predictedX-(blob.x+blob.width/2));
					float distanceY = Math.abs(movingBlob.predictedY-(blob.y+blob.height/2));
					float distance = (float)Math.sqrt(distanceX*distanceX+distanceY*distanceY);
					float widthChange = Math.abs(movingBlob.width-blob.width);
					float heightChange = Math.abs(movingBlob.height-blob.height);
					if(distanceX<=distanceLimitX && distanceY<=distanceLimitY &&
							widthChange<=widthChangeLimit && heightChange<=heightChangeLimit){
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
			getMovingBlobs().add(new MovingBlob(blob));
		}
	}

	private void matchBlob(MovingBlob movingBlob, Blob newBlob){		
		//update information based on new position
		calculateVelocity(movingBlob, newBlob);
		movingBlob.x = newBlob.x;
		movingBlob.y = newBlob.y;
		movingBlob.width = newBlob.width;
		movingBlob.height = newBlob.height;
		movingBlob.age++;
		movingBlob.ageOffScreen=0;
		movingBlob.updatePredictedPosition();	
	}

	private void updateUnmatched(MovingBlob movingBlob){

		if(movingBlob.ageOffScreen>=maxTimeOffScreen){
			//removes blob if it has been gone too long
			getMovingBlobs().remove(movingBlob);
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
		float centerXOld = movingBlob.x + movingBlob.width/2;
		float centerYOld = movingBlob.y + movingBlob.height/2;
		float centerXNew = newBlob.x + newBlob.width/2;
		float centerYNew = newBlob.y + newBlob.width/2;
		float movementX = centerXNew - centerXOld;
		float movementY = centerYNew - centerYOld;

		float tempVelX = movingBlob.velocityX;
		float tempVelY = movingBlob.velocityY;
		//finds average of previous velocity and velocity between last and current frame

		movingBlob.velocityX += movementX;
		movingBlob.velocityX /= 2;
		movingBlob.velocityChangeX = Math.abs(tempVelX-movingBlob.velocityX);


		//System.out.println("Velocity change x: " + movingBlob.velocityChangeX);


		movingBlob.velocityY += movementY;
		movingBlob.velocityY /= 2;
		movingBlob.velocityChangeY = Math.abs(tempVelY-movingBlob.velocityY);
		//System.out.println("Velocity change y: " + movingBlob.velocityChangeY);
		//System.out.println("new velY: " + movingBlob.velocityY);

	}

	public List<MovingBlob> getMovingBlobs() {
		return movingBlobs;
	}

	public void setMovingBlobs(List<MovingBlob> movingBlobs) {
		this.movingBlobs = movingBlobs;
	}
}