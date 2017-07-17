package group5;

import java.util.List;

import group1.IPixel;
import group3.MovingBlob;

public interface IImageDrawing
{
    void draw(IPixel[][] image, List<MovingBlob> blobs);
}
