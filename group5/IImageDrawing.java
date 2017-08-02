package group5;

import group1.IImage;
import group2.Blob;

import java.util.List;

public interface IImageDrawing
{
    void blobsToRectangles(IImage image, List<? extends Blob> iBlobs);
    void draw(IImage image);
}
