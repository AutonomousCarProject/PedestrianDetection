package group5;

import group1.IImage;
import group2.IBlobDetection;
import group3.MovingBlob;

import java.util.List;

public interface IImageDrawing
{
    void draw(IImage image, List<? extends Blob> iBlobs);
}
