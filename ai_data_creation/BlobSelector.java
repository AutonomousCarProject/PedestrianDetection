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
    private Color hoverColor;
    private Color selectedColor;
    private Color lassoColor;
    private Color originalRectangleColor;
    private int originalLineThickness;
    private int newLineThickness;
    private AstheticButton fileWriter;
    private AstheticButton activateWindow;
    private AstheticButton play;
    private AstheticButton pause;
    private Window window;
    private String fileDocumentName;
    private Control control;
    
    public BlobSelector(IImageBoxDrawer ibd, Color selectedColor, Color hoverColor, Color lassoColor, Color originalRectangleColor, int originalLineThickness, int newLineThickness, String fileDocumentName, Control control)
    {
        this.control = control;
        this.ibd = ibd;
        setLayer(-9999);
        this.hoverColor = hoverColor;
        this.selectedColor = selectedColor;
        this.lassoColor = lassoColor;
        this.originalRectangleColor = originalRectangleColor;
        this.originalLineThickness = originalLineThickness;
        this.newLineThickness = newLineThickness;
        activateWindow = makeActivateWindowButton();
        window = makeWindow();
        
        fileWriter = makeFileWriterButton();
        play = makeUnpauseButton();
        pause = makePauseButton();
        
        
        window.add(fileWriter);
        window.add(play);
        window.add(pause);
        this.fileDocumentName = fileDocumentName;
    }
    public BlobSelector(IImageBoxDrawer ibd, Color originalRectangleColor, int originalLineThickness, String fileDocumentName, Control control)
    {
        this(ibd,DEFAULT_SELECTION_COLOR,DEFAULT_HOVER_COLOR,DEFAULT_LASSO_COLOR, originalRectangleColor,originalLineThickness,(int)(originalLineThickness * DEFAULT_NEW_LINE_THICKNESS_RATIO),fileDocumentName,control);
    }
    public BlobSelector(IImageBoxDrawer ibd, String fileDocumentName, Control control)
    {
        this(ibd,DEFAULT_SELECTION_COLOR,DEFAULT_HOVER_COLOR,DEFAULT_LASSO_COLOR, ibd.getRectangleColor(),ibd.getLineThickness(),(int)(ibd.getLineThickness() * DEFAULT_NEW_LINE_THICKNESS_RATIO),fileDocumentName,control);
    }
    public BlobSelector(IImageBoxDrawer ibd, Control control)
    {
        this(ibd,DEFAULT_FILE_DOCUMENT_NAME,control);
    }
    protected Window makeWindow()
    {
        Window w = new Window(100,100,600,600,Background.WHITE_BACKGROUND);
        DecorationBar d;
        w.add(d = w.new DecorationBar(Background.LIGHT_GRAY_BACKGROUND)); 
        d.add(w.new ExitButton()); 
        return w;
    }
    protected AstheticButton makeActivateWindowButton()
    {
        return new AstheticButton(0,0,100,50,"Open Window",new Color(150,255,150)) {
            @Override
            protected void action() 
            {
                window.activate();
                
            }
        };
    }
    protected AstheticButton makeFileWriterButton()
    {
        return new AstheticButton(10,70,100,50,"Save File",new Color(150,255,150)) 
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
        return new AstheticButton(10,120,100,50,"      ||",new Color(150,255,150)) 
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
        return new AstheticButton(10,170,100,50,"       >",new Color(150,255,150)) 
        {
            @Override
            protected void action() 
            {
                if(control.getPaused())
                    control.pauseUnpause();
            }
        };
    }
    public void saveFile() throws FileNotFoundException, IOException
    {
        
        
        String name = generateFileName();
        FileOutputStream fos = new FileOutputStream(name);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(control.getUnifiedBlobs()); 
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
                selectOrDeselectCertainRectangles((l,r) -> {l.add(r);});
            }
            if(e.getButton() == MouseEvent.BUTTON3)
            {
                selectOrDeselectCertainRectangles((l,r) -> {l.remove(r);});
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
    public void selectOrDeselectCertainRectangles(InputAction<List<Rectangle>, Rectangle> action)
    {
        if(this.selectedRectangles == null)
        {
            this.selectedRectangles = new ArrayList<>();
        }
        ArrayList<Rectangle> selectedRectangles = findLassoedRectangles();
        for(Rectangle r : selectedRectangles)
        {
            //this.selectedRectangles.add(r);
            action.act(this.selectedRectangles,r);
        }
        for(Rectangle r : ibd.getRectangles())
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
        }
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
