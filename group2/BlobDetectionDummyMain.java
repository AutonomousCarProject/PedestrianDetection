package group2;

import java.util.List;

import group1.Dummy3;
import group1.IImage;

public class BlobDetectionDummyMain
{
    public static void main(String... args)
    {
        IImage image = new Dummy3();
        IBlobDetection blobDetect = new BlobDetection();

        List<Blob> blobs = blobDetect.getBlobs(image);
        for (Blob blob : blobs)
        {
            System.out.printf("BLOB%n\tSize: %dx%d%n\tCenter: (%f, %f)%n\tColor: (%d, %d, %d)%n", blob.width,
                    blob.height, blob.centerX, blob.centerY, blob.color.getRed(), blob.color.getGreen(),
                    blob.color.getBlue());
        }
    }
}
