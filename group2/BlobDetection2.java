package group2;

import java.util.LinkedList;
import java.util.List;

import group1.IImage;
import group1.IPixel;

public class BlobDetection2 implements IBlobDetection
{
    private static List<Blob> blobs = new LinkedList<>();

    @Override
    public List<Blob> getBlobs(IImage image)
    {
        IPixel[][] pixels = image.getImage();
        final int rowSize = pixels[0].length;
        final int colSize = pixels.length;
        final int size = rowSize * colSize;

        for (int i = 0; i < size; i++)
        {
            final int row = i / rowSize;
            final int col = i % rowSize;

            IPixel pixel = pixels[row][col];

            if (col < colSize - 1)
            {
                IPixel rightPixel = pixels[row][col + 1];
                if (pixel.getColor() == rightPixel.getColor() && pixel.getSaturation() == rightPixel.getSaturation())
                {
                    Blob b = findBlob(row, col, pixel.getColor(), pixel.getSaturation());
                    if (b == null)
                    {
                        b = new Blob(2, 1, row + 0.5f, col + 0.5f, pixel);
                    }
                    else
                    {

                    }
                }
            }

            if (row < rowSize - 1)
            {

            }
        }

        return null;
    }

    private Blob findBlob(int x, int y, int color, int saturation)
    {
        for (Blob b : blobs)
        {
            if (blobContains(b, x, y) && (b.color.getColor() == color) && (b.color.getSaturation() == saturation))
            {
                return b;
            }
        }

        return null;
    }

    private boolean blobContains(Blob b, int x, int y)
    {
        return (Math.abs(b.centerX - x) < (b.width / 2f)) && (Math.abs(b.centerY - y) < (b.height / 2f));
    }
}
