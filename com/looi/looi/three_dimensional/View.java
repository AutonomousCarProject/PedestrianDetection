/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;

/**
 *
 * @author peter_000
 */
public class View extends Point3D
{
    public static final double DEFAULT_SCALE_DEPTH = 750;;
    private double hr=90,vr=0,or=0;//hr = horizontal rotation, vr = vertical rotation, or = orientation rotation
    private double scaleDepth;//the depth (z value) where the coordinates are to-scale
    private ReadOnlyView snapshot;
    public View(double scaleDepth)
    {
        super(0,0,0);
        this.scaleDepth = scaleDepth;
        updateSnapshot();
    }
    public View()
    {
        this(DEFAULT_SCALE_DEPTH);
    }
    private View(String s)
    {
        super(0,0,0);
    }
    public void move(double xInc, double yInc, double zInc, double hrInc, double vrInc, double orInc)
    {
        super.move(xInc,yInc,zInc);
        hr += hrInc;
        vr += vrInc;
        or += orInc;
        updateSnapshot();
    }
    public void set(double x, double y, double z, double hr, double vr, double or)
    {
        this.hr = hr;
        this.vr = vr;
        this.or = or;
        super.setXYZ(x,y,z);
        updateSnapshot();
    }
    protected void updateSnapshot()
    {
        snapshot = new ReadOnlyView(getX(),getY(),getZ(),hr,vr,or,scaleDepth);
    }
    public void setScaleDepth(double d)
    {
        scaleDepth = d;
    }
    public double getHR(){return hr;}
    public double getVR(){return vr;}
    public double getOR(){return or;}
    public double getScaleDepth(){return scaleDepth;}
    public ReadOnlyView getSnapshot(){return snapshot;}
    
    public class ReadOnlyView extends View
    {
        public ReadOnlyView(double x, double y, double z, double hr, double vr, double or, double scaleDepth)
        {
            super("I am read only view!");
            super.set(x,y,z,hr,vr,or);
            super.setScaleDepth(scaleDepth); 
        }
        public void move(double xInc, double yInc, double zInc, double hrInc, double vrInc, double orInc){}
        public void set(double x, double y, double z, double hr, double vr, double or){}
        public void setScaleDepth(double d){}
        public void move(double xInc, double yInc, double zInc){}
        public void setX(double x){}
        public void setY(double y){}
        public void setZ(double z){}
        public void setXYZ(double x, double y, double z){}
        public void setXYZ(double... coordinates){}
        public void setXYZ(Point3D p){}
        protected void updateSnapshot(){}
    }
    
}
