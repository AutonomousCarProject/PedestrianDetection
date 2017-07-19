package group2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import group1.IImage;
import group1.IPixel;

public class BlobDetection2 implements IBlobDetection
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

            boolean blobRowSet = false;
            boolean createdNewBlob = false;

            if (row > 0)
            {
                // Up Look
                IPixel up = pixels[row - 1][col];
                if (pixel.getColor() == up.getColor())
                {
                    System.out.println(pixel.getColor());
                    Blob blob = blobRow[col];
                    if (blob == null)
                    {
                        createdNewBlob = true;
                        blobRow[col] = getBlob(1, 2, col, row - 1, pixel);
                    }
                    else if (!blobContains(blob, col, row))
                    {
                        blob.height = blob.height + 1;
                    }

                    blobRowSet = true;
                }
            }

            if (col > 0)
            {
                // Left look
                IPixel left = pixels[row][col - 1];
                if (pixel.getColor() == left.getColor())
                {
                    Blob upBlob = blobRowSet ? blobRow[col] : null;

                    Blob blob = blobRow[col - 1];
                    if (blob == null)
                    {
                        createdNewBlob = true;
                        blobRow[col] = getBlob(2, 1, col - 1, row, pixel);
                        blobRow[col - 1] = blobRow[col];
                    }
                    else if (!blobContains(blob, col, row))
                    {
                        blob.width = blob.width + 1;
                        blobRow[col] = blobRow[col - 1];
                    }

                    if (upBlob != null && upBlob != blobRow[col])
                    {
                        combine(upBlob, blobRow[col]);

                        unusedBlobs.add(blobRow[col]);
                        blobs.remove(blobRow[col]);

                        for (int j = 0; j <= col; j++)
                        {
                            if (blobRow[j] == blobRow[col])
                            {
                                blobRow[j] = upBlob;
                            }
                        }

                    }

                    blobRowSet = true;
                }
            }

            if (!blobRowSet)
            {
                blobRow[col] = null;
            }

            if (createdNewBlob)
            {
                blobs.add(blobRow[col]);
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

    private boolean blobContains(Blob b, int x, int y)
    {
        final float rx = x - b.centerX, ry = y - b.centerY;
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

    private Blob combine(Blob b1, Blob b2)
    {
        final int up = (int) Math.min(b1.centerY, b2.centerY), left = (int) Math.min(b1.centerX, b2.centerX);
        final int down = (int) Math.max(b1.centerY + b1.height, b2.centerY + b2.height),
                right = (int) Math.max(b1.centerX + b1.width, b2.centerX + b2.width);

        b1.set(right - left + 1, down - up + 1, up, left, b1.color);

        return b1;
    }
}
