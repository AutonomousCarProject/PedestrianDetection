/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ai_data_creation;

import autonomouscarfinalprogram2.Control;
import com.looi.looi.LooiObject;
import com.looi.looi.Point;
import com.looi.looi.gui_essentials.AstheticButton;
import com.looi.looi.gui_essentials.Background;
import com.looi.looi.gui_essentials.Window;
import com.looi.looi.gui_essentials.Window.DecorationBar;
import group3.MovingBlob;
import group5.IImageBoxDrawer;
import group5.IImageBoxDrawer.Rectangle;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author peter_000
 */
public class BlobSelector extends LooiObject
{
    public static final Color DEFAULT_SELECTION_COLOR = Color.CYAN;
    public static final Color DEFAULT_HOVER_COLOR = new Color(255,255,224);
    public static final Color DEFAULT_LASSO_COLOR = new Color(0,255,255,135);
    public static final double DEFAULT_NEW_LINE_THICKNESS_RATIO = 2;
    public static final String DEFAULT_FILE_DOCUMENT_NAME = "files.txt";
    
    private IImageBoxDrawer ibd;
    private Point lassoStart;
    private Point lassoEnd;
    private Point[] lassoSelection;
    private List<Rectangle> selectedRectangles;
    private Color hoverColor = DEFAULT_HOVER_COLOR;
    private Color selectedColor = DEFAULT_SELECTION_COLOR;
    private Color lassoColor = DEFAULT_LASSO_COLOR;
    private Color originalRectangleColor;
    private int originalLineThickness;
    private int newLineThickness;
    private AstheticButton fileWriter;
    private AstheticButton activateWindow;
    private AstheticButton play;
    private AstheticButton pause;
    private AstheticButton step;
    private Window window;
    private String fileDocumentName;
    private Control control;
    
    public BlobSelector(IImageBoxDrawer ibd, Control control, Window attachedWindow)
    {
        this.control = control;
        this.ibd = ibd;
        setLayer(-9999);
        this.originalRectangleColor = ibd.getRectangleColor();
        this.originalLineThickness = ibd.getLineThickness();
        this.newLineThickness = (int)(originalLineThickness * DEFAULT_NEW_LINE_THICKNESS_RATIO);
        //activateWindow = makeActivateWindowButton();
        window = attachedWindow;
        
        fileWriter = makeFileWriterButton();
        play = makeUnpauseButton();
        pause = makePauseButton();
        step = makeStepButton();
        
        
        window.add(fileWriter);
        window.add(play);
        window.add(pause);
        window.add(step);
        this.fileDocumentName = DEFAULT_FILE_DOCUMENT_NAME;
    }
    
    protected Window makeWindow()
    {
        Window w = new Window(100,100,600,600,Background.WHITE_BACKGROUND);
        DecorationBar d;
        w.add(d = w.new DecorationBar()); 
        d.add(w.new ExitButton()); 
        return w;
    }
    protected AstheticButton makeActivateWindowButton()
    {
        return new AstheticButton(0,0,100,50,"Open Window",new Background(new Color(150,255,150))) {
            @Override
            protected void action() 
            {
                window.activate();
                
            }
        };
    }
    protected AstheticButton makeFileWriterButton()
    {
        return new AstheticButton(10,70,100,50,"Save File",new Background(new Color(150,255,150))) 
        {
            @Override
            protected void action() 
            {
                try
                {
                    saveFile();
                }catch(Exception e){e.printStackTrace();}
                
            }
        };
    }
    protected AstheticButton makePauseButton()
    {
        return new AstheticButton(10,120,100,50,"      ||",new Background(new Color(150,255,150))) 
        {
            @Override
            protected void action() 
            {
                if(!control.getPaused())
                    control.pauseUnpause();
            }
        };
    }
    protected AstheticButton makeUnpauseButton()
    {
        return new AstheticButton(10,170,100,50,"       >",new Background(new Color(150,255,150))) 
        {
            @Override
            protected void action() 
            {
                if(control.getPaused())
                    control.pauseUnpause();
            }
        };
    }
    protected AstheticButton makeStepButton() {
        return new AstheticButton(10, 220, 100, 50, "     Step", new Background(new Color(150, 255, 150)))
        {
            @Override
            protected void action()
            {
                control.pauseUnpause();
                control.looiStep();
                control.pauseUnpause();
            }
        };
    }
    public void saveFile() throws FileNotFoundException, IOException
    {
        
        
        String name = generateFileName();
        FileOutputStream fos = new FileOutputStream(name);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(control.getMovingBlobs()); 
        System.out.println("Serialization size: " + control.getMovingBlobs().size());
        
        ArrayList<String> existingLines = new ArrayList<>();
        try
        {
            FileInputStream fis = new FileInputStream(getFileDocumentName());
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String nextLine;
            while((nextLine = br.readLine()) != null)
            {
                if(!nextLine.equals(""))
                {
                    existingLines.add(nextLine);
                }
            }
            br.close();
        }
        catch(FileNotFoundException e){}
        
        FileOutputStream fos2 = new FileOutputStream(getFileDocumentName());
        OutputStreamWriter osw = new OutputStreamWriter(fos2);
        BufferedWriter bw = new BufferedWriter(osw);
        for(String s : existingLines)
        {
            bw.write(s); 
            bw.newLine();
        }
        bw.write(name);
        
        bw.close();
        oos.close();
        
    }
    public String getFileDocumentName()
    {
        return fileDocumentName;
    }
    
    protected String generateFileName()
    {
        return "blob_save_" + Math.random();
    }
    protected void mousePressed(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
        {
            lassoStart = new Point(getInternalMouseX(),getInternalMouseY());
        }
        
        
    }
    protected  void mouseEngaged(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
        {
            if(lassoStart != null)
            {
                
                lassoEnd = new Point(getInternalMouseX(),getInternalMouseY());
                lassoSelection = new Point[] {lassoStart,new Point(lassoStart.getX(),lassoEnd.getY()),lassoEnd,new Point(lassoEnd.getX(),lassoStart.getY())};
                includeCertainRectangles();
            }
        }
    }
    protected void mouseReleased(MouseEvent e)
    {
        if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3)
        {
            if(e.getButton() == MouseEvent.BUTTON1)
            {
                selectOrDeselectCertainRectangles("add");
            }
            if(e.getButton() == MouseEvent.BUTTON3)
            {
                selectOrDeselectCertainRectangles("remove"); 
            }
            lassoStart = null;
            lassoEnd = null;
        }
    }
    public void includeCertainRectangles()
    {
        ArrayList<Rectangle> includedRectangles = findLassoedRectangles();
        ArrayList<Rectangle> excludedRectangles = new ArrayList<Rectangle>();
        for(Rectangle r : ibd.getRectangles())
        {
            excludedRectangles.add(r);
        }
        
        for(Rectangle r : includedRectangles)
        {
            if(selectedRectangles == null || !selectedRectangles.contains(r))
            {
                r.setColor(hoverColor);
                excludedRectangles.remove(r);
            }
            
        }
        for(Rectangle r : excludedRectangles)
        {
            if(selectedRectangles == null || !selectedRectangles.contains(r))
            {
                r.setColor(originalRectangleColor);
            }
            
        }
    }
    public void selectOrDeselectCertainRectangles(String action)
    {
        if(this.selectedRectangles == null)
        {
            this.selectedRectangles = new ArrayList<>();
        }
        ArrayList<Rectangle> lassoedRects = findLassoedRectangles();
        ArrayList<MovingBlob> lassoedBlobs = new ArrayList<>();
        for(Rectangle r : lassoedRects)
        {
            lassoedBlobs.add(r.getMovingBlob());
        }
        for(MovingBlob b : lassoedBlobs)
        {
            //this.selectedRectangles.add(r);
            if(action.equals("add"))
            {
                //r.setColor(selectedColor);
                //r.setThickness(newLineThickness);
                b.setAsPedestrian(true); 
            }
            if(action.equals("remove"))
            {
                //r.setColor(originalRectangleColor); 
                //r.setThickness(originalLineThickness);
                b.setAsPedestrian(false);
            }
        }
        /*for(Rectangle r : ibd.getRectangles())
        {
            r.setColor(originalRectangleColor); 
            r.setThickness(originalLineThickness);
            r.getMovingBlob().setAsPedestrian(false);
        }
        for(Rectangle r : this.selectedRectangles)
        {
            //System.out.println(r);
            r.setColor(selectedColor);
            r.setThickness(newLineThickness);
            r.getMovingBlob().setAsPedestrian(true); 
        }*/
        
    }
    public interface InputAction<E,J>
    {
        public void act(E e,J j);
    }
    public ArrayList<Rectangle> findLassoedRectangles()
    {
        ArrayList<Rectangle> selectedRectangles = new ArrayList<>();
        for(Rectangle r : ibd.getRectangles())
        {
            boolean isSelected = false;
            if(lassoSelection == null)
                {
                    return new ArrayList<Rectangle> ();
                }
            for(Point p : r.getScaledPoints())
            {
                
                if(p.insideConvexSpace(lassoSelection))
                {
                    
                    isSelected = true;
                    break;
                }
                
            }
            
            if(isSelected)
            {
                selectedRectangles.add(r);
            }
        }
        return selectedRectangles;
    }
    protected void looiPaint()
    {
        setColor(lassoColor);
        if(lassoStart == null || lassoEnd == null)
        {
            //System.out.println("fu");
            return;
        }
        //System.out.println("good");
            
        fillRect(lassoStart.getX(),lassoStart.getY(),lassoEnd.getX()-lassoStart.getX(),lassoEnd.getY()-lassoStart.getY());
    }
}
