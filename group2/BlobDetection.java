package group2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import group1.IImage;
import group1.IPixel;

public class BlobDetection implements IBlobDetection
{
    private static Deque<Blob> unusedBlobs = new ArrayDeque<>();
    private static List<Blob> blobs = new LinkedList<>();

    private static Blob[] blobRow = null;

    @Override
    public List<Blob> getBlobs(IImage image)
    {
        unusedBlobs.addAll(blobs);
        blobs.clear();

        IPixel[][] pixels = image.getImage();
        final int height = pixels.length;
        final int width = pixels[0].length;
        final int size = height * width;

        if (blobRow == null)
        {
            blobRow = new Blob[width];
        }

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
                            b = getBlob(2, 1, col, row, pixel);
                            blobs.add(b);
                        }
                        else
                        {
                            if (!blobContains(b, col, row))
                            {
                                b.width = b.width + 1;
                                b.x = col;
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
                            b = getBlob(1, 2, col, row, pixel);
                            blobs.add(b);
                        }
                        else
                        {
                            if (!blobContains(b, col, row))
                            {
                                b.height = b.height + 1;
                                b.y = row;
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

            // b.x = b.x + (b.width / 2f);
            // b.y = b.y + (b.height / 2f);
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
        final float rx = x - b.x, ry = y - b.y;
        return (rx >= 0 && rx < b.width) && (ry >= 0 && ry < b.height);
    }

    private Blob getBlob(int width, int height, int centerX, int centerY, IPixel color)
    {
        if (unusedBlobs.isEmpty())
        {
            return new Blob(width, height, centerX, centerY, color);
        }
        else
        {
            Blob b = unusedBlobs.pop();
            b.set(width, height, centerX, centerY, color);
            return b;
        }
    }
}
