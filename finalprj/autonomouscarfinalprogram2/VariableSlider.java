/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Slider;
import com.looi.looi.gui_essentials.Window;

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
            
            
            
    public static interface Setter<E>
    {
        public void set(E e);
    }
}
