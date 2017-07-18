/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autonomouscarfinalprogram;

import java.util.List;

/**
 *
 * @author peter_000
 */
public interface IBlobDetection
{
    List<Blob> getBlobs(IImage image);
}