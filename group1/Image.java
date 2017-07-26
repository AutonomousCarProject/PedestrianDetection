package group1;

import fly2cam.FlyCamera;

//Defines image as an 2d array of pixels
public class Image implements IImage
{

    public int height;
    public int width;

    private final int frameRate = 3;
    public FlyCamera flyCam = new FlyCamera(); //FIXME
    private final float greyRatio = 1.1f;
    private final int blackRange = 100;
    private final int whiteRange = 200;

    private final int neighborhood = 4; //2^n = actual neighborhood size

    private int tile;
    private int autoCount = 0;
    private int autoFreq = 15;
    private int pos = 0;

    // 307200
    // private byte[] camBytes = new byte[2457636];
    private byte[] camBytes;
    private static IPixel[][] image;

    public Image(int exposure, int shutter, int gain)
    {
        flyCam.Connect(frameRate, exposure, shutter, gain);

        int res = flyCam.Dimz();
        height = res >> 16;
        width = res & 0x0000FFFF;

        camBytes = new byte[height * width * 4];
        image = new LocalPixel[height][width];

        tile = flyCam.PixTile();
        System.out.println("tile: "+tile+" width: "+width+" height: "+height);
    }

    @Override
    public void setAutoFreq(int autoFreq){  //How many frames are loaded before the calibrate is called (-1 never calls it)
        this.autoFreq = autoFreq;

    }

    @Override
    public IPixel[][] getImage()
    {
        return image;
    }

    // gets a single frame
    @Override
    public void readCam()
    {
        autoCount++;
        //System.out.println("TILE: " + flyCam.PixTile());
        // System.out.println(flyCam.errn);
        flyCam.NextFrame(camBytes);
        // System.out.println(flyCam.errn);


        if(autoCount > autoFreq && autoFreq > -1) {
            localAutoConvert();
            autoCount = 0;
        }
        else{
            byteConvert();
        }
    }
    /*
        private void autoColor(){
            readCam();
            int average = 0;
            int variation = 0;
            final int divisor = (width*height);

            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++) {

                    IPixel temp = image[i][j];
                    average += temp.getRed() + temp.getGreen()+ temp.getBlue();
                }

            }
            average = average / (divisor*3);

            for (int i = 0; i < height; i++)
            {
                for (int j = 0; j < width; j++) {

                    IPixel temp = image[i][j];
                    int rVar = temp.getRed()-average;
                    if(rVar < 0){
                        rVar = -rVar;
                    }

                    int gVar = temp.getGreen()-average;
                    if(gVar < 0){
                        gVar = -rVar;
                    }

                    int bVar = temp.getBlue()-average;
                    if(bVar < 0) {
                        bVar = -bVar;
                    }

                    variation += rVar + gVar + bVar;
                }

            }
            average = average * 3;
            variation = variation / divisor;
            Pixel.greyMargin = (int)(variation * greyRatio);
            Pixel.blackMargin = average - blackRange;
            Pixel.whiteMargin = average + whiteRange;
            System.out.println("Variation: "+variation+" greyRatio: "+greyRatio);
            System.out.println("greyMargin: " + Pixel.greyMargin + " blackMargin: " + Pixel.blackMargin + " whiteMargin: " + Pixel.whiteMargin);
        }
    */
    public void finish()
    {
        flyCam.Finish();
    }

  /*  public int getFrameNo(){
        return flyCam.frameNo;
    }
    */

    private void byteConvert()
    {


        pos = 0;
        if(tile == 1){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new LocalPixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255),
                            (short) (camBytes[pos + 1 + width * 2] & 255));
                    pos += 2;

                }

                pos += width * 2;

            }
        }
        else if(tile == 3){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new LocalPixel((short) (camBytes[pos +  width * 2] & 255) , (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    pos += 2;

                }

                pos += width * 2;

            }
        }

    }

    private void autoConvert()
    {
        int average = 0;    //0-255
        int average2;   //0-765
        int variation = 0;
        final int divisor = (width*height);

        int pos = 0;
        if(tile == 1){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255),
                            (short) (camBytes[pos + 1 + width * 2] & 255));
                    pos += 2;

                    average += image[i][j].getRed() + image[i][j].getGreen()+ image[i][j].getBlue();

                }

                pos += width * 2;

            }
        }
        else if(tile == 3){
            for (int i = 0; i < height; i++)
            {

                for (int j = 0; j < width; j++)
                {

                    image[i][j] = new Pixel((short) (camBytes[pos +  width * 2] & 255) , (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    pos += 2;

                    average += image[i][j].getRed() + image[i][j].getGreen()+ image[i][j].getBlue();

                }

                pos += width * 2;

            }
        }

        average2 = average / divisor;
        average = average2/3;

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++) {

                IPixel temp = image[i][j];
                int rVar = temp.getRed()-average;
                if(rVar < 0){
                    rVar = -rVar;
                }

                int gVar = temp.getGreen()-average;
                if(gVar < 0){
                    gVar = -rVar;
                }

                int bVar = temp.getBlue()-average;
                if(bVar < 0) {
                    bVar = -bVar;
                }

                variation += rVar + gVar + bVar;
            }

        }

        variation = variation / divisor;
        Pixel.greyMargin = (int)(variation * greyRatio);
        Pixel.blackMargin = average2 - blackRange;
        Pixel.whiteMargin = average2 + whiteRange;
        System.out.println("Variation: "+variation+" greyRatio: "+greyRatio);
        System.out.println("greyMargin: " + Pixel.greyMargin + " blackMargin: " + Pixel.blackMargin + " whiteMargin: " + Pixel.whiteMargin);

    }

    private void localAutoConvert(){
        pos = 0;
        int neigh = 1 << neighborhood;  //true size of neighborhood
        for(int i = 0; i < height; i+= neigh){
            for(int j = 0; j < width; j += neigh){
                runAuto(i,j);
            }
        }
    }

    private void runAuto(int x, int y){
        int neigh = 1 << neighborhood;  //true size of neighborhood
        int average = 0;    //0-255
        int average2;   //0-765
        int variation = 0;
        final int divisor = (neighborhood << 1);    // number of bits to shift

        pos = ((x*width << 1) + y) << 1;

        if(tile == 1){  //one format type
            for (int i = 0; i < neigh; i++)
            {
                for (int j = 0; j < neigh; j++)
                {
                    int xCor = i+x;
                    int yCor = j+y;

                    IPixel temp = new LocalPixel((short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255), (short) (camBytes[pos + 1 + width * 2] & 255));
                    image[xCor][yCor] = temp;

                    pos += 2;

                    average += temp.getRed() + temp.getGreen()+ temp.getBlue();

                }

                pos += (width << 2) - (neigh << 1);

            }
        }
        else if(tile == 3){ //other format type
            for (int i = 0; i < neigh; i++)
            {
                for (int j = 0; j < neigh; j++)
                {
                    int xCor = i+x;
                    int yCor = j+y;

                    IPixel temp = new LocalPixel((short) (camBytes[pos +  width * 2] & 255) , (short) (camBytes[pos] & 255), (short) (camBytes[pos + 1] & 255));
                    image[xCor][yCor] = temp;

                    pos += 2;

                    average += temp.getRed() + temp.getGreen()+ temp.getBlue();

                }

                pos += (width << 2) - (neigh << 1);

            }
        }

        average2 = average >> divisor;
        average = average2/3;

        for (int i = 0; i < neigh; i++)
        {
            for (int j = 0; j < neigh; j++) {

                IPixel temp = image[i+x][j+y];
                int rVar = temp.getRed()-average;
                if(rVar < 0){
                    rVar = -rVar;
                }

                int gVar = temp.getGreen()-average;
                if(gVar < 0){
                    gVar = -rVar;
                }

                int bVar = temp.getBlue()-average;
                if(bVar < 0) {
                    bVar = -bVar;
                }

                variation += rVar + gVar + bVar;
            }

        }

        variation = variation >> divisor;

        for(int i = 0; i < neigh; i++){
            for(int j = 0; j < neigh; j++){
                IPixel temp = image[i+x][j+y];
                temp.setMargins((int)(variation * greyRatio),average2 - blackRange, average2 + whiteRange );
            }
        }

        //System.out.println("Variation: "+variation+" average: "+average2);
    }

}
