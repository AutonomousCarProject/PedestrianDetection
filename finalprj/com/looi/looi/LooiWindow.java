/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Comparator;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *
 * @author peter_000
 */
public class LooiWindow 
{
    //static:
    
    public static final int DEFAULT_INTERNAL_WIDTH = 1600;//changed on 6-13-17 from 1400 to 1600
    public static final int DEFAULT_INTERNAL_HEIGHT = 1000;
    public static final int DEFAULT_UPDATES_PER_SECOND = 60;
    public static final int DEFAULT_NUM_KEYS = 850;
    public static final String GAME_LOOP_THREAD_NAME = "game_loop_thread";
    public static final String PAINT_THREAD_NAME = "paint_thread";
    private static LooiWindow mainWindow = null;
    
    /**
     * Declares the main window for the entire program
     * @param mainWindow the LooiWindow to be declared as the main window
     */
    public static void setMainWindow(LooiWindow mainWindow)
    {
        LooiWindow.mainWindow = mainWindow;
    }
    public static LooiWindow getMainWindow()
    {
        return mainWindow;
    }
    
    
    //instance:
    
    //view = perceived, room = window
    private int internalWidth;
    private int internalHeight;
    private int viewWidth;
    private int viewHeight;
    private boolean multiThread;
    private int updatesPerSecond;
    private boolean gameLoopInTime = true;
    private boolean running = true;
    private boolean paused = false;
    private int viewHorizontalOffset = 0;
    private int viewVerticalOffset = 0;
    
    private MouseEvent[] inputMousePressed = new MouseEvent[3];// 0 = left, 1 = middle, 2 = right //all inputs must be set to false at the start of each game loop iteration
    private MouseEvent[] inputMouseReleased = new MouseEvent[3];
    private MouseEvent[] mouseEngaged = new MouseEvent[3];
    private MouseEvent[] mousePressed = new MouseEvent[3];
    private MouseEvent[] mouseReleased = new MouseEvent[3];
    
    private KeyEvent[] inputKeyPressed = new KeyEvent[DEFAULT_NUM_KEYS];
    private KeyEvent[] inputKeyReleased = new KeyEvent[DEFAULT_NUM_KEYS];
    private KeyEvent[] keyEngaged = new KeyEvent[DEFAULT_NUM_KEYS];
    private KeyEvent[] keyPressed = new KeyEvent[DEFAULT_NUM_KEYS];
    private KeyEvent[] keyReleased = new KeyEvent[DEFAULT_NUM_KEYS];
    
    private boolean inputWindowClosing = false;
    private boolean windowClosing = false;
    
    private int windowMouseX;
    private int windowMouseY;
    
    private double inputAccumulatedWheelRotation = 0;
    private double accumulatedWheelRotation = 0;
    
    private JFrame frame;
    private Canvas canvas;
    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    
    private Thread constructorThread;
    private Thread gameLoopThread;
    
    private ArrayList<LooiObject> activeLooiObjects = new ArrayList<>();
    private ArrayList<LooiObject> paintActiveLooiObjects = new ArrayList<>();
    private Graphics g;
    
    private Comparator looiObjectSorter = new Comparator<LooiObject>()
    {
        public int compare(LooiObject a, LooiObject b) 
        {
            return a.compareTo(b);
        }
    };
    
    
    /**
     * Creates a new full screen LooiWindow
     * @param UPS Desired updates per second
     * @param multiThread Whether to use multi threading
     */
    public LooiWindow(int UPS, boolean multiThread)
    {
        this("",(int)(toolkit.getScreenSize().getWidth()),(int)(toolkit.getScreenSize().getHeight()),false,UPS,multiThread);
    }
    /**
     * Creates a new full screen LooiWindow
     * @param UPS Desired updates per second
     */
    public LooiWindow(int UPS)
    {
        this("",(int)(toolkit.getScreenSize().getWidth()),(int)(toolkit.getScreenSize().getHeight()),false,UPS,false);
    }
    /**
     * Creates a new full screen LooiWindow. This constructor is best for beginners. This one does not use threads.
     */
    public LooiWindow()
    {
        this("",(int)(toolkit.getScreenSize().getWidth()),(int)(toolkit.getScreenSize().getHeight()),false,DEFAULT_UPDATES_PER_SECOND,false);
    }
    /**
    * Creates a new not full screen LooiWindow
    * @param title The title of the LooiWindow
    * @param width The width of the LooiWindow
    * @param height The height of the LooiWindow
    * @param decoration Whether to decorate the window
    * @param multiThread Whether to use multi threading
    * 
    */
    public LooiWindow(String title, int width, int height, boolean decoration, int UPS, boolean multiThread)
    {
        if(mainWindow == null)
            {
                setMainWindow(this);
            }
        this.multiThread = multiThread;
        this.updatesPerSecond = UPS;
        
        
        
        viewWidth = width;
        viewHeight = height;
        internalWidth = DEFAULT_INTERNAL_WIDTH;
        internalHeight = DEFAULT_INTERNAL_HEIGHT;


        frame = new JFrame();
        frame.setSize(width,height);
        frame.setTitle(title);
        frame.setUndecorated(!decoration);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width,height));
        canvas.setMaximumSize(new Dimension(width,height));
        canvas.setMinimumSize(new Dimension(width,height));

        frame.add(canvas);
        frame.pack();//is this necessary?

        //listeners
        canvas.addMouseListener(new MouseListener()
        {
            public void mousePressed(MouseEvent me) 
            {
                if(me.getButton() == MouseEvent.BUTTON1)
                {
                    inputMousePressed[0] = me;
                }
                if(me.getButton() == MouseEvent.BUTTON2)
                {
                    inputMousePressed[1] = me;
                }
                if(me.getButton() == MouseEvent.BUTTON3)
                {
                    inputMousePressed[2] = me;
                }
            }
            public void mouseReleased(MouseEvent me) 
            {
                if(me.getButton() == MouseEvent.BUTTON1)
                {
                    inputMouseReleased[0] = me;
                }
                if(me.getButton() == MouseEvent.BUTTON2)
                {
                    inputMouseReleased[1] = me;
                }
                if(me.getButton() == MouseEvent.BUTTON3)
                {
                    inputMouseReleased[2] = me;
                }
            }
            public void mouseClicked(MouseEvent me) {}
            public void mouseEntered(MouseEvent me) {}
            public void mouseExited(MouseEvent me) {}
            
        });
        canvas.addKeyListener(new KeyListener()
        {
            public void keyPressed(KeyEvent ke) 
            {
                inputKeyPressed[ke.getKeyCode()] = ke;
            }
            public void keyReleased(KeyEvent ke) 
            {
                inputKeyReleased[ke.getKeyCode()] = ke;
            }
            public void keyTyped(KeyEvent ke) {}
        });
        canvas.addMouseMotionListener(new MouseMotionListener()
        {
            @Override
            public void mouseDragged(MouseEvent me) 
            {
                windowMouseX = me.getX();
                windowMouseY = me.getY();
            }

            @Override
            public void mouseMoved(MouseEvent me) 
            {
                windowMouseX = me.getX();
                windowMouseY = me.getY();
            }
            
        });
        frame.addWindowListener(new WindowListener()
        {
            @Override
            public void windowOpened(WindowEvent we) {}

            @Override
            public void windowClosing(WindowEvent we) 
            {
                inputWindowClosing = true;
            }
            public void windowClosed(WindowEvent we) {}
            public void windowIconified(WindowEvent we) {}
            public void windowDeiconified(WindowEvent we) {}
            public void windowActivated(WindowEvent we) {}
            public void windowDeactivated(WindowEvent we) {}
            
        });
        canvas.addMouseWheelListener(new MouseWheelListener()
        {
            @Override
            public void mouseWheelMoved(MouseWheelEvent mwe) 
            {
                inputAccumulatedWheelRotation += mwe.getPreciseWheelRotation();
            }
            
        });
        
        constructorThread = Thread.currentThread();
        gameLoopThread = new Thread(new GameLoopThread());
        gameLoopThread.setName(GAME_LOOP_THREAD_NAME);
        
        gameLoopThread.start();
    }
    private class GameLoopThread implements Runnable
    {
        
        @Override
        public void run()
        {
            try
            {
                constructorThread.join();
            }
            catch(InterruptedException ie)
            {
                
            }
            if(multiThread)
            {
                gameLoopMultiThread();
            }
            else
            {
                gameLoopSingleThread();
            }
        }
    }
    
    private void gameLoopMultiThread()
    {
        long start = System.nanoTime();
        while(running)
        {
            double nanosecondsPerStep = 1000000000.0/updatesPerSecond;
            savePaintInformation();
            Thread painter = new Thread()
            {
                public void run()
                {
                    paint();
                }
            };
            painter.setName(PAINT_THREAD_NAME);
            painter.start();
            //step thread branch starts
            
            ArrayList<LooiObject> paintActiveLooiObjectsTemp = (ArrayList<LooiObject>)activeLooiObjects.clone();//Order the painting list. However, doing so here may compromise SLIGHTLY on the accuracy of the functionality. However, it will result in SLIGHTLY increased performance. No need to worry at all. I don't even know why I wrote this.
            paintActiveLooiObjectsTemp.sort(looiObjectSorter);
            step();
            //step thread branch ends
            try{painter.join();}catch(Exception e){}
            paintActiveLooiObjects = paintActiveLooiObjectsTemp;
            gameLoopInTime = false;

            while(!(System.nanoTime() - start >= nanosecondsPerStep))
                {
                    gameLoopInTime = true;
                }

            start = System.nanoTime();
        }
    }
    
    private void gameLoopSingleThread()
    {
        long start = System.nanoTime();
        while(running)
            {
                double nanosecondsPerStep = 1000000000.0/updatesPerSecond;
                savePaintInformation();
                paintActiveLooiObjects = (ArrayList<LooiObject>)activeLooiObjects.clone();//(ArrayList<LooiObject>)Calculations.mergeSort((ArrayList<LooiObject>)activeLooiObjects.clone()); //Order the painting list
                paintActiveLooiObjects.sort(looiObjectSorter);
                paint();
                step();
                gameLoopInTime = false;
                while(!(System.nanoTime() - start >= nanosecondsPerStep))
                    {
                        gameLoopInTime = true;
                    }
                start = System.nanoTime();
            }
    }
    private void paint()
    {
        
        BufferStrategy bs = canvas.getBufferStrategy();
        if(bs == null)
            {
                canvas.createBufferStrategy(3);
                return;
            }
        g = bs.getDrawGraphics();

        g.clearRect(0,0,(int)getWindowWidth(),(int)getWindowHeight());
        
        for(int i = paintActiveLooiObjects.size()-1; i > -1 ; i--)
            {
                paintActiveLooiObjects.get(i).setGraphics(g);
                g.setColor(Color.black);//set default color here! omg it used to be yellow and I had no idea... and i was so confused y everything was YELLOW
                paintActiveLooiObjects.get(i).looiPaint();
            }



        g.setColor(Color.black);
        g.fillRect(0,0,getWindowWidth(),viewVerticalOffset);
        g.fillRect(0,viewHeight+viewVerticalOffset,getWindowWidth(),getWindowHeight());
        g.fillRect(0,0,viewHorizontalOffset,getWindowHeight());
        g.fillRect(viewWidth+viewHorizontalOffset,0,getWindowWidth(),getWindowHeight());

        bs.show();
        g.dispose();
        //g.finalize();//This line gives the program a TINY performance boost
    }
    private void step()
    {
        
        //read listener inputs
        for(int i = 0; i < inputMousePressed.length; i++)
        {
            mousePressed[i] = inputMousePressed[i];
            if(mouseEngaged[i] == null && inputMousePressed[i] != null)
            {
                mouseEngaged[i] = inputMousePressed[i];
            }
        }
        for(int i = 0; i < inputMouseReleased.length; i++)
        {
            mouseReleased[i] = inputMouseReleased[i];
            if(mouseEngaged[i] != null && inputMouseReleased[i] != null)
            {
                mouseEngaged[i] = null;
            }
        }
        for(int i = 0; i < inputKeyPressed.length; i++)
        {
            keyPressed[i] = null;
            if(keyEngaged[i] == null && inputKeyPressed[i] != null)
            {
                keyEngaged[i] = inputKeyPressed[i];
                keyPressed[i] = inputKeyPressed[i];
            }
        }
        for(int i = 0; i < inputKeyReleased.length; i++)
        {
            keyReleased[i] = inputKeyReleased[i];
            if(keyEngaged[i] != null && inputKeyReleased[i] != null)
            {
                keyEngaged[i] = null;
            }
        }
        windowClosing = inputWindowClosing;
        accumulatedWheelRotation = inputAccumulatedWheelRotation;

        //set inputs to false or 0 or null
        for(int i = 0; i < inputMousePressed.length; i++)
        {
            inputMousePressed[i] = null;
        }
        for(int i = 0; i < inputMouseReleased.length; i++)
        {
            inputMouseReleased[i] = null;
        }
        for(int i = 0; i < inputKeyPressed.length; i++)
        {
            inputKeyPressed[i] = null;
        }
        for(int i = 0; i < inputKeyReleased.length; i++)
        {
            inputKeyReleased[i] = null;
        }
        inputWindowClosing = false;
        inputAccumulatedWheelRotation = 0;
            
        if(!paused)
        {
            for(MouseEvent e : mousePressed)
            {
                if(e != null)
                {
                    MouseEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.mousePressed(f);} ); 
                }
            }
            for(MouseEvent e : mouseEngaged)
            {
                if(e != null)
                {
                    MouseEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.mouseEngaged(f);} ); 
                }
            }
            for(MouseEvent e : mouseReleased)
            {
                if(e != null)
                {
                    MouseEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.mouseReleased(f);} ); 
                }
            }
            
            for(KeyEvent e : keyPressed)
            {
                if(e != null)
                {
                    KeyEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.keyPressed(f);} ); 
                }
            }
            for(KeyEvent e : keyEngaged)
            {
                if(e != null)
                {
                    KeyEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.keyEngaged(f);} ); 
                }
            }
            for(KeyEvent e : keyReleased)
            {
                if(e != null)
                {
                    KeyEvent f = e;
                    iterateThroughActiveLooiObjectsAndDo( (l) -> {l.keyReleased(f);} ); 
                }
            }
            //iterate through activeLooiObjects
            for(int i = 0; i < activeLooiObjects.size(); i++)//int i = 0; i < objects.size(); i++
            {
                activeLooiObjects.get(i).looiStep();
            }
        }
    }
    private void iterateThroughActiveLooiObjectsAndDo(LooiObjectAction l)
    {
        for(int i = 0; i < activeLooiObjects.size(); i++)
        {
            l.action(activeLooiObjects.get(i)); 
        }
    }
    private interface LooiObjectAction{public void action(LooiObject l);}
    
    private void savePaintInformation()
    {
        for(int i = 0; i < activeLooiObjects.size(); i++)//int i = 0; i < objects.size(); i++
        {
            activeLooiObjects.get(i).savePaintInformation();
        }
    }
    /**
    * Arranges the window nicely so that the internal dimensions are in proportion to the view dimensions, and the view dimensions are as large as they can be within the bounds of the window
    */
    public void fitWindow()//setPerceivedDimensionsToProportionalMaxFullScreenLandscape() can be set to private. This method replaces it. Used to be called setPerceivedDimensionsToProportionalMaxRoomSize
    {//honestly this should be in the looiwindow class
        double internalRatio = 1.0*this.getInternalWidth()/this.getInternalHeight();
        double windowRatio = 1.0*this.getWindowWidth()/this.getWindowHeight();

        if(windowRatio > internalRatio)
            {
                //scale up to height
                this.setViewWidth((int)(1.0*this.getWindowHeight()/this.getInternalHeight() * this.getInternalWidth()));
                this.setViewHeight((int)(1.0*this.getWindowHeight()/this.getInternalHeight() * this.getInternalHeight()));
                this.setViewHorizontalOffset((this.getWindowWidth() - this.getViewWidth())/2);
            }
        else
            {
                this.setViewWidth((int)(1.0*this.getWindowWidth()/this.getInternalWidth() * this.getInternalWidth()));
                this.setViewHeight((int)(1.0*this.getWindowWidth()/this.getInternalWidth() * this.getInternalHeight()));
                this.setViewVerticalOffset((this.getWindowHeight() - this.getViewHeight())/2);
            }
    }
    public int getInternalWidth(){return internalWidth;}
    public int getInternalHeight(){return internalHeight;}
    public int getViewWidth(){return viewWidth;}
    public int getViewHeight(){return viewHeight;}
    public int getWindowWidth(){return frame.getWidth();}
    public int getWindowHeight(){return frame.getHeight();}
    public double getScreenWidth(){return toolkit.getScreenSize().getWidth();}
    public double getScreenHeight(){return toolkit.getScreenSize().getHeight();}
    
    public void setInternalWidth(int internalWidth){this.internalWidth = internalWidth;}
    public void setInternalHeight(int internalHeight){this.internalHeight = internalHeight;}
    public void setViewWidth(int viewWidth){this.viewWidth = viewWidth;}
    public void setViewHeight(int viewHeight){this.viewHeight = viewHeight;}
    public void setWindowWidth(int windowWidth){frame.setSize(windowWidth,frame.getHeight());}
    public void setWindowHeight(int windowHeight){frame.setSize(frame.getWidth(),windowHeight);}
    
    public void setViewHorizontalOffset(int i){this.viewHorizontalOffset = i;}
    public void setViewVerticalOffset(int i){this.viewVerticalOffset = i;}
    
    public int getViewHorizontalOffset(){return viewHorizontalOffset;}
    public int getViewVerticalOffset(){return viewVerticalOffset;}
    /**
    * Activates a LooiObject so that looiStep() and looiPaint() are called at every step
    * @param looiObject The LooiObject to be activated
    * 
    */
    protected void activate(LooiObject looiObject, Object...activationInfo)
    {
        if(!looiObject.isActive())
        {
            activeLooiObjects.add(looiObject);
            looiObject.uponActivationFinal(activationInfo);
        }
    }
    /**
    * Deactivates a LooiObject so that looiStep() and looiPaint() are no longer called at every step
    * @param looiObject The LooiObject to be deactivated
    */
    protected void deactivate(LooiObject looiObject, Object...activationInfo)
        {
            if(looiObject.isActive())
                {
                    activeLooiObjects.remove(looiObject);
                    looiObject.uponDeactivationFinal(activationInfo);
                }
        }
    
    /**
    * Gets the layer of the LooiObject that has the most negative (top) 
    * layer
    * @return 
    */
    public double getTopLayer()
    {
        
        if(paintActiveLooiObjects != null && paintActiveLooiObjects.size() > 0)
        {
            return paintActiveLooiObjects.get(0).getLayer();
        }
        return 0;
    }
    /**
    * Gets the layer of the LooiObject that has the most positive (bottom) 
    * layer
    * @return 
    */
    public double getBottomLayer()
    {
        if(paintActiveLooiObjects != null && paintActiveLooiObjects.size() > 0)
            return paintActiveLooiObjects.get(paintActiveLooiObjects.size()-1).getLayer();
        return 0;
    }
    /**
     * Pauses the game loop so that objects temporarily cease to update. This
     * should only be used for debugging purposes, since idk how you're going to
     * get this game unpaused if you have no more updation happening!
     */
    public void pause()
    {
        paused = true;
    }
    /**
     * Unpause the game
     */
    public void unpause()
    {
        paused = false;
    }
    /**
     * Closes this window
     */
    public void closeWindow()
    {
        running = false;
        frame.setVisible(false);
    }
    /**
    * Returns the number of active LooiObjects. The higher the number of active LooiObjects there are, the slower the program will run.
    * @return The number of active LooiObjects
    */
    public int getNumActiveObjects()
    {
        return activeLooiObjects.size();
    }
    /**
     * Returns the active LooiObject that corresponds to the index given. Hint: All active LooiObjects for one window are stored in a single ArrayList
     * @param index The index of the active LooiObject
     * @return The active LooiObject that corresponds to the index given
     */
    public LooiObject getActiveObject(int index)
    {
        return activeLooiObjects.get(index);
    }
    /**
     * Same thing as getActiveObject, but this time it fetches the object from
     * the sorted ArrayList of LooiObjects
     * @param index
     * @return 
     */
    public LooiObject getActiveObjectSorted(int index)
    {
        ArrayList<LooiObject> sortedLooiObjects = (ArrayList<LooiObject>)activeLooiObjects.clone();
        sortedLooiObjects.sort(looiObjectSorter);
        return sortedLooiObjects.get(index);
    }
    /**
    * Returns whether a certain LooiObject is active
    * @param looiObject The LooiObject in question
    * @return Whether the LooiObject is active
    */
    public boolean hasActiveLooiObject(LooiObject looiObject)
    {
        return activeLooiObjects.contains(looiObject);
    }
    /**
    * Sets the updates per second for this LooiWindow
    * @param UPS The desired number of updates per second
    */
    public void setUpdateRate(int UPS)//needs testing
    {
        updatesPerSecond = UPS;
    }
    /**
     * Gets the declared number of updates(looiSteps) per second for this LooiWindow
     * @return Updates per second
     */
    public double getUPS()
    {
        return updatesPerSecond;
    }
    /**
    * Returns true if computer is able to keep up with the game loop speed. 
    * Returns false if painting and/or stepping is too demanding on the 
    * computer for the computer to be able to keep up to the rhythm of the 
    * specified number of UPS.
    * @return Whether the loop is in time
    */
    public boolean isLoopInTime()
    {
        return gameLoopInTime;
    }
    /**
    * Returns the canvas of this window
    * @return The canvas of this window
    */
    public Canvas getCanvas()
    {
        return canvas;
    }
    /**
     * Returns the JFrame that this LooiWindow uses
     * @return 
     */
    public JFrame getJFrame()
    {
        return frame;
    }
    
    /**
    * Returns whether a key is engaged. Engaged means that it is being held down.
    * @param key The key in question. Use KeyEvent.VK_<Insert key title>
    * @return Whether the key is engaged
    */
    public boolean keyEngaged(int key)
    {
        return keyEngaged[key] != null;//key down
    }
    /**
     * Returns whether a key has just been pressed
     * @param key The key in question. Use KeyEvent.VK_<Insert key title>
     * @return Whether the key has just been pressed
     */
    public boolean keyPressed(int key)
    {
        return keyPressed[key] != null;
    }
    /**
     * Returns whether a key has just been released
     * @param key The key in question. Use KeyEvent.VK_<Insert key title>
     * @return Whether the key has just been released
     */
    public boolean keyReleased(int key)
    {
        return keyReleased[key] != null;
    }
    /**
     * Returns whether the left mouse button has just been pressed
     * @return Whether the left mouse button has just been pressed
     */
    public boolean mouseLeftPressed()
    {
        return mousePressed[0] != null;
    }
    /**
     * Returns whether the middle mouse button has just been pressed
     * @return Whether the middle mouse button has just been pressed
     */
    public boolean mouseMiddlePressed()
    {
        return mousePressed[1] != null;
    }
    /**
     * Returns whether the right mouse button has just been pressed
     * @return Whether the right mouse button has just been pressed
     */
    public boolean mouseRightPressed()
    {
        return mousePressed[2] != null;
    }
    /**
     * Returns whether the left mouse button has just been released
     * @return Whether the left mouse button has just been released
     */
    public boolean mouseLeftReleased()
    {
        return mouseReleased[0] != null;
    }
    /**
     * Returns whether the middle mouse button has just been released
     * @return Whether the middle mouse button has just been released
     */
    public boolean mouseMiddleReleased()
    {
        return mouseReleased[1] != null;
    }
    /**
     * Returns whether the right mouse button has just been released
     * @return Whether the right mouse button has just been released
     */
    public boolean mouseRightReleased()
    {
        return mouseReleased[2] != null;
    }
    /**
     * Returns whether the left mouse button is engaged. Engaged means that it is being held down.
     * @return Whether the left mouse button is engaged
     */
    public boolean mouseLeftEngaged()
    {
        return mouseEngaged[0] != null;
    }
    /**
     * Returns whether the middle mouse button is engaged
     * @return Whether the middle mouse button is engaged
     */
    public boolean mouseMiddleEngaged()
    {
        return mouseEngaged[1] != null;
    }
    /**
     * Returns whether the right mouse button is engaged
     * @return Whether the right mouse button is engaged
     */
    public boolean mouseRightEngaged()
    {
        return mouseEngaged[2] != null;
    }
    /**
     * Returns the mouse wheel rotation value. The mouse wheel rotation value represents in which direction the mouse wheel is currently turning.
     * @return The mouse wheel rotation value
     */
    public double getMouseWheelRotation()//Warning! Wheel rotation is BUGGY if you put it inside the looiPaint method. It works perfectly if you put it inside the step method.
    {
        return accumulatedWheelRotation;
    }
    /**
     * Returns whether the window close button (the red "X" in the upper corner of the window) has been pressed at all in the past.
     * @return Whether the window close button has been pressed at all in the past.
     */
    public boolean windowCloseButton()
    {
        return this.windowClosing;
    }
    /**
    * Window mouse-x is the x position of the mouse within the entire window
    * @return Window mouse-x
    */
    public int getWindowMouseX()
    {
        return windowMouseX;
    }
    /**
     * Window mouse-y is the y position of the mouse within the entire window
     * @return Window mouse-y
     */
    public int getWindowMouseY()
    {
        return windowMouseY;
    }
    /**
     * Internal mouse-x is the x position of the mouse within the virtual object interaction space.
     * @return Internal mouse-x
     */
    public double getInternalMouseX()
    {
        return ((((double)(getWindowMouseX() - getViewHorizontalOffset()))/getViewWidth())*getInternalWidth());
    }
    /**
     * Internal mouse-y is the y position of the mouse within the virtual object interaction space.
     * @return Internal mouse-y
     */
    public double getInternalMouseY()
    {
        return ((((double)(getWindowMouseY() - getViewVerticalOffset()))/getViewHeight())*getInternalHeight());
    }
    /**
     * Internal mouse-x is the x position of the mouse within the viewing port of the window.
     * @return View mouse-x
     */
    public int getViewMouseX()// NEEDS TESTING!
    {
        return getWindowMouseX() - getViewHorizontalOffset();
    }
    /**
     * Internal mouse-y is the y position of the mouse within the viewing port of the window.
     * @return View mouse-y
     */
    public int getViewMouseY()// NEEDS TESTING!
    {
        return getWindowMouseY() - getViewVerticalOffset();
    }
    /**
     * Gets the current graphics object
     * @return 
     */
    public Graphics getGraphics()
    {
        return g;
    }
    /**
     * Returns the mouse x position in the entire screen
     * @return 
     */
    public static int getScreenMouseX()
    {
        PointerInfo p = MouseInfo.getPointerInfo();
        Point b = p.getLocation();
        return (int)b.getX();
    }
    /**
     * Returns the mouse y position in the entire screen
     * @return 
     */
    public static int getScreenMouseY()
    {
        PointerInfo p = MouseInfo.getPointerInfo();
        Point b = p.getLocation();
        return (int)b.getY();
    }
}
