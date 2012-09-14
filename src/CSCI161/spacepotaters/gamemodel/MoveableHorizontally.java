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

/**
 * A component that can be moved horizontally.
 */
public interface MoveableHorizontally {
    
    /**
     * Move the component left one "step".
     */
    void moveLeft();
    
    /**
     * Move the component right one "step".
     */
    void moveRight();
}
