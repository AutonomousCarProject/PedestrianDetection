package group2;

import java.util.List;

import group1.IImage;

public interface IBlobDetection
{
    List<Blob> getBlobs(IImage image);
}
