package group3;
import group2.Blob;
import group2.TestBlobDetection;
import group2.TestPixel;
import group2.IBlobDetection;
import java.util.LinkedList;
import java.util.List;

public class MovingBlobDetectionTest implements IMovingBlobDetection {
	private List<MovingBlob> movingBlobs;

	public MovingBlobDetectionTest() {
		movingBlobs = new LinkedList<>();
	}

	public static void main(String[] args){
		TestBlobDetection test = new TestBlobDetection();
		MovingBlobDetection movingTest = new MovingBlobDetection();

		final long startTime = System.currentTimeMillis();
		List<MovingBlob> list = new LinkedList<>();
		list.add(new MovingBlob(10, 10, 20, 20, null, 25, 27));
		list.add(new MovingBlob(10, 10, 35, 35, null, 20, 20));
		MovingBlob movingBlob = movingTest.getUnifiedBlobs(list).get(0);
		List<MovingBlob> list2 = new LinkedList<>();
		System.out.println(movingBlob);
		System.out.println(movingBlob.width);
		System.out.println(movingBlob.height);

		list2.add(movingBlob);
		list2.add(new MovingBlob(2, 2, 45, 27, null, 19, 20));
		System.out.println(movingTest.getUnifiedBlobs(list2).get(0));
		

		final long endTime = System.currentTimeMillis();

		System.out.println("Total execution time: " + (endTime - startTime) );
	}

	// test data
	public List<MovingBlob> getMovingBlobs(List<Blob> blobList){
		for (Blob blob:blobList){
			this.movingBlobs.add(new MovingBlob(blob));
		}
		return movingBlobs;
	}

	public List<MovingBlob> getUnifiedBlobs(List<MovingBlob> blobs) {return blobs;}
}
