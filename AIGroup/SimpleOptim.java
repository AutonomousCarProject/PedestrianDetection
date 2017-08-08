package AIGroup;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.TextBox;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.gui_essentials.Window.DecorationBar;
import global.Constant;
import global.DefaultConstant;
import group3.MovingBlob;
import group4.BlobFilter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * Created by Spencer on 8/1/2017.
 */
public class SimpleOptim {
    private List<MovingBlob> blobs;
    private String filename;
    private final static int threshNum = 5;
    private final int threshLoc = 0b00001111100000000000000;  //this is the location in the switch case of the thresholds being used
    private final int threshLocSize = 23;
    private float thresholds[] = new float[threshNum];
    private float gradients[] = new float[threshNum];
    private float increment[] = new float[threshNum];

    private float stepSize = 0.5f;
    private float incrementRatio = 10f;

    float max  = 0;
    float thresholdsMax[] = new float[threshNum];

    public SimpleOptim(String filename) {
        this.filename = filename;

        int count = 0;
        for (int i = 1; i <= threshLocSize; i++) {
            if (((threshLoc >> i) & 1) == 1) {
                float threshold = DefaultConstant.getVariable(i).floatValue();
                thresholds[count] = threshold;
                increment[count] = threshold/incrementRatio;
                count++;
            }
        }
        blobs = getBlobs();
    }
    public float[] getThresholdsMax()
    {
        return thresholdsMax;
    }
    public void setFileName(String name)
    {
        this.filename = name;
    }
    public void setThresholds(){
        
        int count = 0;
        for(int i = 0; i < threshLocSize; i++){
            if(((threshLoc >> i) & 1) == 1){
                
                Constant.setVariable(i,thresholds[count]);
                count++;
            }
        }
    }

    public List<String> loadFilenames(){
        List<String> filenames = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream("files.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String line;
            double y = 10;
            while ((line = br.readLine()) != null){
                filenames.add(line);
            }
        }
        catch(Exception exc){
            System.out.println("Exception when loading filenames");
        }
        return filenames;
    }

    public void runCombined(){
        List<String> filenames = loadFilenames();
        int sum[] = new int[threshNum];
        int length = filenames.size();

        for(int i =0 ; i < length; i++){
            System.out.println(filenames.get(i));
            filename = filenames.get(i);
            runForce();
            for(int j = 0; j < threshNum; j++){
                sum[j] += thresholdsMax[j];
            }
        }
        System.out.println();
        for(int i = 0; i < threshNum; i++){
            thresholds[i] = sum[i]/length;
            setThresholds();
            System.out.print(thresholds[i]+"\t\t\t");
        }
    }


    public void runForce(){
        max = 0;
        blobs = Thresholds.getFrame(filename);
        recurseForce(0);

        System.out.println("Score: " + max);
        for(int j = 0; j < threshNum; j++){
            thresholds[j] = thresholdsMax[j];
        }
        setThresholds();
    }

    private boolean recurseForce(int count){
        for(float i = 0; i < increment[count]*incrementRatio*2; i+= increment[count]){
            thresholds[count] = i;
            float score = getScore()[0];
            

            if(score > max) {
                System.out.println("max: " + max);
                max = score;
                for(int j = 0; j < threshNum; j++){
                    thresholdsMax[j] = thresholds[j];
                }
            }

            if(count < threshNum-1) {
                if(recurseForce(count+1)) return true;
            }

        }
        return false;

    }

    public void runUphill(int iterations){
        
        BlobFilter filter = new BlobFilter();
        for(int i = 0; i < iterations; i++){
            iterate();
        }
    }
    public List<MovingBlob> getBlobs()
    {
        List<MovingBlob> blobs = Thresholds.getFrame(filename);
        return blobs;
    }

    private void iterate(){
        setGradients();
        for(int i = 0; i < threshNum; i++){
            thresholds[i] += (gradients[i] * stepSize);
        }
    }

    private void setGradients(){
        for(int i = 0; i < threshNum; i++){
            float init = thresholds[i];
            float first = getScore()[0];
            thresholds[i] = init + increment[i];
            float second = getScore()[0];
            gradients[i] = (second - first)/(float)increment[i];
            thresholds[i] = init;
            setThresholds();
        }
    }

    public void setFrame(){
        blobs = Thresholds.getFrame(filename);
    }

    public float[] getScore(){

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
//        if(accuracy > 0) System.out.println("NON ZERO accuracy");
        if(retain.size()==0 && retainCount == 0) accuracy = 1;
        float missAcc = 1-((float)miss.size() / (float)missCount);
//        if(missAcc > 0) System.out.println("NON ZERO mssAcc");
        if(miss.size()==0 && missCount == 0) missAcc = 1;
        
        
        
        float score = accuracy * missAcc;
        return new float[] {score,accuracy,missAcc};
    }
}
