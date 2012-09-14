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

import java.awt.Graphics;

/**
 * A drawable game component. Any object that is drawn to the screen should
 * implement this interface.
 */
public interface Drawable {

    /**
     * Draw the component onto the graphics context.
     * @param g the graphics context to draw to
     */
    void draw(Graphics g);
    
}