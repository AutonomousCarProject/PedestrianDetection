package global;

import fly2cam.FlyCamera;

public class ResetCamera
{
    public static void main(String[] args)
    {
        FlyCamera cam = new FlyCamera();
        cam.Connect(FlyCamera.FrameRate_30, 0, 0, 0);
        System.out.println(Long.toBinaryString(cam.ReadRegister(0x1800)));
        cam.SafeWriteRegister(0x0, 0x1, "Failed to write to register");
        while(cam.SafeReadRegister(0x0, "Failed to read register") != 0);
        System.out.println("Reset Camera Settings");
    }
}
