/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import java.text.DecimalFormat;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.utilities.Supplier;


/**
 *
 * @author peter_000
 */
public class VariableSlider<E extends Number> extends Slider
{
    
    private double max;
    private double min;
    private Setter<E> setter;
    private Supplier<E> getter;
    public VariableSlider(double x, double y, double width, double height, Background background, double min, double max, Setter<E> setter, Supplier<E> getter)
    {
        super(x,y,width,height, background);
        this.setter = setter;
        this.getter = getter;
        this.max = max;
        this.min = min;
        looiStep();
    }
    protected void looiStep()
    {
        super.looiStep();
        setter.set((E)(Double)(super.getPercentage()/100.0 * (max - min) + min));
        
        
    }
        
    protected void looiPaint()
    {
    	super.looiPaint();
    	
    	DecimalFormat df = new DecimalFormat();
    	df.setMaximumFractionDigits(2);
    	drawString(df.format(super.getPercentage()/100.0 * (max - min) + min),getX() + getWidth()/2,getY() -20);
    }
    public void scrollToSupplierValue()
    {
        scrollToValue(getter.get());
    }
    public void scrollToValue(E value)
    {
        
        double doubleVal = (double)(Double)value;
        double percentage = (doubleVal - min)/(max-min) * 100;
        
        //System.out.println("(" + doubleVal +"-"+ min+")/("+max+"-" + min+") * 100" + " -Peter");
        //System.out.println(percentage + " -Peter");
        super.slideToPercentage(percentage); 
        //slideToPercentage(0);
        
    }
    public static interface Setter<E extends Number>
    {
        public void set(E e);
    }
}
