/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;

import java.util.List;

public interface IImageDrawing
{
    void draw(IImage image, List<MovingBlob> iBlobs);
}