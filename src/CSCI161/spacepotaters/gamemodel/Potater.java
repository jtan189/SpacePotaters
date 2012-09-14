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

import CSCI161.spacepotaters.presentation.Sprite;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A general potater enemy. The player should try to kill these using Gollum.
 */
public abstract class Potater implements MoveableHorizontally, Destroyable, Drawable{
    
    private int speed;
    private int[] location;
    private int pointValue;
    private Sprite sprite;
    
    /**
     * Default constructor.
     * @param speed the speed of the potater
     * @param location the coordinates of the potater (top-left corner)
     * @param pointValue the point value of the potater
     * @param sprite the sprite for the potater
     */
    public Potater(int speed, int[] location, int pointValue, Sprite sprite){
        this.speed = speed;
        this.location = location;
        this.pointValue = pointValue;
        this.sprite = sprite;
    }
    
    /**
     * Move the potater left.
     */
    @Override
    public abstract void moveLeft();
    
    /**
     * Move the potater right.
     */
    @Override
    public abstract void moveRight();
    
    /**
     * Draw the potater's sprite onto the graphics context.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics g){
        g.drawImage(sprite.getSpriteImage(), location[0], location[1], null);
    }
    
    /**
     * Return the rectangular representation of this potater. Used in collision
     * detecting.
     * @return the rectangular representation
     */
    @Override
    public Rectangle getRectRep(){
        return new Rectangle(location[0], location[1], sprite.getWidth(), sprite.getHeight());
    }
    
    /**
     * Return the point value of this potater.
     * @return the point value
     */
    public int getPointValue(){
        return pointValue;
    }
    
    /**
     * Return the sprite for this potater.
     * @return 
     */
    public Sprite getSprite(){
        return sprite;
    }
    
    /**
     * Set the sprite for this potater.
     * @param sprite the sprite to set to
     */
    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }
    
    /**
     * Return the coordinates of this potater (top-left corner).
     * @return the coordinates
     */
    public int[] getLocation(){
        return location;
    }
    
    /**
     * Set the x-coordinate of this potater.
     * @param x the x-coordinate to set to
     */
    public void setXLocation(int x){
        location[0] = x;
    }
    
    /**
     * Set the y-coordinate of this potater.
     * @param y the y-coordinate to set to
     */
    public void setYLocation(int y){
        location[1] = y;
    }
    
    /**
     * Return the x-coordinate of this potater (top-left corner).
     * @return the x-coordinate
     */
    public int getXLocation(){
        return location[0];
    }
    
    /**
     * Return the y-coordinate of this potater (top-left corner).
     * @return the y-coordinate
     */
    public int getYLocation(){
        return location[1];
    }
    
    /**
     * Return the speed of this potater.
     * @return the speed
     */
    public int getSpeed(){
        return speed;
    }
}
