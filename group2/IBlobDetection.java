package group2;

import java.util.List;

import group1.IPixel;

public interface IBlobDetection
{
    List<Blob> getBlobs(IPixel[][] image);
}
