/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import java.text.DecimalFormat;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;

/**
 *
 * @author peter_000
 */
public class VariableSlider extends Slider
{
    private double max;
    private double min;
    private Setter<Double> setter;
    public VariableSlider(double x, double y, double width, double height, Background background, double min, double max, Setter<Double> setter)
    {
        super(x,y,width,height, background);
        this.setter = setter;
        this.max = max;
        this.min = min;
    }
    protected void looiStep()
    {
        super.looiStep();
        setter.set(super.getPercentage()/100.0 * (max - min) + min);
    }
            
    protected void looiPaint()
    {
    	super.looiPaint();
    	
    	DecimalFormat df = new DecimalFormat();
    	df.setMaximumFractionDigits(2);
    	drawString(df.format(super.getPercentage()/100.0 * (max - min) + min),getX() + getWidth()/2,getY() -20);
    }
            
    public static interface Setter<E>
    {
        public void set(E e);
    }
}
