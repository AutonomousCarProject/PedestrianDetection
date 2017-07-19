package group2;

import java.util.LinkedList;
import java.util.List;

import group1.IImage;
import group1.IPixel;

public class BlobDetection implements IBlobDetection
{
    private List<Blob> blobs = new LinkedList<>();

    @Override
    public List<Blob> getBlobs(IImage image)
    {
        IPixel[][] pixels = image.getImage();
        final int height = pixels.length;
        final int width = pixels[0].length;
        final int size = height * width;

        for (int i = 0; i < size; i++)
        {
            final int row = i / width;
            final int col = i % width;

            IPixel pixel = pixels[row][col];

            if (col < width - 1)
            {
                IPixel rightPixel = pixels[row][col + 1];
                if (pixel.getColor() == rightPixel.getColor() && pixel.getSaturation() == rightPixel.getSaturation())
                {
                    Blob b = findBlob(col, row, pixel.getColor(), pixel.getSaturation());
                    if (b == null)
                    {
                        b = findBlob(col + 1, row, pixel.getColor(), pixel.getSaturation());
                        if (b == null)
                        {
                            b = new Blob(2, 1, col, row, pixel);
                            blobs.add(b);
                        }
                        else
                        {
                            if (!blobContains(b, col, row))
                            {
                                b.width = b.width + 1;
                                b.centerX = col;
                            }
                        }
                    }
                    else
                    {
                        if (!blobContains(b, col + 1, row))
                        {
                            b.width = b.width + 1;
                        }
                    }
                }
            }

            if (row < height - 1)
            {
                IPixel downPixel = pixels[row + 1][col];
                if (pixel.getColor() == downPixel.getColor() && pixel.getSaturation() == downPixel.getSaturation())
                {
                    Blob b = findBlob(col, row, pixel.getColor(), pixel.getSaturation());
                    if (b == null)
                    {
                        b = findBlob(col, row + 1, pixel.getColor(), pixel.getSaturation());
                        if (b == null)
                        {
                            b = new Blob(1, 2, col, row, pixel);
                            blobs.add(b);
                        }
                        else
                        {
                            if (!blobContains(b, col, row))
                            {
                                b.height = b.height + 1;
                                b.centerY = row;
                            }
                        }
                    }
                    else
                    {
                        if (!blobContains(b, col, row + 1))
                        {
                            b.height = b.height + 1;
                        }
                    }
                }
            }
        }

        List<Blob> toRemove = new LinkedList<>();
        for (Blob b : blobs)
        {
            if (b.width < 4 || b.height < 4)
            {
                toRemove.add(b);
            }

            b.centerX = b.centerX + (b.width / 2f);
            b.centerY = b.centerY + (b.height / 2f);
        }

        blobs.removeAll(toRemove);

        return blobs;
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
        final float rx = x - b.centerX, ry = y - b.centerY;
        return (rx >= 0 && rx < b.width) && (ry >= 0 && ry < b.height);
    }
}
