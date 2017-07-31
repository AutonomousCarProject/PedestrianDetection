package group2;

import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import group1.IImage;
import group1.IPixel;

public class BlobDetection implements IBlobDetection
{
    public static final int MAXIMUM_DIFFERENCE_IN_WIDTH_BETWEEN_TWO_BLOBS_IN_ORDER_TO_JOIN = 75;

    private Deque<Blob> unusedBlobs = new ArrayDeque<>();
    private Deque<BlobInProgress> unusedBips = new ArrayDeque<>();
    private BlobInProgress[][] bips = null;
    private Set<Integer> added = new HashSet<>();
    private List<Blob> blobs = new LinkedList<>();

    @Override
    public List<Blob> getBlobs(IImage image)
    {
        unusedBlobs.addAll(blobs);
        blobs.clear();
        
        IPixel[][] pixels = image.getImage();

        if (bips == null || pixels.length != bips.length || pixels[0].length != bips[0].length)
        {
            bips = new BlobInProgress[pixels.length][pixels[0].length];
        }
        else
        {
            for(int i = 0; i < bips.length; i++)
            {
                for(int j = 0; j < bips.length; j++)
                {
                    BlobInProgress bip = bips[i][j];
                    if(bip != null)
                    {
                        bips[i][j] = null;
                        if (!added.contains(bip.id))
                        {
                            added.add(bip.id);
                            unusedBips.push(bip);
                        }
                    }
                }
            }
            
            added.clear();
        }

        for (int row = 0; row < pixels.length; row++)
        {
            for (int col = 0; col < pixels[0].length - 1; col++)
            {
                IPixel pix1 = pixels[row][col];
                IPixel pix2 = pixels[row][col + 1];

                if (pix1.getColor() == pix2.getColor())// matching
                {

                    if (bips[row][col] != null)
                    {
                        bips[row][col].right = max(bips[row][col].right, col + 1);
                    }
                    else
                    {
                        bips[row][col] = getBip(row, col, row, col + 1, pixels[row][col]);
                    }

                    bips[row][col + 1] = bips[row][col];
                }
            }
        }
        for (int row = 0; row < pixels.length - 1; row++)
        {
            for (int col = 0; col < pixels[0].length; col++)
            {
                IPixel pix1 = pixels[row][col];
                IPixel pix2 = pixels[row + 1][col];

                if (pix1.getColor() == pix2.getColor() && bips[row + 1][col] != null && bips[row][col] != null
                        && Math.abs(bips[row + 1][col].width() - bips[row][col]
                                .width()) <= MAXIMUM_DIFFERENCE_IN_WIDTH_BETWEEN_TWO_BLOBS_IN_ORDER_TO_JOIN)
                {
                    if (bips[row][col] != null)// top pixel has a blob in
                                               // progress
                    {
                        bips[row][col].bottom = max(bips[row][col].bottom, row + 1);

                        if (bips[row + 1][col] != null && bips[row][col] != bips[row + 1][col])// they
                                                                                               // are
                                                                                               // both
                                                                                               // something
                        {
                            BlobInProgress old = bips[row + 1][col];

                            for (int r = old.top; r <= old.bottom; r++)
                            {
                                for (int c = old.left; c <= old.right; c++)
                                {
                                    if (bips[r][c] == old)
                                    {
                                        bips[r][c] = bips[row][col];
                                    }
                                }
                            }

                            bips[row][col].left = min(bips[row][col].left, old.left);
                            bips[row][col].right = max(bips[row][col].right, old.right);
                            bips[row][col].top = min(bips[row][col].top, old.top);
                            bips[row][col].bottom = max(bips[row][col].bottom, old.bottom);
                        }

                        bips[row + 1][col] = bips[row][col];
                    }
                    else if (bips[row + 1][col] != null)
                    {
                        bips[row + 1][col].top = min(bips[row + 1][col].top, row);
                        bips[row][col] = bips[row + 1][col];
                    }
                    else
                    {
                        bips[row][col] = getBip(row, col, row + 1, col, pixels[row][col]);
                        bips[row + 1][col] = bips[row][col];
                    }

                }
            }
        }

        for (BlobInProgress[] bipRow : bips)
        {
            for (BlobInProgress bip : bipRow)
            {
                if (bip != null)
                {
                    if (!added.contains(bip.id))
                    {
                        added.add(bip.id);
                        if (bip.width() >= 4 && bip.height() >= 4 && bip.width() < (pixels[0].length >> 2)
                                && bip.height() < (pixels.length >> 2) && bip.color.getColor() != 3)
                        {
                            blobs.add(getBlob(bip));
                        }
                    }
                }
            }
        }

        return blobs;
    }
    
    private BlobInProgress getBip(int top, int left, int bottom, int right, IPixel color)
    {
        if(unusedBips.isEmpty())
        {
            return new BlobInProgress(top, left, bottom, right, color);
        }
        else
        {
            BlobInProgress bip = unusedBips.pop();
            bip.top = top;
            bip.left = left;
            bip.bottom = bottom;
            bip.right = right;
            bip.color = color;
            return bip;
        }
    }

    private Blob getBlob(BlobInProgress bip)
    {
        if(unusedBlobs.isEmpty())
        {
            return new Blob(bip.width(), bip.height(), bip.left, bip.top, bip.color);
        }
        else
        {
            Blob blob = unusedBlobs.pop();
            blob.width = bip.width();
            blob.height = bip.height();
            blob.x = bip.left;
            blob.y = bip.right;
            blob.color = bip.color;
            return blob;
        }
        
    }
    
    private static class BlobInProgress
    {
        private int top, left, bottom, right, id;
        private IPixel color;
        private static int currentId = 0;
        
        public BlobInProgress(int top, int left, int bottom, int right, IPixel color)
        {
            this.top = top;
            this.left = left;
            this.bottom = bottom;
            this.right = right;
            this.id = currentId++;
            this.color = color;
        }

        public int width()
        {
            return right - left + 1;
        }

        public int height()
        {
            return bottom - top + 1;
        }
    }
}
