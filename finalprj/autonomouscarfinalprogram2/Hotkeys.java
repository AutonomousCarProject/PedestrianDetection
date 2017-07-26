/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram2;

import com.looi.looi.LooiObject;
import com.looi.looi.MiscellaneousMethods;
import java.awt.event.KeyEvent;

/**
 *
 * @author peter_000
 */
public class Hotkeys extends LooiObject
{
	public Control control;
    public Hotkeys(Control c)
    {
        control = c;
    }
    public void keyPressed(KeyEvent e)
    {
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            MiscellaneousMethods.exitSystem();
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE)
        {
        	System.out.println("yep");
        	control.pauseUnpause();
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
        	control.incrementCurrentFrame(-1);
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
        	control.incrementCurrentFrame(1);
        }
    } 
}
