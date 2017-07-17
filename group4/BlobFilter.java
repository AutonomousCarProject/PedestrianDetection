public class BlobFilter implements IMovingBlobReduction 
{
	public List<MovingBlob> blobs;
	
	public BlobFilter(List<MovingBlob> blobs)
	{
		this.blobs = blobs;
	}
	
	public List<MovingBlob> reduce(List<MovingBlob> blobs)
	{
		for (Blob blob : blobs)
		{
			if (!isPerson(blob))
			{
				blobs.remove(blob);
			}
		}
	}
	
	public boolean isPerson(Blob blob)
	{
		if (blob.width > blob.height)
		{
			return false;
		}
		
		
	}
}