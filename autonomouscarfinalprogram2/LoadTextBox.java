/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.TextBox;
import global.Constant;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class LoadTextBox extends TextBox
{
    private ArrayList<VariableSlider> vs = new ArrayList<>();
    public LoadTextBox(double x, double y, double width, double height, Background background, String text, Font font, boolean editable, Color textColor, double horizontalMargin, double verticalMargin, double lineSpacing) 
    {
        super(x, y, width, height, background, text, font, editable, textColor, horizontalMargin, verticalMargin, lineSpacing);
    }
    public void addSliders(VariableSlider...sliders)
    {
        for(VariableSlider v : sliders)
        {
            vs.add(v);
        }
    }
    public void addSliders(ArrayList<VariableSlider> sliders)
    {
        for(VariableSlider v : sliders)
        {
            vs.add(v);
        }
    }
        
    protected void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            try
            {
                FileInputStream fis = new FileInputStream(super.getText());
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(isr);
                String s;
                while((s = br.readLine()) != null)
                {
                    for(Field f : Constant.class.getDeclaredFields())
                    {
                        if(s.indexOf(":") != -1 && f.getName().equals( s.substring(0,s.indexOf(":"))))
                        {
                            String theValue = s.substring(s.indexOf(":") + 2);
                            if(f.getType() == float.class)
                            {
                                f.setFloat(null,Float.parseFloat(theValue));
                            }
                            else if(f.getType() == int.class)
                            {
                                
                                f.setInt(null,Integer.parseInt(theValue));
                            }
                            else if(f.getType() == short.class)
                            {
                                f.setShort(null,Short.parseShort(theValue));
                            }
                            if(f.getType() == double.class)
                            {
                                f.setDouble(null,Double.parseDouble(theValue)); 
                            }
                            System.out.println("VALUE:" + theValue);
                            
                        }

                    }
                    
                }
            }
            catch(IllegalAccessException|IOException ex){System.out.println("ex!!!!!!!!!!!!!!!");}
            for(VariableSlider s : vs)
            {
                s.scrollToSupplierValue();
            }
            
        }
        super.keyPressed(e);
        
        
    }
}
