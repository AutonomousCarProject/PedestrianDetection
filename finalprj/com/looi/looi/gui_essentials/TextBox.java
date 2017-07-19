/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import com.looi.looi.utilities.Action;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class TextBox extends Rectangle
{
    private boolean editable;
    private String defaultText;
    private String text;
    private Font font;
    private Color textColor;
    private double verticalMargin;
    private double horizontalMargin;
    private double lineSpacing;
    
    
    public TextBox(double x, double y, double width, double height, Background background, String text, Font font, boolean editable, Color textColor, double horizontalMargin, double verticalMargin, double lineSpacing)
    {
        super(x,y,width,height,background);
        setDefaultText(text);
        setText(text);
        setEditable(editable);
        setTextColor(textColor);
        setFont(font);
        setHorizontalMargin(horizontalMargin);
        setVerticalMargin(verticalMargin);
        setLineSpacing(lineSpacing);
    }
    public void setText(String text){this.text = text;}
    public String getText(){return text;}
    public void setDefaultText(String defaultText){this.defaultText = defaultText;}
    public String getDefaultText(){return defaultText;}{this.editable = editable;}
    public void setFont(Font f){font = f;}
    public Font getFont(){return font;}
    public Color getTextColor(){return textColor;}
    public void setTextColor(Color textColor){this.textColor = textColor;}
    public boolean isEditable(){return editable;}
    public void setEditable(boolean editable){this.editable = editable;}
    
    public double getVerticalMargin(){return verticalMargin;}
    public double getHorizontalMargin(){return horizontalMargin;}
    public void setVerticalMargin(double d){verticalMargin = d;}
    public void setHorizontalMargin(double d){horizontalMargin = d;}
    public void setLineSpacing(double d){lineSpacing = d;}
    public double getLineSpacing(){return lineSpacing;}
    
    protected void looiPaint()
    {
        super.looiPaint();
        
        Font scaledFont = new Font(getFont().getName(),getFont().getStyle(),scaleW(getFont().getSize()));
        
        
        setColor(getTextColor());
        super.setFont(scaledFont);//MUST be SUPER.setFont(...) because setFont(...) is overridden
        double minY = getY() + getVerticalMargin();
        double maxY = getY() + getHeight() - getVerticalMargin();
        double minX = getX() + getHorizontalMargin();
        double maxX = getX() + getWidth() - getHorizontalMargin();
        
        ArrayList<String> lines = new ArrayList<>();
        lines.add(getText());
        while(getGraphics().getFontMetrics().stringWidth(lines.get(lines.size() - 1)) >= scaleW(maxX - minX))
        {
            
            String currentLast = lines.remove(lines.size() - 1);//LAST ONE REMOVED AT THIS LINE!!!!!!
            String newLast = "";
            while(getGraphics().getFontMetrics().stringWidth(currentLast) >= scaleW(maxX - minX))
            {
                newLast = currentLast.substring(currentLast.length() - 1) + newLast;
                currentLast = currentLast.substring(0,currentLast.length() - 1);
            }
            lines.add(currentLast);
            lines.add(newLast);
        }
        for(int i = 0; i < lines.size(); i++)
        {
            super.setFont(getFont());//MUST be SUPER.setFont(...) because setFont(...) is overridden
            double offsetDueToNotBeingTheFirstLine = i * (scaleH(getFont().getSize()) + getLineSpacing());
            drawString(lines.get(i),getX() + getHorizontalMargin(),offsetDueToNotBeingTheFirstLine + getY() + scaleH(getFont().getSize()) + getVerticalMargin());
        }
        
    }
}
class Cheat
{
    public static <E> E combine(Action a, E e)
    {
        a.act();
        return e;
    }
}
