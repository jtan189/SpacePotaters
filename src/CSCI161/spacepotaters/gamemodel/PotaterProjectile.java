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
 * A projectile shot from an ordinary potater.
 */
public class PotaterProjectile extends Projectile implements MoveableDown {
    
    /**
     * Constructor specifying speed.
     * @param speed the speed of the projectile
     * @param location the coordinates of the projectile (top-left corner)
     * @param shooter the component that shot the projectile
     */
    public PotaterProjectile(int speed, int[] location, Fireable shooter){
        super(speed, location, SpriteLibrary.getSpriteLibrary().getSprite("sprites/potaterBullet.png"), shooter);
    }
    
    /**
     * Constructor.
     * @param location the coordinates of the projectile (top-left corner)
     * @param shooter the component that shot the projectile
     */
    public PotaterProjectile(int[] location, Fireable shooter){
        super(location, SpriteLibrary.getSpriteLibrary().getSprite("sprites/potaterBullet.png"), shooter);
    }

    /**
     * Move the projectile down.
     */
    @Override
    public void moveDown() {
        setYLocation(getYLocation() + getSpeed());
    }
    
}
