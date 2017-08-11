/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiWindow;

/**
 *
 * @author peter_000
 */
public class AutonomousCarFinalProgram2 {

    private static Hotkeys hotkeys;
    private static LooiWindow mainWindow;
    private static Control control;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        mainWindow = new LooiWindow(12,true);
        mainWindow.fitWindow();
        
        // parameter in this constructor is the delay (in milliseconds) between frames
        control = new Control("src/group1/fly0cam/FlyCapped6.By8", true);
        hotkeys = new Hotkeys(control);
    }
    
}
