/*
 * CSCI 161 (Section 1) - Fall 2011
 * Group Project - Space Potaters
 * Team Name : Team Ramrod
 * Team Members:
 *      Anderson, Travis
 *      Birrenkott, Chris
 *      Tan, Joshua
 *      Wako, Shambel
 */

package CSCI161.spacepotaters.gamemodel;

import java.awt.Rectangle;

/**
 * A destroyable game component. Classes implementing this are involved in 
 * collision testing.
 */
public interface Destroyable {

    /**
     * Return a rectangle representation of this component.
     * @return a rectangle representation
     */
    Rectangle getRectRep();
    
}
