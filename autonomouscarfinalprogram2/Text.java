/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Rectangle;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author peter_000
 */
public class Text extends Rectangle
{
    private String text;
    public static final Font ONLY_AVAILIBLE_FONT = new Font("",Font.PLAIN,16);
    public Text(double x, double y, double width, double height, Background background, String text)
    {
        super(x,y,width,height,background);
        this.text = text;
    }
    protected void looiPaint()
    {
        super.looiPaint();
        setColor(Color.BLACK);
        
        setFont(ONLY_AVAILIBLE_FONT);
        drawString(text,getX(),getY()+20);
    }
}
