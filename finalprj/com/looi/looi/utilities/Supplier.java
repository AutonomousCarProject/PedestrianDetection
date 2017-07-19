/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.utilities;

import java.io.Serializable;

/**
 *
 * @author peter_000
 */
public interface Supplier<E> extends Serializable
{
    public E get();
}
