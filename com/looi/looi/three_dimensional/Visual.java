/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

import java.awt.Color;
import java.io.Serializable;

/**
 *
 * @author peter_000
 */
public interface Visual extends Serializable
{
    public void activate(Object...o);
    public void deactivate(Object...o);
    public void delete();
    public Point3D getCenter();
    public Color getColor();
}
