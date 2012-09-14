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
 * A projectile shot by Gollum.
 */
public class GollumProjectile extends Projectile implements MoveableUp{
    
    /**
     * Constructor specifying specific speed.
     * @param speed the speed of the projectile
     * @param location the location of the projectile
     * @param shooter the component that shot this projectile
     */
    public GollumProjectile(int speed, int[] location, Fireable shooter){
        super(speed, location, SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollumBullet.png"), shooter);
    }
    
    /**
     * Constructor.
     * @param location the location of the projectile
     * @param shooter the component that shot this projectile
     */
    public GollumProjectile(int[] location, Fireable shooter){
        super(location, SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollumBullet.png"), shooter);
    }
    
    /**
     * Move the projectile up.
     */
    @Override
    public void moveUp() {
        setYLocation(getYLocation() - getSpeed());
    }
    
}
