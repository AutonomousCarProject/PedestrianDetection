package group2;

import java.util.List;

import group1.IImage;
import group1.Image;

public class BlobDetectionDummyMain
{
    public static void main(String... args)
    {
        IImage image = new Image();
        image.readCam();
        IBlobDetection blobDetect = new BlobDetection2();

        // Runs once to pre-allocate blobs (simulating first frame startup
        // costs)
        blobDetect.getBlobs(image);

        long nanoTime = System.nanoTime();
        List<Blob> blobs = blobDetect.getBlobs(image);
        System.out.println(System.nanoTime() - nanoTime);

        for (Blob blob : blobs)
        {
            //@formatter:off
            
            System.out.printf("BLOB%n\tSize: %dx%d%n\tCenter: (%f,%f)%n\tColor: (%d, %d, %d)%n", blob.width,
                    blob.height, blob.centerX, blob.centerY, blob.color.getRed(), blob.color.getGreen(),
                    blob.color.getBlue());
            
            //@formatter:on
        }
    }
}
