/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;

public class Blob
{
    public int width, height;
    public int centerX, centerY;
    public IPixel color;

    public Blob(int width, int height, int centerX, int centerY, IPixel color)
    {
        this.width = width;
        this.height = height;
        this.centerX = centerX;
        this.centerY = centerY;
        this.color = color;
    }
}