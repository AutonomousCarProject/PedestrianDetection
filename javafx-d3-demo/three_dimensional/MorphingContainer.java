/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

import com.looi.looi.LooiObject;
import com.looi.looi.utilities.Supplier;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author peter_000
 */
public class MorphingContainer extends LooiObject implements Visual
{
    private ArrayList<Visual> closeVisual = new ArrayList<>();
    private ArrayList<Visual> farVisual = new ArrayList<>();
    private Supplier<Double> activationDistance;
    private Supplier<View> view;
    private Point3D center = new Point3D(0,0,0);
    public MorphingContainer(Supplier<Double> activationDistance, Supplier<View> view)
    {
        this.activationDistance = activationDistance;
        this.view = view;
    }
    public Supplier<View> getView()
    {
        return view;
    }
    public void setView(Supplier<View> view)
    {
        this.view = view;
    }
    public Supplier<Double> getActivationDistance()
    {
        return activationDistance;
    }
    public void setActivationDistance(Supplier<Double> activationDistance)
    {
        this.activationDistance = activationDistance;
    }
    public ArrayList<Visual> getCloseVisual()
    {
        return closeVisual;
    }
    public ArrayList<Visual> getFarVisual()
    {
        return farVisual;
    }
    protected void looiStep()
    {
        
        center = findCenter();
        if(center.get3DDistance(view.get()) <= activationDistance.get())
        {
            deactivateFar();
            activateClose();
        }
        else
        {
            deactivateClose();
            activateFar();
        }
    }
    protected Point3D findCenter()
    {
        double sumX = 0;
        double sumY = 0;
        double sumZ = 0;
        for(Visual p : closeVisual)
        {
            sumX += p.getCenter().getX();
            sumY += p.getCenter().getY();
            sumZ += p.getCenter().getZ();
        }
        for(Visual p : farVisual)
        {
            sumX += p.getCenter().getX();
            sumY += p.getCenter().getY();
            sumZ += p.getCenter().getZ();
        }
        int totalPolygons = closeVisual.size() + farVisual.size();
        double avgX = sumX / totalPolygons;
        double avgY = sumY / totalPolygons;
        double avgZ = sumZ / totalPolygons;
        return new Point3D(avgX,avgY,avgZ);
    }
    protected void uponDeactivation()
    {
        deactivateClose();
        deactivateFar();
    }
    protected void activateClose()
    {
        for(Visual v : closeVisual)
        {
            v.activate(this);
        }
    }
    protected void activateFar()
    {
        for(Visual v : farVisual)
        {
            v.activate(this);
        }
    }
    protected void deactivateClose()
    {
        for(Visual v : closeVisual)
        {
            v.deactivate(this);
        }
    }
    protected void deactivateFar()
    {
        for(Visual v : farVisual)
        {
            v.deactivate(this);
        }
    }
    public void delete()
    {
        super.delete();
        for(Visual v : closeVisual)
        {
            v.delete();
        }
        for(Visual v : farVisual)
        {
            v.delete();
        }
    }
    public void remove(Visual v)
    {
        closeVisual.remove(v);
        farVisual.remove(v);
    }

    @Override
    public Point3D getCenter() 
    {
        return center;
    }
    public Color getAverageColor()
    {
        int totalR = 0;
        int totalG = 0;
        int totalB = 0;
        int totalQuadrilaterals = 0;
        for(Visual q : closeVisual)
            {
                totalR += q.getColor().getRed();
                totalG += q.getColor().getGreen();
                totalB += q.getColor().getBlue();
                totalQuadrilaterals++;
            }
        for(Visual q : farVisual)
            {
                totalR += q.getColor().getRed();
                totalG += q.getColor().getGreen();
                totalB += q.getColor().getBlue();
                totalQuadrilaterals++;
            }
        if(totalQuadrilaterals == 0)
            {
                return Color.BLACK;
            }
        int avgR = totalR / totalQuadrilaterals;
        int avgG = totalG / totalQuadrilaterals;
        int avgB = totalB / totalQuadrilaterals;
        return new Color(avgR,avgG,avgB);
    }
    public Color getColor()
    {
        return getAverageColor();
    }

}
