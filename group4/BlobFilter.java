package group4;

import group3.MovingBlob;
import group4.IMovingBlobReduction;

import java.util.List;

public class BlobFilter implements IMovingBlobReduction
{
	/**
	 * Constant thresholds which do all the magic
	 * All inclusive thresholds
	 */

	//the maximum ratio of w/h the blob can be to be considered valid
	private static final double WIDTH_HEIGHT_RATIO_MAX = 1;
	//the minimum age (in frames) the blob must be to be considered valid
	private static final short AGE_MIN = 10;
	//the maximum X velocity the blob can have to be considered valid
	private static final short X_VELOCITY_MAX = 10;
	//the rectangle around the top, right, and left border in which to check if the blob will probably leave the frame (in px)
	private static final short BORDER_VELOCITY_CHECK_MAX = 20;
	//the maximum angle difference the velocity vector can be from perpendicular to the border during the border velocity check (in degrees)
	private static final short BORDER_VELOCITY_CHECK_ANGLE_MAX = 30;
	
	/**
	 * Checks the list of potential pedestrian blobs to distinguish pedestrians from non-pedestrians.
	 * Non-pedestrians are removed from the list of blobs.
	 * 
	 * @param blobs 	the list of potential pedestrian blobs
	 * @return			the list of blobs determined to be pedestrians
	 */
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		for (int i = 0; i < blobs.size(); i++)
		{
			if (!isPedestrian(blobs.get(i)) blobs.remove(i--);

		}
		return blobs;
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
		return  blob.width <= blob.height * WIDTH_HEIGHT_RATIO_MAX
				&& blob.age >= AGE_MIN
				&& Math.abs(blob.movementX) <= X_VELOCITY_MAX;
	}

	/**
	 * Checks an ind. blobs velocity to see if the blob is unlikley to remain in the frame, thereby not important
	 * Made by calculating the angle, looking at a position threshold, and then making a decision based on those values
	 * @param movementX     The blobs X velocity
	 * @param movmentY      The blobs Y velocity
	 * @return              Whether or not the blob is important
	 */
	private boolean checkVelocity(int centerX, int centerY, float movementX, float movmentY)
	{
		//check position threshold
		//left, top, right
		double angle = Math.toDegrees(Math.atan2(movmentY, movementX));
		return !((centerX < BORDER_VELOCITY_CHECK_MAX && Math.abs(angle - 180) < BORDER_VELOCITY_CHECK_ANGLE_MAX)
				|| (centerY < BORDER_VELOCITY_CHECK_MAX && Math.abs(angle - 90) < BORDER_VELOCITY_CHECK_ANGLE_MAX)
				|| (centerX - (640 - BORDER_VELOCITY_CHECK_MAX) > 0 && Math.abs(angle) < BORDER_VELOCITY_CHECK_ANGLE_MAX));
	}
}