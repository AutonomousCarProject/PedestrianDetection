package group4;

import group3.MovingBlob;
import group4.IMovingBlobReduction;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class BlobFilter implements IMovingBlobReduction
{
	/**
	/*
	 * Moving Blob filters
	 */
	//Minimum age to not be filtered
	private static final short AGE_MIN = 3;
	//Maximum 
	private static final short VELOCITY_X_MAX = 20;
	
	private static final short VELOCITY_Y_MAX = 10;

	private static final float MAX_VELOCITY_CHANGE_X = 3;

	private static final float MAX_VELOCITY_CHANGE_Y = 2;

	
	/*
	 * Unified Blob filters
	 */

	private static final short MAX_WIDTH_HEIGHT_RATIO = 1;

	private static final short MAX_WIDTH = 100;

	private static final short MAX_HEIGHT = 200;

	private static final short MAX_SCALED_VELOCITY_X = 10;

	private static final short MAX_SCALED_VELOCITY_Y = 10;
	
	/**
	 * Checks the list of potential pedestrian blobs to distinguish pedestrians from non-pedestrians.
	 * Non-pedestrians are removed from the list of blobs.
	 * 
	 * @param blobs 	the list of potential pedestrian blobs
	 * @return			the list of blobs determined to be pedestrians
	 */
	/*public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		return blobs.parallelStream().filter(p -> isPedestrian(p)).collect(Collectors.toList());
	}*/
	
	/**
	 * Checks an individual blob to determine if it is a pedestrian or non-pedestrian. Determination
	 * is made based on blob width vs. height, blob 'age' on screen, and blob xVelocity.
	 * 
	 * @param blob 		the blob being checked
	 * @return 			if the blob is a pedestrian
	 */
	/*private boolean isPedestrian(MovingBlob blob)
	{
		//lol formatting wut
		return  blob.width >= DIMENSION_MIN && blob.height >= DIMENSION_MIN
				&& (float)blob.width / (float)blob.height <= WIDTH_HEIGHT_RATIO_MAX
				&& (blob.age >= AGE_MIN || (blob.x + blob.width/2 >= CENTER_CHECK_WIDTH / 2
											&& blob.x + blob.width/2 <= 640 - CENTER_CHECK_WIDTH / 2
											&& blob.y + blob.height/2 >= CENTER_CHECK_HEIGHT / 2
											&& blob.y + blob.height/2 <= 480 - CENTER_CHECK_HEIGHT / 2
											&& blob.velocityX >= CENTER_X_VELOCITY_MIN))
				&& Math.abs(blob.velocityX) <= X_VELOCITY_MAX
				&& blob.predictedX >= PREDICTED_BORDER_DISTANCE_MIN && blob.predictedX <= (640 - PREDICTED_BORDER_DISTANCE_MIN)
				&& blob.predictedY >= PREDICTED_BORDER_DISTANCE_MIN && blob.predictedY <= (480 - PREDICTED_BORDER_DISTANCE_MIN);
	}*/

	public List<MovingBlob> reduce(List<MovingBlob> blobs){
		return filterUnifiedBlobs(blobs);
	}

	public List<MovingBlob> filterMovingBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(filterMovingBlob(blob)) ret.add(blob);
		}
		return ret;
	}
	
	//returns false if blob should be filtered
	private boolean filterMovingBlob(MovingBlob blob){
		return blob.age > AGE_MIN &&
				Math.abs(blob.velocityY) < VELOCITY_Y_MAX &&
				Math.abs(blob.velocityX) < VELOCITY_X_MAX &&
				blob.velocityChangeX < MAX_VELOCITY_CHANGE_X &&
				blob.velocityChangeY < MAX_VELOCITY_CHANGE_Y;
	}
	
	public List<MovingBlob> filterUnifiedBlobs(List<MovingBlob> blobs){
		List<MovingBlob> ret = new LinkedList<>();
		for(MovingBlob blob : blobs){
			if(filterUnifiedBlob(blob)) ret.add(blob);
		}
		return ret;
	}
	
	private boolean filterUnifiedBlob(MovingBlob blob){
		return (float)blob.width / (float)blob.height < MAX_WIDTH_HEIGHT_RATIO &&
				blob.width < MAX_WIDTH &&
				blob.height < MAX_HEIGHT &&
				blob.getScaledVelocityX() < MAX_SCALED_VELOCITY_X &&
				blob.getScaledVelocityY() < MAX_SCALED_VELOCITY_Y;
	}
	
}