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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A block that represents a section of a barrier. Used to simulate damage.
 */
public class BarrierBlock implements Destroyable{

    private static int width;
    private static int height;
    private boolean isActive;
    private int[] location;
    
    /**
     * Default constructor.
     * @param sideLength the length (in pixels) of this block
     * @param location the coordinates of the top-left corner of this block
     */
    public BarrierBlock(int sideLength, int[] location){
        width = sideLength;
        height = sideLength;
        this.location = location;
        isActive = true;
    }
    
    /**
     * If block is inactive, draw the damage onto the block (i.e. paint that block's
     * section of the barrier the same as the background.
     * @param g the graphics context to paint to
     */
    public void drawDamage(Graphics g){
        if (!isActive){
            g.setColor(Color.BLACK);
            g.fillRect(location[0], location[1], width, height);
        }
    }
    
    /**
     * Set this block to inactive. Use to signal a destroyed block (i.e. damaged
     * barrier).
     */
    public void setInactive(){
        isActive = false;
    }
    
    /**
     * Return whether or not this block is active.
     * @return true if block is active
     */
    public boolean isActive(){
        return isActive;
    }
    
    /**
     * Return the x-coordinate of this block.
     * @return the x-coordinate
     */
    public int getXLocation(){
        return location[0];
    }
    
    /**
     * Return the y-coordinate of this book.
     * @return the y-coordinate
     */
    public int getYLocation(){
        return location[1];
    }
    
    /**
     * Return the width of this block. Width = Height if blocks are modeled as
     * squares.
     * @return the width of this block
     */
    public int getWidth(){
        return width;
    }
    
    /**
     * Return the height of this block. Width = Height if blocks are modeled as
     * squares.
     * @return the height of this block
     */
    public int getHeight(){
        return height;
    }

    /**
     * Return a rectangle representation of this block. Used to test for collisions
     * between game components.
     * @return a rectangle representation of this block
     */
    @Override
    public Rectangle getRectRep() {
        return new Rectangle(location[0], location[1], width, height);
    }
}
