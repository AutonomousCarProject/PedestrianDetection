/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalprj.autonomouscarfinalprogram2;

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
        mainWindow = new LooiWindow(60,true);
        mainWindow.fitWindow();
        hotkeys = new Hotkeys();
        control = new Control();
    }
    
}
