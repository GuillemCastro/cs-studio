/**
 * Copyright (C) 2012-14 graphene developers. See COPYRIGHT.TXT
 * All rights reserved. Use is subject to license terms. See LICENSE.TXT
 */
package org.epics.graphene;

/**
 * A scheme that will associate a RBG color to any data value.
 * This is a transformation from <b>value</b> to <b>color</b>.
 * 
 * @author carcassi
 */
public interface ValueColorScheme {
    
    public ValueColorSchemeInstance createInstance(Range range);
    
    /**
     * Calculate the color for the value according to the ranges and puts it
     * into the colors buffer.
     *
     * @param value the value to color
     * @return the RGB color
     */
    //public int colorFor(double value);
    
    //public void setColors();
    
    //public int getColor(double value);
}
