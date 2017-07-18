package group3;
import group2.Blob;
import group2.TestBlobDetection;
import group2.TestPixel;
import group2.IBlobDetection;
import java.util.LinkedList;
import java.util.List;

public class MovingBlobDetectionTest implements IMovingBlobDetection {
	private List<MovingBlob> movingBlobs;
	
	public static void main(String[] args){
		TestBlobDetection test = new TestBlobDetection();
		MovingBlobDetection movingtest = new MovingBlobDetection();
		
		final long startTime = System.currentTimeMillis();
		List<MovingBlob> list = movingtest.getMovingBlobs(test.getBlobs(null));	
		for (int i = 0; i < 1000; i++) {
			list = movingtest.getMovingBlobs(test.getBlobs(null));
		}
		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );
	}
	
	public MovingBlobDetectionTest() {
		movingBlobs = new LinkedList<>();
	}
	
	// test data
	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		for (Blob blob:blobList){
			this.movingBlobs.add(new MovingBlob(blob));
		}
		return movingBlobs;
	}
}
