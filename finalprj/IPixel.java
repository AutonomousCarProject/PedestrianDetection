/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;

/**
 *
 * @author peter_000
 */
public interface IPixel
{
    // All values between 0 and 255
	
    // YUV
    short getLuma();

    short getSaturation();

    short getColor();

    // RGB
    short getRed();

    short getGreen();

    short getBlue();
}