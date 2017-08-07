package group2;

import java.util.List;

import group1.IImage;
import group1.IPixel;

//Intereface Blob Detection
public interface IBlobDetection
{
    List<Blob> getBlobs(IImage image);
}
