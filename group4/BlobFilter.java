package group4;

import group3.MovingBlob;
import group4.IMovingBlobReduction;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlobFilter implements IMovingBlobReduction
{
	/**
	 * Constant thresholds which do all the magic
	 * All inclusive thresholds
	 */

	//the maximum ratio of w/h the blob can be to be considered valid
	private static final double WIDTH_HEIGHT_RATIO_MAX = 1;
	//minimum dimensions size for the blob
	private static final int DIMENSION_MIN = 10;
	//the minimum age (in frames) the blob must be to be considered valid
	//OR
	//the minimum velocity for an object in the center
	private static final float CENTER_X_VELOCITY_MIN = (int)(1.0f / 15.0f * 32.0f);
	//and the width and height of the box to check that velocity
	private static final int CENTER_CHECK_WIDTH = 200;
	private static final int CENTER_CHECK_HEIGHT = 100;
	//the maximum X velocity the blob can have to be considered valid (6 m/s converted to px/frame)
	//the minimum distance from the top, left, or right border the predicted position of the blob must be in order to be considered (in px)
	private static final short PREDICTED_BORDER_DISTANCE_MIN = 20;
	
	/*
	 * Moving Blob filters
	 */
	//Minimum age to not be filtered
	private static final short AGE_MIN = 10;
	//Maximum 
	private static final short VELOCITY_X_MAX = 30;
	
	private static final short VELOCITY_Y_MAX = 10;
	
	/*
	 * Unified Blob filters
	 */
	
	/**
	 * Checks the list of potential pedestrian blobs to distinguish pedestrians from non-pedestrians.
	 * Non-pedestrians are removed from the list of blobs.
	 * 
	 * @param blobs 	the list of potential pedestrian blobs
	 * @return			the list of blobs determined to be pedestrians
	 */
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		return blobs.parallelStream().filter(p -> isPedestrian(p)).collect(Collectors.toList());
	}
	
	/**
	 * Checks an individual blob to determine if it is a pedestrian or non-pedestrian. Determination
	 * is made based on blob width vs. height, blob 'age' on screen, and blob xVelocity.
	 * 
	 * @param blob 		the blob being checked
	 * @return 			if the blob is a pedestrian
	 */
	private boolean isPedestrian(MovingBlob blob)
	{
		//lol formatting wut
		return  blob.width >= DIMENSION_MIN && blob.height >= DIMENSION_MIN
				&& blob.width / blob.height <= WIDTH_HEIGHT_RATIO_MAX
				&& (blob.age >= AGE_MIN || (blob.x + blob.width/2 >= CENTER_CHECK_WIDTH / 2
											&& blob.x + blob.width/2 <= 640 - CENTER_CHECK_WIDTH / 2
											&& blob.y + blob.height/2 >= CENTER_CHECK_HEIGHT / 2
											&& blob.y + blob.height/2 <= 480 - CENTER_CHECK_HEIGHT / 2
											&& blob.velocityX >= CENTER_X_VELOCITY_MIN))
				&& Math.abs(blob.velocityX) <= X_VELOCITY_MAX
				&& blob.predictedX >= PREDICTED_BORDER_DISTANCE_MIN && blob.predictedX <= (640 - PREDICTED_BORDER_DISTANCE_MIN)
				&& blob.predictedY >= PREDICTED_BORDER_DISTANCE_MIN && blob.predictedY <= (480 - PREDICTED_BORDER_DISTANCE_MIN);
	}
	
	public List<MovingBlob> filterMovingBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(!filterMovingBlob(blob)) ret.add(blob);
		}
		return ret;
	}
	
	//returns true if blob should be filtered
	private boolean filterMovingBlob(MovingBlob blob){
		boolean passes = true;
		//age filter
		if(blob.age<=AGE_MIN){
			passes = false;
		}
		if(blob.velocityY>=VELOCITY_Y_MAX){
			passes = false;
		}
		if(blob.velocityX>=VELOCITY_Y_MAX){
			passes = false;
		}
	}
	
	public List<MovingBlob> filterUnifiedBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(!filterUnifiedBlobs(blob)) ret.add(blob);
		}
	}
	
	private boolean filterMovingBlob(MovingBlob blob){
		
	}
	
}