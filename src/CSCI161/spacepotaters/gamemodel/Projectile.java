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
 * A projectile that can damage game components. Analogous to a bullet/missile/etc.
 */
public abstract class Projectile implements Drawable, Destroyable{
    
    private int speed;
    private int[] location;
    private Sprite sprite;
    private Fireable shooter;
    
    /**
     * Constructor specifying a specific speed.
     * @param speed the speed of the projectile
     * @param location the coordinates of the projectile (top-left corner)
     * @param sprite the sprite of the projectile
     * @param shooter the component that shot the projectile
     */
    public Projectile(int speed, int[] location, Sprite sprite, Fireable shooter){
        this.speed = speed;
        this.location = location;
        this.sprite = sprite;
        this.shooter = shooter;
    }
    
    /**
     * Constructor.
     * @param location the coordinates of the projectile
     * @param sprite the sprite of the projectile
     * @param shooter the component that shot the projectile
     */
    public Projectile(int[] location, Sprite sprite, Fireable shooter){
        this(GameModel.DEFAULT_BULLET_SPEED, location, sprite, shooter);
    }
    
    /**
     * Draw the sprite for the projectile onto the graphics context.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics g){
        g.drawImage(sprite.getSpriteImage(), location[0], location[1], null);
    }
    
    /**
     * Return the rectangular representation for the projectile. Used in collision
     * detecting.
     * @return the rectangular representation
     */
    @Override
    public Rectangle getRectRep(){
        return new Rectangle(location[0], location[1], sprite.getWidth(), sprite.getHeight());
    }
    
    /**
     * Return the component that shot this projectile.
     * @return the component that shot this projectile
     */
    public Fireable getShooter(){
        return shooter;
    }
    
    /**
     * Return the sprite associated with this projectile.
     * @return the projectile's sprite
     */
    public Sprite getSprite(){
        return sprite;
    }
    
    /**
     * Return the coordinates of the projectile.
     * @return the coordinates
     */
    public int[] getLocation(){
        return location;
    }
    
    /**
     * Set the y-coordinate of the projectile.
     * @param newYLocation the y-coordinate to set to
     */
    public void setYLocation(int newYLocation){
        location[1] = newYLocation;
    }
    
    /**
     * Return the x-coordinate of the projectile.
     * @return the x-coordinate
     */
    public int getXLocation(){
        return location[0];
    }
    
    /**
     * Return the y-coordinate of the projectile.
     * @return the y-coordinate
     */
    public int getYLocation(){
        return location[1];
    }
    
    /**
     * Return the speed of the projectile.
     * @return the projectile's speed
     */
    public int getSpeed(){
        return speed;
    }
    
}
