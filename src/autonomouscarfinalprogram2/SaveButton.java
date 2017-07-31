/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.AstheticButton;
import global.Constant;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalTime;

/**
 *
 * @author peter_000
 */
public class SaveButton extends AstheticButton
{

    public SaveButton(double x, double y, double width, double height, String text, Color frontColor) {
        super(x, y, width, height, text, frontColor);
    }

    @Override
    protected void action() 
    {
        try
        {
            
            String name = "data_save_on_" + LocalTime.now().getHour() + "_" + LocalTime.now().getMinute() + "_" + LocalTime.now().getSecond() + "_" + System.currentTimeMillis() + ".txt";
            FileOutputStream fos = new FileOutputStream(name);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
           
            
            Field[] fields = Constant.class.getDeclaredFields();
            for(Field f : fields)
            {
                bw.write(f.getName() + ": " + f.get(null) + "\r");
                bw.newLine();
                System.out.println(f.getName()+":  " + f.get(null));
            }
            bw.write("Honestly, why? smh");
            
            
            bw.close();
        }
        catch(IOException|IllegalAccessException e){}
        Toolkit.getDefaultToolkit().beep();
    }
    
}
