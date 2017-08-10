package AIGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spencer on 8/2/2017.
 */
public class testAI {

    public static void main(String[] args) {
        SimpleOptim optim = new SimpleOptim("blob_save_0.8923267038713327");
        long before = System.nanoTime();
        optim.runCombined();
        long after = System.nanoTime();
        System.out.println("Time taken: "+ ((double)(after - before)/1e9d));
//        List<String> fileList = new ArrayList<>();
//        fileList.add("blob_save_0.5032035524632258");
//        fileList.add("blob_save_0.8923267038713327")
//        optim.runCombined();
//        List list = optim.loadFilenames();
//        for(int i =0; i < list.size(); i++){
//            System.out.println(list.get(i));
//        }
    }
}
