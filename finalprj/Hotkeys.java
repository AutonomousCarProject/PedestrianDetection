/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;

import com.looi.looi.LooiObject;
import com.looi.looi.MiscellaneousMethods;
import java.awt.event.KeyEvent;

/**
 *
 * @author peter_000
 */
public class Hotkeys extends LooiObject
{
    public Hotkeys()
    {
        
    }
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            MiscellaneousMethods.exitSystem();
        }
    }
}
