package AIGroup;

import global.Constant;
import group3.MovingBlob;
import group4.BlobFilter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Float.NaN;

/**
 * Created by Spencer on 8/1/2017.
 */
public class Thresholds {

    public static float getScore(String filename){
        List<MovingBlob> blobs = getFrame(filename);

        List<MovingBlob> retain = new ArrayList();
        List<MovingBlob> miss = new ArrayList();

        for(MovingBlob blob : blobs){
            if(blob.isPedestrian()) {
                retain.add(blob);
                System.out.println("PEDESTRIANS!!    " + retain.size());
            }
            else miss.add(blob);
        }

        int retainCount = retain.size();
        int missCount = miss.size();

        BlobFilter filter = new BlobFilter();
        Collection seen = filter.filterMovingBlobs(blobs);
        retain.retainAll(seen);
        miss.retainAll(seen);
        System.out.println("size: "+seen.size());

        float accuracy = (float)retain.size() / (float)retainCount;
        if(retain.size()==0 && retainCount == 0) accuracy = 1;
        float missAcc = 1-((float)miss.size() / (float)missCount);
        if(miss.size()==0 && missCount == 0) missAcc = 1;
        float score = accuracy * missAcc;
        return score;
    }


    public static List<MovingBlob> getFrame(String filename){
        List<MovingBlob> blobs = new LinkedList<>();
        try{
            FileInputStream fis = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fis);

            blobs = (LinkedList<MovingBlob>)ois.readObject();
            
            ois.close();
        }
        catch(IOException exc){
            //exc.printStackTrace();
            //System.out.println("IOException");
        }
        catch(ClassNotFoundException exc){
            //exc.printStackTrace();
            //System.out.println("Class not found");
        }
        catch(Exception exc){
            //exc.printStackTrace();
        }
        return blobs;
    }

}
