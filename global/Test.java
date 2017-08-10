package global;

import fly2cam.FlyCamera;

public class Test
{
    public static void main(String[] args)
    {
        FlyCamera cam = new FlyCamera();
        cam.Connect(FlyCamera.FrameRate_30, 0, 0, 0);
        
        System.out.println(cam.Dimz() >> 16);
        System.out.println(cam.Dimz() & 0xFFFF);

        System.out.printf("%d %d%n", cam.debug, cam.debug2);
        
        cam.Finish();
    }
}
