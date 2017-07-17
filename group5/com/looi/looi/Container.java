/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi;

/**
 *
 * @author peter_000
 */
public class Container extends LooiObject
{
    public Container(LooiWindow l)
    {
        super(l,false);
    }
    public Container()
    {
        super(LooiWindow.getMainWindow(),false);
    }
    public void activate(Object...activationInfo)
    {
        activateContainedObjs(activationInfo);
    }
    public void deactivate(Object...activationInfo)
    {
        deactivateSelf(activationInfo);
    }
}
