package group2;

import java.util.ArrayList;
import java.util.List;

import group1.IImage;
import group1.IPixel;

//creates a class for detecting blobs 

public class BlobDetection implements IBlobDetection
{
    private static final int SATURATION_THRESHOLD = 15;

    private boolean isWithinThreshold(int val1, int val2, int thresh)
    {
        return Math.abs(val1 - val2) < thresh;
    }

    @Override
    public List<Blob> getBlobs(IImage image)
    {
        IPixel[][] pixels = image.getImage();
        final int rowSize = pixels[0].length;
        final int colSize = pixels.length;
        final int size = rowSize * colSize;

        List<Integer> visited = new ArrayList<>();
        List<Blob> blobs = new ArrayList<>();

        for (int i = 0; i < size; i++)
        {
            if (visited.contains(i))
            {
                continue;
            }

            List<Integer> toVisit = new ArrayList<>();
            toVisit.add(i);

            final int iRow = i % rowSize;
            final int iCol = i / rowSize;
            final int iColor = pixels[iRow][iCol].getColor();
            final int iSaturation = pixels[iRow][iCol].getSaturation();
            // FIXME possibly update with averages

            int top = Integer.MAX_VALUE, bottom = 0, left = Integer.MAX_VALUE, right = 0;

            while (!toVisit.isEmpty())
            {
                int n = toVisit.remove(0);
                visited.add(n);

                final int row = n % rowSize;
                final int col = n / rowSize;

                if (iColor == pixels[row][col].getColor()
                        && isWithinThreshold(iSaturation, pixels[row][col].getSaturation(), SATURATION_THRESHOLD))
                {
                    if (row > 0 && !visited.contains(n - rowSize))
                    {
                        // we can go up
                        toVisit.add(n - rowSize);
                    }
                    if (row < colSize - 1 && !visited.contains(n + rowSize))
                    {
                        // we can go down
                        toVisit.add(n + rowSize);
                    }
                    if (col > 0 && !visited.contains(n - 1))
                    {
                        // we can go left
                        toVisit.add(n - 1);
                    }
                    if (col < rowSize - 1 && !visited.contains(n + 1))
                    {
                        // we can go right
                        toVisit.add(n + 1);
                    }

                    top = Math.min(row, top);
                    bottom = Math.max(row, bottom);
                    left = Math.min(col, left);
                    right = Math.max(row, right);
                }
            }

            int width = right - left;
            int height = bottom - top;

            if (width > 4 && height > 4)
            {
                blobs.add(new Blob(width, height, left + (width / 2), top + (height / 2), pixels[iRow][iCol]));
            }
        }

        return blobs;
    }
}