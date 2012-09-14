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
import CSCI161.spacepotaters.presentation.SpriteLibrary;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * The defender fighting off the oncoming potaters. Gollum is the user-controlled
 * component and shooter of the game. He can fire "the one ring"-projectiles,
 * move left, and move right.
 * 
 * Methods are synchronized since they might be updated by the event thread of
 * the game while animator is examining/drawing (source: KGPJ)
 */
public class Gollum implements MoveableHorizontally, Fireable, Destroyable, Drawable{
    
    private static int speed = 5; // "step size"
    private GameModel game;
    
    private int[] location;
    private Sprite sprite;
    
    /**
     * Constructor specifying location and sprite of Gollum.
     * @param location the coordinates of Gollum (top-left corner)
     * @param sprite the sprite to be associated with Gollum
     * @param game the game Gollum belongs to
     */
    public Gollum(int[] location, Sprite sprite, GameModel game){
        this.location = location;
        this.sprite = sprite;
        this.game = game;
    }
    
    /**
     * Constructor.
     * @param game the game Gollum belongs to
     */
    public Gollum(GameModel game){
        int bottomSpacer = 50; // space (in pixels) between Gollum and bottom of the screen
        Sprite gollSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollum.png");
        
        int smeagXLocation = ((game.getRightBoundary() - game.getLeftBoundary()) - gollSprite.getWidth()) / 2;
        int smeagYLocation = game.getWindowHeight() - bottomSpacer - gollSprite.getHeight();
        int[] smeagLocation = {smeagXLocation, smeagYLocation};
        
        location = smeagLocation;
        sprite = gollSprite;
        this.game = game;
    }
    
    /**
     * Reset the position of Gollum to be underneath the center of a middle barrier.
     */
    public synchronized void resetPosition() {
        Barrier middle = game.getBarriers().get(GameModel.NUM_OF_BARRIERS/2);
        int xLoc = middle.getXLocation() + ((middle.getSprite().getWidth() - sprite.getWidth())/ 2);
        location[0] = xLoc;
    }
    
    /**
     * Move Gollum left.
     */
    @Override
    public synchronized void moveLeft(){
        if ((location[0] - speed )> game.getLeftBoundary()){
            location[0] -= speed;
        }
    }
    
    /**
     * Move Gollum right.
     */
    @Override
    public synchronized void moveRight(){
        if ((location[0] + sprite.getWidth()) < game.getRightBoundary()){
            location[0] += speed;
        }
    }
    
    /**
     * Shoot a projectile with Gollum.
     * @return the projectile shot by Gollum
     */
    @Override
    public synchronized Projectile shoot(){
        Sprite shotSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollumBullet.png");
        int shotHeight = shotSprite.getHeight();
        int shotWidth = shotSprite.getWidth();
        int[] projLocation = {location[0] + ((sprite.getWidth() - shotWidth)/2), location[1] - shotHeight};
        return new GollumProjectile(projLocation, this);
    }
    
    /**
     * Return a rectangular representation of Gollum, to be used in collision
     * detecting.
     * @return a rectangular representation of Gollum
     */
    @Override
    public synchronized Rectangle getRectRep(){
        return new Rectangle(location[0], location[1], sprite.getWidth(), sprite.getHeight());
    }
    
    /**
     * Draw Gollum's sprite on the graphics context.
     * @param g the graphics context
     */
    @Override
    public synchronized void draw(Graphics g){
        g.drawImage(sprite.getSpriteImage(), location[0], location[1], null);
    }
    
    /**
     * Return the coordinates of Gollum's top-left corner.
     * @return Gollum's coordinates
     */
    public synchronized int[] getLocation(){
        return location;
    }
    
    /**
     * Return the y-coordinate of Gollum.
     * @return the y-coordinate
     */
    public synchronized int getYLocation(){
        return location[1];
    }
    
    /**
     * Set the x-coordinate of Gollum.
     * @param xLoc the x-coordinate to set to
     */
    public synchronized void setXLocation(int xLoc){
        location[0] = xLoc;
    }
    
    /**
     * Return the sprite associated with Gollum.
     * @return Gollum's sprite
     */
    public Sprite getSprite(){
        return sprite;
    }
}
