/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public abstract class LooiObject implements Comparable<LooiObject>
{
    private double layer = 0;
    private transient Graphics g;
    private transient LooiWindow thisWindow;
    private boolean active;
    private boolean drawScaled = true;
    private ArrayList<LooiObject> components = new ArrayList<>();
    
    /**
    * Creates a new LooiObject and activates it to the main window. Once
    * thisWindow is chosen, it cannot be changed
    */
    protected LooiObject()
        {
            thisWindow = LooiWindow.getMainWindow();
            if(thisWindow != null)
                {
                    thisWindow.activate(this);
                }
        }
    /**
     * Creates a new LooiObject and activates it to a specified LooiWindow 
     * @param looiWindow The LooiWindow which to activate this LooiObject 
     * to. If this is passed in as null, no LooiWindow will activate this
     * LooiObject.
     */
    protected LooiObject(LooiWindow looiWindow, boolean activate)
        {
            thisWindow = looiWindow;
            if(activate)
                {
                    looiWindow.activate(this);
                }
        }
    /**
    * The job of this definition of activate() is to add this 
    * LooiObject and its components to the paint/step ArrayList. It will 
    * NOT call the "uponActivation()" method if already in an 
    * activated state. If states do change, "uponActivation()" 
    * will certainly be called. Programmer may override this method to
    * define what it means to be "activated."
    */
    public void activate(Object...activationInfo)
    {
        activateSelf(activationInfo);
        activateContainedObjs(activationInfo);
    }
    /**
    * Activates all components directly below
    */
    protected void activateContainedObjs(Object...activationInfo)
    {
        for(LooiObject l : components)
        {
            l.activate(activationInfo);
        }
    }
    /**
     * Adds this LooiObject to the paint/step ArrayList
     */
    protected void activateSelf(Object...activationInfo)
    {
        if(thisWindow == null)
        {
            new Exception("thisWindow is null. Cannot activate.").printStackTrace();
        }
        thisWindow.activate(this);
    }
    /**
    * The job of this definition of deactivate() is to remove this 
    * LooiObject and its components from the paint/step ArrayList. It will 
    * NOT call the "uponDectivation()" method if already in a 
    * deactivated state. If states do change, "uponDectivation()" 
    * will certainly be called. Programmer may override this method to
    * define what it means to be "deactivated."
    */
    public void deactivate(Object...activationInfo)
    {
        deactivateContainedObjs(activationInfo);
        deactivateSelf(activationInfo);
    }
    /**
     * Deactivates all components
     */
    protected void deactivateContainedObjs(Object...activationInfo)
    {
        for(int i = 0; i < components.size(); i++)
            {
                components.get(i).deactivate(activationInfo);
            }
    }
    /**
     * Removes this LooiObject from the paint/step ArrayList
     */
    protected void deactivateSelf(Object...activationInfo)
    {
        if(thisWindow == null)
            {
                throw new RuntimeException("This LooiObject's thiWindow is null. Cannot deactivate.");
            }
        thisWindow.deactivate(this);
    }
    
    
    
    
    
    
    protected void looiStep(){}
    protected void looiPaint(){}
    protected void savePaintInformation(){}
    protected void uponActivation(Object...activationInfo){}
    protected void uponDeactivation(Object...activationInfo){}
    
    protected void keyPressed(KeyEvent k){}
    protected void keyEngaged(KeyEvent k){}
    protected void keyReleased(KeyEvent k){}
    protected void mousePressed(MouseEvent k){}
    protected void mouseEngaged(MouseEvent k){}
    protected void mouseReleased(MouseEvent k){}
    
    
    /**
     * Sorts LooiObjects. The ones with the highest number value layer are first
     * in the list, because they need to be painted first. The lowest number 
     * value layered LooiObjects will be at the end of the list.
     * @param other
     */
    @Override
    public int compareTo(LooiObject other) 
    {
        
        if(layer > other.layer)
        {
            return 1;
        }
        if(layer < other.layer)
        {
            return -1;
        }
        return 0;
    }
    /**
     * Returns the layer of this LooiObject
     * @return 
     */
    public double getLayer()
    {
        return layer;
    }
    /**
     * Sets the layer of this LooiObject. Negative is more in-front.
     * @param layer 
     */
    public void setLayer(double layer)
    {
        this.layer = layer;
    }
    /**
     * Checks whether this LooiObject is present in this window's active looi
     * objects arraylist
     * @return 
     */
    public boolean isActive(){return active;}
    /**
     * Gets this LooiObject's LooiWindow
     * @return 
     */
    public LooiWindow thisWindow(){return thisWindow;}
    /**
     * Adds LooiObjects to this LooiObject's components
     * @param looiObject The LooiObject to add
     */
    protected void add(LooiObject looiObject)
    {
        components.add(looiObject);
        if(isActive())
        {
            looiObject.activate(this,"You were added to an active LooiObject");
        }
        else
        {
            looiObject.deactivate(this,"You were added to an inactive LooiObject");
        }
    }
    /**
     * Removes LooiObjects from this LooiObject's components
     * @param looiObject The LooiObject to remove
     */
    protected void remove(LooiObject looiObject)
    {
        components.remove(looiObject);
    }
    /**
    * Removes all components
    */
    protected void removeAllComponents()
    {
        components.clear();
    }
    /**
    * Attempts to remove all pointers within the Looi Engine that point 
    * to this LooiObject. If you still have a pointer that points to this 
    * object kept somewhere else, this LooiObject will yet not be
    * be ready for garbage collection.
    */
    public void delete()
    {
        deactivate();
        for(LooiObject l : components)
            {
                l.delete();
            }
    }
    /**
    * Returns the number of components
    * @return The number of components
    */
    public int getNumComponents()
    {
        return components.size();
    }
    /**
     * Returns a contained LooiObject 
     * @param index The index of the component to return
     * @return A component
     */
    protected LooiObject getComponent(int index)
    {
        return components.get(index);
    }
    /**
    * Determines whether this LooiObject has the specified component (LooiObject)
    * @param looiObject The LooiObject that is being tested for being componentship
    * @return Whether this LooiObject has the other LooiObject as a component
    */
    public boolean hasComponent(LooiObject looiObject)
    {
        return components.contains(looiObject);
    }
    /**
    * Sets the layer of this LooiObject so that this LooiObject is painted
    * at the front of the screen
    */
    public void goToFront()
    {
        setLayer(thisWindow().getTopLayer()-.001);
    }
    /**
     * Sets the layer of this LooiObject so that this LooiObject is painted
     * at the back of the screen
     */
    public void goToBack()
    {
            setLayer(thisWindow().getBottomLayer()+.001);
    }
    /**
    * This method defines the customized serialization for LooiObjects.
    * @param ois the OIS that is passed in
    */
    private void readObject(ObjectInputStream ois)
    {
        try
            {
                ois.defaultReadObject();
            }
        catch(Exception e){}

        thisWindow = LooiWindow.getMainWindow();
    }
    /**
    * Returns whether this window detects that the left mouse button has just been pressed
    * @return Whether this window detects that the left mouse button has just been pressed
    */
    protected boolean mouseLeftPressed()
    {
        return thisWindow.mouseLeftPressed();
    }
    /**
     * Returns whether this window detects that the middle mouse button has just been pressed
     * @return Whether this window detects that the middle mouse button has just been pressed
     */
    protected boolean mouseMiddlePressed()
    {
        return thisWindow.mouseMiddlePressed();
    }
    /**
     * Returns whether this window detects that the right mouse button has just been pressed
     * @return Whether this window detects that the right mouse button has just been pressed
     */
    protected boolean mouseRightPressed()
    {
        return thisWindow.mouseRightPressed();
    }
    /**
     * Returns whether this window detects that the left mouse button has just been released
     * @return Whether this window detects that the left mouse button has just been released
     */
    protected boolean mouseLeftReleased()
    {
        return thisWindow.mouseLeftReleased();
    }
    /**
     * Returns whether this window detects that the middle mouse button has just been released
     * @return Whether this window detects that the middle mouse button has just been released
     */
    protected boolean mouseMiddleReleased()
    {
        return thisWindow.mouseMiddleReleased();
    }
    /**
     * Returns whether this window detects that the right mouse button has just been released
     * @return Whether this window detects that the right mouse button has just been released
     */
    protected boolean mouseRightReleased()
    {
        return thisWindow.mouseRightReleased();
    }
    /**
     * Returns whether this window close button (red X in upper corner of window) has been pressed yet 
     * @return Whether this window close button has been pressed yet 
     */
    protected boolean windowCloseButton()
    {
        return thisWindow.windowCloseButton();
    }
    /**
     * Returns whether this window detects that a key is engaged
     * @param key The key in the form of an integer (use KeyEvent.VK_<Key Name>)
     * @return Whether this window detects that a key is engaged
     */
    protected boolean keyEngaged(int key)
    {
        return thisWindow.keyEngaged(key);//key down
    }
    /**
     * Returns whether this window detects that a key has just been pressed
     * @param key The key in the form of an integer (use KeyEvent.VK_<Key Name>)
     * @return Whether this window detects that a key has just been pressed
     */
    protected boolean keyPressed(int key)
    {
        return thisWindow.keyPressed(key);
    }
    /**
     * Returns whether this window detects that a key has just been released
     * @param key The key in the form of an integer (use KeyEvent.VK_<Key Name>)
     * @return Whether this window detects that a key has just been pressed
     */
    protected boolean keyReleased(int key)
    {
        return thisWindow.keyReleased(key);
    }
    /**
     * Returns whether this window detects that the left mouse button is engaged
     * @return Whether this window detects that the left mouse button is engaged
     */
    protected boolean mouseLeftEngaged()
    {
        return thisWindow.mouseLeftEngaged();
    }
    /**
     * Returns whether this window detects that the middle mouse button is engaged
     * @return Whether this window detects that the middle mouse button is engaged
     */
    protected boolean mouseMiddleEngaged()
    {
        return thisWindow.mouseMiddleEngaged();
    }
    /**
     * Returns whether this window detects that the right mouse button is engaged
     * @return Whether this window detects that the right mouse button is engaged
     */
    protected boolean mouseRightEngaged()
    {
        return thisWindow.mouseRightEngaged();
    }
    /**
     * Returns this window detects a mouse wheel rotation value. The mouse wheel rotation value represents in which direction the mouse wheel is currently turning.
     * @return The mouse wheel rotation value
     */
    protected double getMouseWheelRotation()//Warning! Wheel rotation is BUGGY if you put it inside the looiPaint method. It works perfectly if you put it inside the step method.
    {
        return thisWindow.getMouseWheelRotation();
    }
    /**
    * Internal mouse-x is the x position of the mouse within the virtual object interaction space.
    * @return Internal mouse-x
    */
    protected double getInternalMouseX()
    {
        return thisWindow.getInternalMouseX();
    }
    /**
     * Internal mouse-y is the y position of the mouse within the virtual object interaction space.
     * @return Internal mouse-y
     */
    protected double getInternalMouseY()
    {
        return thisWindow.getInternalMouseY();
    }
    
    /**
     * Returns this LooiWindow's internal width
     * @return 
     */
    protected int getInternalWidth()
    {
        return thisWindow.getInternalWidth();
    }
    /**
     * Returns this LooiWindow's internal height
     * @return 
     */
    protected int getInternalHeight()
    {
        return thisWindow.getInternalHeight();
    }
    
    
    //Paint Methods
    protected int scaleX(double i)//Used to be widthScaleAndOffset
        {
            return (int)(i*getViewOverInternalWidth()) + thisWindow.getViewHorizontalOffset();
        }

    protected int scaleY(double i)//Used to be heightScaleAndOffset
        {
            return (int)(i*getViewOverInternalHeight()) + thisWindow.getViewVerticalOffset();
        }

    protected int scaleW(double i)//Stands for scale width. Used to be widthScale
        {
            return (int)(i*getViewOverInternalWidth());
        }

    protected int scaleH(double i)//Stands for scale height. Used to be heightScale
        {
            return (int)(i*getViewOverInternalHeight());
        }
    protected double getViewOverInternalWidth()
        {
            return (1.0*thisWindow.getViewWidth())/(1.0*thisWindow.getInternalWidth());
        }
    protected double getViewOverInternalHeight()
        {
            return (1.0*thisWindow.getViewHeight())/(1.0*thisWindow.getInternalHeight());
        }
    /**
     * sets whether the following paint operations will be scaled
     * @param drawScaled 
     */
    protected void setDrawScaled(boolean drawScaled)
        {
            this.drawScaled = drawScaled;
        }
    /**
     * returns whether this looiObject is set to draw in a scaled manner
     * @return 
     */
    protected boolean isDrawingScaled()
        {
            return drawScaled;
        }
    /**
     * Sets the pen color
     * @param color Desired pen color
     */
    protected void setColor(Color color)
        {
            g.setColor(color);
        }
    /**
     * Sets the Font for drawing Strings
     * @param font The desired Font
     */
    protected void setFont(Font font)
        {
            g.setFont(font);
        }
    /**
     * Draws a String on this window
     * @param o The String to be drawn
     * @param x The x position
     * @param y The y position
     */
    protected void drawString(Object o, double x, double y)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            if(g.getFont() != null)//scale font
                                {
                                    Font font = new Font(g.getFont().getName(),g.getFont().getStyle(),scaleW(g.getFont().getSize()));//Font scales to width. Font may not perfectly scale because I have to use a STUPID WHOLE NUMBER(int) FONT SIZE O-M-G so stupid!
                                    g.setFont(font);
                                }
                            g.drawString(o.toString(),scaleX(x),scaleY(y));
                        }
                    else
                        {

                            g.drawString(o.toString(), (int)x, (int)y);
                        }

                }
        }

    /**
     * Draws a line on this window
     * @param x X1 position
     * @param y Y1 position
     * @param x2 X2 position
     * @param y2 Y2 position
     */
    protected void drawLine(double x, double y, double x2, double y2)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.drawLine(scaleX(x),scaleY(y),scaleX(x2),scaleY(y2));
                        }
                    else
                        {
                            g.drawLine((int)(x),(int)(y),(int)(x2),(int)(y2));
                        }

                }
        }
    /**
     * Fills a rectangle on this window
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     */
    protected void fillRect(double x, double y, double width, double height)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.fillRect(scaleX(x),scaleY(y),scaleW(width),scaleH(height));
                        }
                    else
                        {
                            g.fillRect((int)(x),(int)(y),(int)(width),(int)(height));
                        }

                }
        }
    /**
     * Draws a rectangle on this window
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     */
    protected void drawRect(double x, double y, double width, double height)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.drawRect(scaleX(x),scaleY(y),scaleW(width),scaleH(height));
                        }
                    else
                        {
                            g.drawRect((int)(x),(int)(y),(int)(width),(int)(height));
                        }

                }
        }
    /**
     * Fills an oval on this window
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     */
    protected void fillOval(double x, double y, double width, double height)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.fillOval(scaleX(x),scaleY(y),scaleW(width),scaleH(height));
                        }
                    else
                        {
                            g.fillOval((int)(x),(int)(y),(int)(width),(int)(height));
                        }

                }
        }
    /**
     * Draws an oval on this window
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     */
    protected void drawOval(double x, double y, double width, double height)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.drawOval(scaleX(x),scaleY(y),scaleW(width),scaleH(height));
                        }
                    else
                        {
                            g.drawOval((int)(x),(int)(y),(int)(width),(int)(height));
                        }

                }
        }
    /**
     * Draws a polygon on this window. Will be scaled for certain
     * @param xValues The x values of the points of the polygon
     * @param yValues The y values of the points of the polygon
     */
    protected void drawPolygon(double[] xValues, double[] yValues)
        {
            int[] iXValues = new int[xValues.length];
            int[] iYValues = new int[xValues.length];
            for(int i = 0; i < xValues.length; i++)
                {
                    iXValues[i] = scaleX(xValues[i]);
                    iYValues[i] = scaleY(yValues[i]);
                }
            g.drawPolygon(new Polygon(iXValues,iYValues,iXValues.length));
        }
    /**
     * Fills a polygon on this window
     * @param xValues The x values of the points of the polygon
     * @param yValues The y values of the points of the polygon
     */
    protected void fillPolygon(double[] xValues, double[] yValues)
        {
            int[] iXValues = new int[xValues.length];
            int[] iYValues = new int[xValues.length];
            for(int i = 0; i < xValues.length; i++)
                {
                    iXValues[i] = scaleX(xValues[i]);
                    iYValues[i] = scaleY(yValues[i]);
                }
            g.fillPolygon(new Polygon(iXValues,iYValues,iXValues.length));
        }
    protected void fillPolygon(Point...points)
        {
            double[] xValues = new double[points.length];
            double[] yValues = new double[points.length];
            for(int i = 0; i < points.length; i++)
            {
                xValues[i] = points[i].getX();
                yValues[i] = points[i].getY();
            }
            fillPolygon(xValues,yValues);
        }
    /**
     * Draws an image on this window
     * @param ima The image to draw
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     * @param imageObserver Something
     */
    protected void drawImage(Image ima, double x, double y, double width, double height, ImageObserver imageObserver)
        {
            if(g != null)
                {
                    if(drawScaled)
                        {
                            g.drawImage(ima,scaleX(x),scaleY(y),scaleW(width),scaleH(height),imageObserver);
                        }
                    else
                        {
                            g.drawImage(ima,(int)(x),(int)(y),(int)(width),(int)(height),imageObserver);
                        }

                }
        }
    /**
     * Draws an image on this window
     * @param ima The image to draw
     * @param x X position
     * @param y Y position
     * @param width The width
     * @param height The height
     */
    protected void drawImage(Image ima, double x, double y, double width, double height)
        {
            drawImage(ima,x,y,width,height,null);
        }
    /**
     * Fills a 2D quadrilateral on this window
     * @param xValues The x values of the points of the polygon
     * @param yValues The y values of the points of the polygon
     * @param texture The desired Texture
     */
    protected void fillQuadrilateral(double[] xValues, double[] yValues, Texture texture)//2 Dimensional
        {
            Point p0 = new Point(xValues[0],yValues[0]);
            Point p1 = new Point(xValues[1],yValues[1]);
            Point p2 = new Point(xValues[2],yValues[2]);
            Point p3 = new Point(xValues[3],yValues[3]);
            fillQuadrilateral(p0,p1,p2,p3,texture);
        }
    /**
     * Fills a 2D quadrilateral on this window
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param p4 Point 4
     * @param texture The desired Texture
     */
    protected void fillQuadrilateral(Point p1, Point p2, Point p3, Point p4, Texture texture)//2 Dimensional
        {
            Point[][] points = new Point[texture.rows()+1][texture.columns()+1];
            double p1p2dist = p1.getDistance(p2);
            double p2p3dist = p2.getDistance(p3);
            double p3p4dist = p3.getDistance(p4);
            double p1p4dist = p1.getDistance(p4);
            double rows = texture.rows();
            double columns = texture.columns();

            for(int o = 0; o < points.length; o++)
                {

                    for(int i = 0; i < points[0].length; i++)
                        {
                            Point rowPoint = p1.moveTowards(p4,p1p4dist/rows*o);
                            Point oppositeRowPoint = p2.moveTowards(p3,p2p3dist/rows*o);
                            Point finalPoint = rowPoint.moveTowards(oppositeRowPoint,(rowPoint.getDistance(oppositeRowPoint))/columns*i); 
                            points[o][i] = finalPoint;
                        }
                }
            for(int o = 0; o < rows; o++)
                {
                    for(int i = 0; i < columns; i++)
                        {

                            setColor(texture.getColors()[o][i]);
                            double[] xValues = {points[o][i].getX(),points[o+1][i].getX(),points[o+1][i+1].getX(),points[o][i+1].getX()};
                            double[] yValues = {points[o][i].getY(),points[o+1][i].getY(),points[o+1][i+1].getY(),points[o][i+1].getY()};
                            fillPolygon(xValues,yValues);
                        }
                }
        }
    /**
     * Fills a 2D Quadrilateral of Graphics g's current color
     * @param xValues The xValues
     * @param yValues The yValues
     * @param texture The Texture used
     * @param repeatPercentageWidth How much of one width one repetition of the texture covers
     * @param repeatPercentageHeight How much of one height one repetition of the texture covers
     */
    protected void fillQuadrilateral(double[] xValues, double[] yValues, Texture texture,double repeatPercentageWidth, double repeatPercentageHeight)//2 Dimensional
        {
            Point p0 = new Point(xValues[0],yValues[0]);
            Point p1 = new Point(xValues[1],yValues[1]);
            Point p2 = new Point(xValues[2],yValues[2]);
            Point p3 = new Point(xValues[3],yValues[3]);
            fillQuadrilateral(p0,p1,p2,p3,texture,repeatPercentageWidth,repeatPercentageHeight);
        }
    /**
     * Fills a 2D Quadrilateral of Graphics g's current color
     * @param p1 Point 1
     * @param p2 Point 2
     * @param p3 Point 3
     * @param p4 Point 4
     * @param texture The Texture used
     * @param repeatPercentageWidth How much of one width one repetition of the texture covers
     * @param repeatPercentageHeight How much of one height one repetition of the texture covers
     */
    protected void fillQuadrilateral(Point p1, Point p2, Point p3, Point p4, Texture texture, double repeatPercentageWidth, double repeatPercentageHeight)//repeatPercentageHeight percentage of height that one repetition takes
        {//2 Dimensional
            double repeatDecimalWidth = repeatPercentageWidth/100.0;
            double repeatDecimalHeight = repeatPercentageHeight/100.0;
            double widthRepetitions = 1/repeatDecimalWidth;
            double heightRepetitions = 1/repeatDecimalHeight;
            int numColumns = (int)(widthRepetitions*texture.columns());//num columns
            int numRows = (int)(heightRepetitions*texture.rows());//num rows
            Point[][] points = new Point[numRows+1][numColumns+1];
            double p1p2dist = p1.getDistance(p2);
            double p2p3dist = p2.getDistance(p3);
            double p3p4dist = p3.getDistance(p4);
            double p1p4dist = p1.getDistance(p4);
            for(int o = 0; o < points.length; o++)
                {
                    for(int i = 0; i < points[0].length; i++)
                        {
                            Point rowPoint = p1.moveTowards(p4,p1p4dist/numRows*o);
                            Point oppositeRowPoint = p2.moveTowards(p3,p2p3dist/numRows*o);
                            Point finalPoint = rowPoint.moveTowards(oppositeRowPoint,(rowPoint.getDistance(oppositeRowPoint))/numColumns*i);
                            points[o][i] = finalPoint;
                        }
                }
            for(int o = 0; o < numRows; o++)
                {
                    for(int i = 0; i < numColumns; i++)
                        {

                            setColor(texture.getColors()[o%texture.rows()][i%texture.columns()]);
                            double[] xValues = {points[o][i].getX(),points[o+1][i].getX(),points[o+1][i+1].getX(),points[o][i+1].getX()};
                            double[] yValues = {points[o][i].getY(),points[o+1][i].getY(),points[o+1][i+1].getY(),points[o][i+1].getY()};
                            fillPolygon(xValues,yValues);
                        }
                }
        }
    protected void setFontSize(int size)
    {
        setFont(new Font(getGraphics().getFont().getName(),getGraphics().getFont().getStyle(),size));
    }
    protected Graphics getGraphics()
    {
        return thisWindow().getGraphics();
    }
    final void uponActivationFinal(Object...activationInfo)
    {
        active = true;
        uponActivation(activationInfo);
    }
    final void uponDeactivationFinal(Object...activationInfo)
    {
        active = false;
        uponDeactivation(activationInfo);
    }
    void setGraphics(Graphics g)
    {
        this.g = g;
    }
    
}
