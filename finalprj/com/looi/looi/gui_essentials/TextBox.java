/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.gui_essentials;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class TextBox extends Rectangle
{
    public static final double DEFAULT_CURSOR_WIDTH = 3;
    public static final double DEFAULT_SPAM_DELAY = .2;//in seconds
    
    private boolean editable;
    private String defaultText;
    private String text;
    private Font font;
    private Color textColor;
    private double verticalMargin;
    private double horizontalMargin;
    private double lineSpacing;
    private double spamDelay;
    private double spamTimer = 0;
    private int currentKey;
    
    private ArrayList<String> lines = new ArrayList<>();
    
    private int row;
    private int indexInLine; //cursor is between indexInLine and indexInLine+1
    private boolean selected = false;
    private Cursor cursor;
    private double cursorWidth;
    private boolean lastMouseClickValid = false;
    
    
    public TextBox(double x, double y, double width, double height, Background background, String text, Font font, boolean editable, Color textColor, double horizontalMargin, double verticalMargin, double lineSpacing, double cursorWidth, double spamDelay)
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
        setCursorWidth(cursorWidth);
        setSpamDelay(spamDelay);
        cursor = newCursor();
        cursor.setLayer(getLayer() - 1);
    }
    public TextBox(double x, double y, double width, double height, Background background, String text, Font font, boolean editable, Color textColor, double horizontalMargin, double verticalMargin, double lineSpacing)
    {
        this(x,y,width,height,background,text,font,editable,textColor,horizontalMargin,verticalMargin,lineSpacing,DEFAULT_CURSOR_WIDTH,DEFAULT_SPAM_DELAY);
    }
    public synchronized void setText(String text){this.text = text;}
    public String getText(){return text;}
    public void setDefaultText(String defaultText){this.defaultText = defaultText;}
    public String getDefaultText(){return defaultText;}
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
    public double getCursorWidth(){return cursorWidth;}
    public void setCursorWidth(double d){this.cursorWidth = d;}
    public void setSpamDelay(double d){spamDelay = d;}
    public double getSpamDelay(){return spamDelay;}
    
    protected void savePaintInformation()
    {
        if(getGraphics()!=null)
        {
            
        }
        
    }
    protected void looiPaint()
    {
        super.looiPaint();
        
        

        
        
        synchronized(this)//so that looistep doesn't also edit it at the same time
        {
            Font scaledFont = new Font(getFont().getName(),getFont().getStyle(),scaleW(getFont().getSize()));
            super.setFont(scaledFont);//MUST be SUPER.setFont(...) because setFont(...) is overridden
            double minY = getY() + getVerticalMargin();
            double maxY = getY() + getHeight() - getVerticalMargin();
            double minX = getX() + getHorizontalMargin();
            double maxX = getX() + getWidth() - getHorizontalMargin();
            lines.clear(); 
            lines.add(getText());
            //System.out.println("Start. Size: " + lines.size());
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
        
            if(lines.isEmpty())
            {
                lines.add("");
            }
        
            setColor(getTextColor());

            for(int i = 0; i < lines.size(); i++)
            {

                /*super.setFont(getFont());//MUST be SUPER.setFont(...) because setFont(...) is overridden
                double offsetDueToNotBeingTheFirstLine = i * (scaleH(getFont().getSize()) + getLineSpacing());

                if(!(offsetDueToNotBeingTheFirstLine + getY() + 2*scaleH(getFont().getSize()) + getVerticalMargin() > getY() + getHeight() - getVerticalMargin()))
                {
                    drawString(lines.get(i),getX() + getHorizontalMargin(),offsetDueToNotBeingTheFirstLine + getY() + scaleH(getFont().getSize()) + getVerticalMargin());
                }*/
                super.setFont(getFont());//MUST be SUPER.setFont(...) because setFont(...) is overridden
                double offsetDueToNotBeingTheFirstLine = i * (getFont().getSize() + getLineSpacing());

                if(!(offsetDueToNotBeingTheFirstLine + getY() + getFont().getSize() + getVerticalMargin() > getY() + getHeight() - getVerticalMargin()))
                {
                    drawString(lines.get(i),getX() + getHorizontalMargin(),offsetDueToNotBeingTheFirstLine + getY() + getFont().getSize() + getVerticalMargin());
                }

            }
            //System.out.println("end. Size: " + lines.size() + " ScaledFont size: " + scaledFont.getSize() + " Font size: " + getGraphics().getFont().getSize());
        }
    }
    protected void looiStep()
    {
        spamTimer += 1.0/thisWindow().getUPS();
        
        if(isEditable())
        {
            boolean isPressed = false;
            boolean justPressed = false;
            boolean justReleased = false;
            
            boolean mouseOnButton = (getInternalMouseX() > getX() + getHorizontalMargin() && getInternalMouseX() < getX() + getWidth() - getHorizontalMargin() && getInternalMouseY() > getY() + getVerticalMargin()&& getInternalMouseY() < getY() + getHeight() - getVerticalMargin());
            
            //check last click
            if(mouseLeftPressed())
            {
                if(mouseOnButton)
                {
                    lastMouseClickValid = true;
                }
                else
                {
                    lastMouseClickValid = false;
                }
            }
            if(mouseOnButton)
            {
                if(mouseLeftPressed())
                {
                    justPressed = true;
                }
                if(mouseLeftReleased() && lastMouseClickValid)
                {
                    justReleased = true;
                }
                if(mouseLeftEngaged() && lastMouseClickValid)
                {
                    isPressed = true;
                }
            }
            if(justReleased)
            {
                synchronized(this)
                {
                    whenPressed();
                }
                
            }
        }
    }
    protected String insert(char c, int index, String s)
    {
        String first = s.substring(0,index);
        String last = s.substring(index);
        return first + c + last;
    }
    protected String remove(int index, String s)
    {
        if(index < 0)
            return s;
        if(s.length() == 1 && index == 0)
        {
            return "";
        }
        String first = s.substring(0,index);
        String last = s.substring(index);
        if(last.length() > 1)
            last = last.substring(1);
        return first + last;
    }
    protected void keyPressed(KeyEvent e)
    {
        spamTimer = 0;
        currentKey = e.getKeyCode();
        if(e.getKeyChar() != Character.MAX_VALUE)
        {
            type(e);
        }
    }
    protected void keyEngaged(KeyEvent e)
    {
        if(e.getKeyChar() != Character.MAX_VALUE && e.getKeyCode() == currentKey && spamTimer >= spamDelay)
        {
            type(e);
        }
    }
    protected void type(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            this.setText(remove(calculateIndexInEntireText()-1,getText()));
            indexInLine--;
            if(indexInLine < 0)
            {
                row--;
                if(row == -1)
                {
                    row = 0;
                    indexInLine = 0;
                }
                else
                {
                    indexInLine = lines.get(row).length();
                }

            }
        }
        else
        {
            this.setText(insert(e.getKeyChar(),calculateIndexInEntireText(),getText()));
            indexInLine++;
            double minX = getX() + getHorizontalMargin();
            double maxX = getX() + getWidth() - getHorizontalMargin();
            if(getGraphics().getFontMetrics().stringWidth(lines.get(row)) >= scaleW(maxX - minX))
            {
                indexInLine = 0;
                row++;
            }
        }
    }
    protected int calculateIndexInEntireText()
    {
        int index = 0;
        for(int i = 0; i < row; i++)
        {
            index += lines.get(i).length();
        }
        index += indexInLine;
        return index;
    }
    protected synchronized void whenPressed()
    {
        
        row = 0;
        boolean set = false;
        for(int i = 0; i < lines.size(); i++)
        {
            double offsetDueToNotBeingTheFirstLine = i * (getFont().getSize() + getLineSpacing());
            if(getInternalMouseY() < offsetDueToNotBeingTheFirstLine + getY() + getFont().getSize() + getVerticalMargin())
            {
                row = i;
                set = true;
                break;
            }
        }
        if(! set)
        {
            row = lines.size()-1;
            if(row == -1)
            {
                row = 0;
            }
        }
        String line = lines.get(row);
        boolean set2 = false;
        Graphics g = getGraphics().create();
        Font scaledFont = new Font(getFont().getName(),getFont().getStyle(),scaleW(getFont().getSize()));
        g.setFont(scaledFont);
        for(int i = 0; i <= line.length(); i++)
        {
            //System.out.println("Try: " + getInternalMouseX() +" < "+ (getX() + getHorizontalMargin() + scaleW(g.getFontMetrics().stringWidth(line.substring(0,i)))));
            double centerOfCharacter;
            if(i != 0)
            {
                centerOfCharacter = ( scaleX(getX() + getHorizontalMargin()) + g.getFontMetrics().stringWidth(line.substring(0,i)) + scaleX(getX() + getHorizontalMargin()) + g.getFontMetrics().stringWidth(line.substring(0,i-1)) )/2;
            }
            else
            {
                centerOfCharacter = ( scaleX(getX() + getHorizontalMargin()) + g.getFontMetrics().stringWidth(line.substring(0,i)) + scaleX(getX() + getHorizontalMargin()) + 0 )/2;
            }
            if(scaleX(getInternalMouseX()) < centerOfCharacter)
            {
                indexInLine = i - 1;
                set2 = true;
                break;
            }
        }
        if(! set2)
        {
            indexInLine = line.length();
        }
        select();
        
        
    }
    public void select()
    {
        selected = true;
        cursor.activate();
    }
    public Cursor newCursor()
    {
        return new Cursor();
    }
    public class Cursor extends Rectangle
    {
        public Cursor()
        {
            
        }
        protected void looiPaint()
        {
            drawString(indexInLine,700,700);
            super.looiPaint();
            super.setDimensions(getCursorWidth(),TextBox.this.getFont().getSize()); 
            synchronized(TextBox.this)
            {
                if(TextBox.this.getGraphics() != null)
                {
                    Font scaledFont = new Font(getFont().getName(),getFont().getStyle(),scaleW(getFont().getSize()));
                    Cursor.this.setFont(scaledFont);
                    double offsetDueToNotBeingTheFirstLine = row * (getFont().getSize() + getLineSpacing());
                    try
                    {
                        super.setPosition(TextBox.this.getX() + TextBox.this.getHorizontalMargin() + Cursor.this.getGraphics().getFontMetrics().stringWidth(lines.get(row).substring(0,indexInLine))/getViewOverInternalWidth(),offsetDueToNotBeingTheFirstLine + TextBox.this.getY() + getVerticalMargin());
                    }
                    catch(java.lang.IndexOutOfBoundsException e)
                    {
                        
                    }
                    
                    //System.out.println(indexInLine + " from Cursor");
                }
                
            }
            
        }
        
    }
}

