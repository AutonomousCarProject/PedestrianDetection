package AIGroup;

import global.Constant;
import group3.MovingBlob;
import group4.BlobFilter;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by Spencer on 8/1/2017.
 */
public class Thresholds {

    public static float getScore(){
        List<MovingBlob> blobs = getFrame();

        List<MovingBlob> retain = new ArrayList();
        List<MovingBlob> miss = new ArrayList();

        for(MovingBlob blob : blobs){
            if(blob.isPedestrian()) retain.add(blob);
            else miss.add(blob);
        }

        int retainCount = retain.size();
        int missCount = miss.size();

        BlobFilter filter = new BlobFilter();
        Collection seen = filter.filterMovingBlobs(blobs);
        retain.retainAll(seen);
        miss.retainAll(seen);

        float accuracy = (float)retain.size() / (float)retainCount;
        float missAcc = 1-((float)miss.size() / (float)missCount);
        float score = accuracy * missAcc;
        return score;
    }


    public static List<MovingBlob> getFrame(){
        List<MovingBlob> blobs = new ArrayList<>();
        try{

            FileInputStream fis = new FileInputStream("t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(ois.available() > 0)    blobs.add((MovingBlob)ois.readObject());

            ois.close();


        }
        catch(Exception exc){
            System.out.println("Exception");
        }
        return blobs;
    }

}
