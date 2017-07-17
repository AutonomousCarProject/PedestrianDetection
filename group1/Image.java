

import java.io.*;
import fly2cam.FlyCamera;

public FlyCamera flyCam = new FlyCamera();
public byte[] camBytes = null;



void readCam(){

	flyCam.NextFrame(camBytes);

}

