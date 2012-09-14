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

import CSCI161.spacepotaters.presentation.SpriteLibrary;

/**
 * A bonus potater that randomly floats across the top of the game screen. Worth
 * more points that an ordinary potater.
 */
public class BonusPotater extends Potater{
    
    private static int BONUS_SPEED = 2; // "step size"
    private static int BONUS_POINTS = 1000;
    
    private boolean movingLeft; // true if currently moving left

    /**
     * Default constructor.
     * @param location the coordinates of this bonus potater (top-left corner)
     * @param movingLeft true if current moving in the left direction
     */
    public BonusPotater(int[] location, boolean movingLeft){
        super(BONUS_SPEED, location, BONUS_POINTS, SpriteLibrary.getSpriteLibrary().getSprite("sprites/bonus.png"));
        this.movingLeft = movingLeft;
    }
    
    /**
     * Move one step in whatever direction the bonus potater is already moving.
     */
    public void move(){
        if (movingLeft){
            moveLeft();
        } else {
            moveRight();
        }
    }
    
    /**
     * Return whether or not the potater is moving left.
     * @return true if currently moving left
     */
    public boolean isMovingLeft(){
        return movingLeft;
    }
    
    /**
     * Move left one step.
     */
    @Override
    public void moveLeft() {
        setXLocation(getXLocation() - BONUS_SPEED);
    }

    /**
     * Move right one step.
     */
    @Override
    public void moveRight() {
        setXLocation(getXLocation() + BONUS_SPEED);
    }
    
}