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

/**
 * An ordinary potater moving downwards to Gollum within the wave. Have different
 * ranks depending on which row in the wave the ordinary potaters are located.
 */
public class OrdinaryPotater extends Potater implements MoveableDown, Fireable{
    
    private boolean isSpriteV1; // true if sprite is version 1 (out of 2 versions)
    private PotaterRank rank;
    
    /**
     * Constructor.
     * @param speed the speed of the potater
     * @param location the coordinates of the potater
     * @param pointValue the point value of the potater
     * @param sprite the sprite associated with the potater
     */
    public OrdinaryPotater(int speed, int[] location, int pointValue, Sprite sprite){
        super(speed, location, pointValue, sprite);
        isSpriteV1 = false;
        rank = PotaterRank.MIDDLE;
    }
    
    /**
     * Constructor.
     * @param speed the speed of the potater
     * @param location the coordinates of the potater
     * @param rank the rank of the potater (depends on initial row position)
     * @param isSpriteV1 true if the current sprite assigned is version 1
     */
    public OrdinaryPotater(int speed, int[] location, PotaterRank rank, boolean isSpriteV1){
        this(speed, location, getPointsFromRank(rank), getSpriteFromRank(rank, isSpriteV1));
        this.isSpriteV1 = isSpriteV1;
        this.rank = rank;
    }
    
    /**
     * Determine the point value from a rank.
     * @param rank the rank of the potater
     * @return the point value of the potater
     */
    public static int getPointsFromRank(PotaterRank rank){
        
        if (rank == PotaterRank.TOP){
            return 500;
        } else if (rank == PotaterRank.MIDDLE){
            return 300;
        } else { // BOTTOM
            return 100;
        }
        
    }
    
    /**
     * Return the sprite using the rank (and sprite version) of the potater.
     * @param rank the rank of the potater (based on initial row placement)
     * @param isSpriteV1 true if currently assigned to sprite version 1
     * @return the sprite for the potater
     */
    public static Sprite getSpriteFromRank(PotaterRank rank, boolean isSpriteV1) {
        Sprite sprite = null;
        switch (rank) {
            case TOP:
                if (isSpriteV1) {
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/topOrdinaryPotaterV1.png");
                } else { // v2
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/topOrdinaryPotaterV2.png");
                }
                break;
            case MIDDLE:
                if (isSpriteV1) {
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/middleOrdinaryPotaterV1.png");
                } else { // v2
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/middleOrdinaryPotaterV2.png");
                }
                break;
            case BOTTOM:
                if (isSpriteV1) {
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/bottomOrdinaryPotaterV1.png");
                } else { // v2
                    sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/bottomOrdinaryPotaterV2.png");
                }
                break;
        }
        return sprite;
    }
    
    /**
     * Move the potater down.
     */
    @Override
    public void moveDown(){
        setYLocation(getLocation()[1] + getSpeed());
    }
    
    /**
     * Shoot a projectile with the potater.
     * @return the projectile shot
     */
    @Override
    public Projectile shoot(){
        Sprite shotSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/potaterBullet.png");
        int xBulletLoc = getXLocation() + ((getSprite().getWidth() - shotSprite.getWidth())/2);
        int yBulletLoc = getYLocation() + shotSprite.getHeight();
        int[] bulletLoc = {xBulletLoc, yBulletLoc};
        return new PotaterProjectile(bulletLoc, this);
    }

    /**
     * Move the potater left. Validity of move monitored by PotaterWave.
     */
    @Override
    public void moveLeft() {
        setXLocation(getLocation()[0] - getSpeed());
    }

    /**
     * Move the potater right. Validity of move monitored by PotaterWave.
     */
    @Override
    public void moveRight() {
        setXLocation(getLocation()[0] + getSpeed());
    }

    /**
     * Switch the potater's sprite version. Used for animation.
     */
    public void switchSprite(){
        isSpriteV1 = !isSpriteV1;
        setSprite(getSpriteFromRank(rank, isSpriteV1));
    }
    
    /**
     * Different ranks that can be assigned to an ordinary potater.
     */
    public enum PotaterRank {
        TOP, MIDDLE, BOTTOM
    }
}
