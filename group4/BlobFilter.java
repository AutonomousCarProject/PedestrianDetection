public class BlobFilter implements IMovingBlobReduction 
{
	public List<MovingBlob> blobs;
	
	public BlobFilter(List<MovingBlob> blobs)
	{
		this.blobs = blobs;
	}
	
	/**
	 * Checks the list of potential pedestrian blobs to distinguish pedestrians from non-pedestrians.
	 * Non-pedestrians are removed from the list of blobs.
	 * 
	 * @param blobs 	the list of potential pedestrian blobs
	 * @return			the list of blobs determined to be pedestrians
	 */
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		for (int i = 0; i < blobs.length(); i++)
		{
			if (!isPedestrian(blobs.get(i)))
			{
				blobs.remove(i--);
			}
		}
	}
	
	/**
	 * Checks an individual blob to determine if it is a pedestrian or non-pedestrian. Determination
	 * is made based on blob width vs. height, blob 'age' on screen, and blob xVelocity.
	 * 
	 * @param blob 		the blob being checked
	 * @return 			if the blob is a pedestrian
	 */
	public boolean isPedestrian(MovingBlob blob)
	{
		if (blob.width > blob.height) return false;
		if (blob.age < 10) return false;
		if (blob.movementX > 10) return false;
		return true;
	}
}