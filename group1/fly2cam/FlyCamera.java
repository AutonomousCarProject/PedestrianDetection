// The actual class FlyCamera reads images from camera..   -- 2017 February 27

//   You need both FlyCapture2_C.dll and FlyCapture2.dll in your Java project folder

package fly2cam; // (same API as fly0cam)

public class FlyCamera {
  public static final int FrameRate_15 = 3, FrameRate_30 = 4;

  private int rose, // actual number of rows = FlyCap2.fc2Image.rows/2
      colz, // actual number of columns = FlyCap2.fc2Image.cols/2
      tile, // see FlyCapture2Defs.fc2BayerTileFormat
      errn; // returns an error number, see ErrorNumberText()
  private long stuff; // used for error reporting, or not at all

  public native boolean Connect(int frameRate); // required at start, sets rose,colz,tile
  public native boolean NextFrame(byte[] pixels); // fills pixels, false if can't
  public native void Finish(); // required at end to prevent memory leaks

  static { System.loadLibrary("FlyCamera"); }

  public static String ErrorNumberText(int errno) { // to explain errn in toString()
    if (errno == -20) return "ByteArray is not same size as received data";
    switch (errno) {
    case -1: return "No camera connected";
    case 0: return "No error";
    case 1: return "fc2CreateContext failed";
    case 2: return "fc2GetNumOfCameras failed";
    case 3: return "No cameras detected";
    case 4: return "fc2GetCameraFromIndex did not find first camera";
    case 5: return "fc2Connect failed to connect to first camera";
    case 6: return "fc2StartCapture failed";
    case 7: return "fc2CreateImage failed";
    case 8: return "No error";
    case 9: return "fc2RetrieveBuffer failed";
    case 10: return "rawImage.format = 0 (probably unset)";
    case 11: return "ByteArray to NextFrame is null";
    case 12: return "Connect failed or not called (context == null)";
    case 13: return "Something in context corrupted";
    case 14: return "ByteArray is way too short or too long";
    case 15: return "GetByteArrayElements failed (couldn't access bytes)";
    case 16: return "fc2RetrieveBuffer failed, possibly timeout";
    case 17: return "fc2GetImageData failed";
    case 18: return "No pixel data received";
    case 19: return "Unknown camera image size";
    case 20: return "No error";
    case 21: return "fc2StopCapture failed";
    case 22: return "fc2DestroyImage failed";
    case 23: return "Both fc2StopCapture and fc2DestroyImage failed";
    case 24: return "fc2CreateImage failed (RGB)";
    case 25: return "fc2ConvertImageTo (RGB) failed";
    case 26: return "fc2GetProperty failed";
    case 27: return "Unknown frame rate";
    case 28: return "fc2SetProperty failed";} //~switch
    return "fc2RetrieveBuffer probably returned some format other than Bayer8";
  } //~ErrorNumberText

  public int Dimz() {return (rose<<16)+colz;} // access to image size from camera
  public int PixTile() {return tile;} // image Bayer encoding, frex RG/GB = 1, GB/RG = 3
  public String toString() {return "fly2cam.FlyCamera " + ErrorNumberText(errn);}
  public boolean Live() {return tile>0;} // we have a live camera (fly2cam)

  public static void main(String[] args) { // to test the interface
    int tall = 0, wide = 0, pix = -1;
    byte[] buff;
    FlyCamera hello = new FlyCamera();
    if (hello.Connect(0)) tall = hello.Dimz();
    wide = tall&0xFFFF;
    tall = tall>>16;
    if (tall>0) if (wide>0) { // we got reasonable image size, get one image..
      buff = new byte[tall*wide*4];
      if (hello.NextFrame(buff)) // got an image, extract 1st pixel..
        pix = (((int)buff[0])<<24)|((((int)buff[1])&255)<<16)
            | ((((int)buff[wide+wide])&255)<<8) | (((int)buff[wide+wide+1])&255);
      else pix = 0; // no image came
      // set breakpoint here to look in the debugger, or else log pix
      wide = pix;} // otherwise Java complains that it's unused
    hello.Finish();
    pix = 0;} //~main

  public FlyCamera() {
    rose = 0;
    colz = 0;
    tile = 0;
    errn = 0;}} //~FlyCamera (fly2cam) (F2)
