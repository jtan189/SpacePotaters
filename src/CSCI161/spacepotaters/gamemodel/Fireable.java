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
 * A game component that has the ability to fire projectiles.
 */
public interface Fireable {
 
    /**
     * Shoot a projectile from this component.
     * @return the projectile shot
     */
    Projectile shoot();
    
}
