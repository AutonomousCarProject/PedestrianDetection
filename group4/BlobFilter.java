public class BlobFilter implements IMovingBlobReduction 
{
	public List<MovingBlob> blobs;
	
	public BlobFilter(List<MovingBlob> blobs)
	{
		this.blobs = blobs;
	}
	
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		for (int i = 0; i < blobs.length(); i++)
		{
			if (!isPerson(blobs.get(i)))
			{
				blobs.remove(i--);
			}
		}
	}
	
	public boolean isPerson(MovingBlob blob)
	{
		if (blob.width > blob.height) return false;
		if (blob.age < 10) return false;
		if (blob.movementX > 10) return false;
		return true;
	}
}