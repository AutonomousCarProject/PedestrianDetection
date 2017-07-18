import group1.IImage;
import group2.IBlobDetection;

public interface IImageDrawing
{
    void draw(IImage image, List<MovingBlob> iBlobs);
}