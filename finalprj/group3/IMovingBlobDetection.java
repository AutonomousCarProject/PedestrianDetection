package group3;

import java.util.List;

import group2.Blob;

public interface IMovingBlobDetection
{
    List<MovingBlob> getMovingBlobs(List<Blob> blobs);
}
