package AIGroup;

import global.Constant;
import group3.MovingBlob;
import group4.BlobFilter;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static global.Constant.setVariable;

/**
 * Created by Spencer on 8/1/2017.
 */
public class SimpleOptim {
    private List<MovingBlob> blobs;
    private String filename;
    private final static int threshNum = 2;
    private int thresholds[] = new int[threshNum];
    private float gradients[] = new float[threshNum];
    private int init[] = new int[threshNum];

    public SimpleOptim(String filename){
        this.filename = filename;
        for(int i = 0; i < threshNum; i++){
            thresholds[i] = init[i] = (int)Constant.getVariable(i);
        }
    }

    private void getFrame(){
        try{
            FileInputStream fis = new FileInputStream("t.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);

            while(ois.available() > 0)    blobs.add((MovingBlob)ois.readObject());

            ois.close();
        }
        catch(Exception exc){
            System.out.println("Exception");
        }
    }

    private void setGradients(int[] increment){
        for(int i = 0; i < threshNum; i++){
            thresholds[i] = init[i];
            float first = getScore();
            thresholds[i] = init[i] + increment[i];
            float second = getScore();
            gradients[i] = second - first;
            thresholds[i] = init[i];
            setThresholds();
        }
    }

    private void setThresholds(){
        for(int i = 0; i < threshNum; i++) Constant.setVariable(i, thresholds[i]);
    }

    private float getScore(){
        setThresholds();
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
}
