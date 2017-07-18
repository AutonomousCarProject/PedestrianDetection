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
		MovingBlobDetectionTest movingtest = new MovingBlobDetectionTest();
		List<MovingBlob> list = movingtest.getMovingBlobs(test.getBlobs(null));	
		
		MovingBlobDetection detection = new MovingBlobDetection();
		TestBlobDetection testBlobDetection = new TestBlobDetection();
		detection.getMovingBlobs(testBlobDetection.getBlobs(null));
		detection.getMovingBlobs(testBlobDetection.getBlobs2(null));
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
