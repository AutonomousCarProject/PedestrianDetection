package fly2cam;

import group1.IImage;
import group1.IPixel;
import group1.Image;

public class ToggleExposure implements IAutoExposure
{
    private boolean shouldBoost = false;
    private FlyCamera flyCam;
    
    public ToggleExposure(IImage image)
    {
        if(image instanceof Image)
        {
            flyCam = ((Image) image).flyCam;
        }
        else
        {
            flyCam = null;
        }
        
    }
    
    @Override
    public void autoAdjust(IPixel[][] pixels)
    {
        if(flyCam == null) return;
        
        if(shouldBoost)
        {
            flyCam.SetShutter(75);
        }
        else
        {
            flyCam.SetShutter(25);
        }
        
        shouldBoost = !shouldBoost;
    }
}
