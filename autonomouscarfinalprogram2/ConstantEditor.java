package autonomouscarfinalprogram2;

import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.TextBox;
import global.Constant;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

public class ConstantEditor extends TextBox {
    public int constantIndex;

    public ConstantEditor(double x, double y, double width, double height, Background background, String text, Font font, Color textColor, double horizontalMargin, double verticalMargin, double lineSpacing, int constantIndex) {
        super(x,y,width,height,background,text,font,true,textColor,horizontalMargin,verticalMargin,lineSpacing);
        this.constantIndex = constantIndex;
    }

    protected void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            String s = super.getText();

            try
            {
                if (s != null) {
                    Constant.setVariable(constantIndex, Double.parseDouble(s));
                }
            }
            catch (NumberFormatException ex)
            {
                System.out.println("NumberFormatException: Something was wrong with the argument put into the constant editor.");
            }
        }
        super.keyPressed(e);
    }
}
