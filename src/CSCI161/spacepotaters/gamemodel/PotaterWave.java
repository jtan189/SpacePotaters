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

import CSCI161.spacepotaters.gamemodel.OrdinaryPotater.PotaterRank;
import CSCI161.spacepotaters.presentation.Sprite;
import CSCI161.spacepotaters.presentation.SpriteLibrary;
import CSCI161.spacepotaters.toplevelgui.SpacePotaters.Difficulty;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * The wave of ordinary potaters trying to invade and/or kill Gollum. 
 */
public class PotaterWave implements Drawable{
    
    // default size of a discrete step taken by Ordinary Potaters
    private static final int DEFAULT_OP_STEP_SIZE = 15;
    // wait period for slow potaters (wait period = number of game loops to wait)
    private static final int slowWaitPeriod = 30; 
    
    private GameModel game;
    private Difficulty difficultySetting;
    private int topSpacer; // vertical spacer from top of window
    private int initialLowestPoint; // used in determining milestones
    
    private ArrayList<OrdinaryPotater> potaters;
    private boolean leftDirection; // true if currently moving left
    private int speedModifier;
    private int waitPeriod; // number of loops to wait before can move
    private int haveWaited; // number of loops have waited for
    
    // progress milestone flags
    private boolean reached25Percent;
    private boolean reached50Percent;
    private boolean reached75Percent;

    private Random randomShot;
    
    /**
     * Default constructor.
     * @param difficultySetting the difficulty setting of the game
     * @param game the game belonged to
     */
    public PotaterWave(Difficulty difficultySetting, GameModel game){
        this.game = game;
        this.difficultySetting = difficultySetting;
        randomShot = new Random();
        potaters = new ArrayList<OrdinaryPotater>();
        leftDirection = false; // begin moving right
        haveWaited = 0;
        reached25Percent = false;
        reached50Percent = false;
        reached75Percent = false;
         
        switch (difficultySetting) { // speed of tater depends on difficulty
            case EASY:
                speedModifier = 0;
                break;
            case MEDIUM:
                speedModifier = 1;
                break;
            case HARD:
                speedModifier = 2;
                break;
        }
        
        waitPeriod = slowWaitPeriod - (speedModifier * 10);

        // determine top spacer and initial x-coordinate of wave
        int bonusSpriteHeight = SpriteLibrary.getSpriteLibrary().getSprite("sprites/bonus.png").getHeight();
        topSpacer = 10 + game.getBonusTopSpacer() + bonusSpriteHeight; 
        Sprite sprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/topOrdinaryPotaterV1.png");
        int centeredStartX = ((game.getRightBoundary() - game.getLeftBoundary()) - (11*sprite.getWidth())) / 2;
        
        // create ordinary potaters
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 11; col++) {
                // top row, next two rows, and next two rows after that have different sets of sprites
                PotaterRank rank = null;
                switch (row) {
                    case 0:
                        rank = PotaterRank.TOP;
                        break;
                    case 1:
                    case 2:
                        rank = PotaterRank.MIDDLE;
                        break;
                    case 3:
                    case 4:
                        rank = PotaterRank.BOTTOM;
                        break;
                }
                boolean isSpriteV1 = ((row + col)%2 == 0)? true : false; // alternate sprite versions
                int[] location = {(centeredStartX + (col * sprite.getWidth())), (topSpacer + (row * sprite.getHeight()))};
                // initialize and add potater to wave
                OrdinaryPotater tater = new OrdinaryPotater(DEFAULT_OP_STEP_SIZE, location, rank, isSpriteV1);
                addPotater(tater);
                if (row == 4 && col == 10){
                    initialLowestPoint = tater.getYLocation() + sprite.getHeight();
                }
            }

        }
    }
    
    /**
     * Add an ordinary potater to the wave.
     * @param tater the potater to add
     */
    private void addPotater(OrdinaryPotater tater){
        potaters.add(tater);
    }
    
    /**
     * Draw the potater wave onto the graphics context.
     * @param g the graphics context to draw to
     */
    @Override
    public void draw(Graphics g) {
        for (OrdinaryPotater tater : potaters){
            tater.draw(g);
        }
    }
    
    /**
     * Check if it is time to move the wave. Update haveWaited.
     * @return true if it is time to move the potater wave
     */
    public boolean isTimeToMove(){
        if (haveWaited >= waitPeriod){
            haveWaited = 0;
            return true;
        }
        
        haveWaited++;
        return false;
    }
    
    /**
     * Return true if potater wave is empty.
     * @return true if the potater wave is empty
     */
    public boolean isEmpty(){
        return potaters.isEmpty();
    }
    
    /**
     * Move the potater wave (if it is time to move them). Alternate sprites of the
     * potaters during move. Also check if a milestone has been reached - speed
     * up accordingly if have. If the wave reaches the edge of the screen, move
     * down one step and change directions.
     */
    public void move() {
        if (isTimeToMove()) { // check if have waited enough loops
            alternateSprites(); // animate
            if (atEdge()) {
                leftDirection = !leftDirection; // reverse direction
                for (OrdinaryPotater tater : potaters) {
                    tater.moveDown();
                    // check if reached milestone; only perform once when initially reach the milestone
                    boolean updatePeriod = false;
                    if (!reached25Percent && lowestPoint() > yCoordWhenPercentTraveled(.25)) {
                        updatePeriod = true;
                        reached25Percent = true;
                    } else if (!reached50Percent && lowestPoint() > yCoordWhenPercentTraveled(.50)) {
                        updatePeriod = true;
                        reached50Percent = true;
                    } else if (!reached75Percent && lowestPoint() > yCoordWhenPercentTraveled(.75)) {
                        updatePeriod = true;
                        reached75Percent = true;
                    }
                    if (updatePeriod) {
                        // decrease wait period = speed up wave (move more often)
                        waitPeriod -= (speedModifier * 5);
                    }
                }
            } else { // not at edge
                if (leftDirection) {
                    for (OrdinaryPotater tater : potaters) {
                        tater.moveLeft();
                    }
                } else { // rightDirection
                    for (OrdinaryPotater tater : potaters) {
                        tater.moveRight();
                    }
                }
            }
        }
    }

    /**
     * Determine the y-coordinate of a wave that has traveled the given fraction
     * of the screen.
     * @param fractionTraveled the fraction of the game screen traveled
     * @return the y-coordinate associated with the given fraction traveled
     */
    public int yCoordWhenPercentTraveled(double fractionTraveled) {
        double totalDistance = game.getGollum().getYLocation() - initialLowestPoint;
        return initialLowestPoint + (int)(totalDistance * fractionTraveled);
    }

    /**
     * Return y-coordinate of the lowest point of the wave.
     * @return the lowest y-coordinate of the wave
     */
    public int lowestPoint() {
        int max = 0;
        int maxSpriteHeight = SpriteLibrary.getSpriteLibrary().getSprite("sprites/topOrdinaryPotaterV1.png").getHeight();
        for (OrdinaryPotater tater : potaters) {
            int searchLoc = tater.getYLocation();
            if (searchLoc > max) {
                max = searchLoc;
            }
        }
        return max + maxSpriteHeight;
    }

    /**
     * Determine whether or not the wave has reached the left or right edge.
     * @return true if the wave has reached an edge
     */
    public boolean atEdge() {
        for (OrdinaryPotater tater : potaters) {
            if (leftDirection) { // check if at left edge
                if (tater.getLocation()[0] - tater.getSpeed() < game.getLeftBoundary()) {
                    return true;
                }
            } else { // rightDirection
                if (tater.getLocation()[0] + tater.getSprite().getWidth() + tater.getSpeed() > game.getRightBoundary()) {
                    return true;
                }
            }
        }

        return false; // none of the potaters are at an edge
    }
    
    /**
     * Determine if the given potato is on the bottom of the wave, i.e. there are
     * no potaters directly below. Used to determine if a potater can shoot or not.
     * @param tater the potater to check if is on bottom
     * @return true if the potater is on the bottom of the wave
     */
    public boolean potaterOnBottom(Potater tater){
        int[] taterLocation = tater.getLocation();
        for (OrdinaryPotater otherTater : potaters){
            int[] otherTaterLocation = otherTater.getLocation();
            if ((tater != otherTater) && (otherTaterLocation[0] == taterLocation[0]) && (otherTaterLocation[1] > taterLocation[1])){
                return false;
            }
        }
        return true;
    }
    
    /**
     * Randomly fire the projectiles from the potaters that are located on the 
     * bottom of the wave.
     */
    public void randomShootBottoms(){
        for (OrdinaryPotater tater : potaters){
            
            if (potaterOnBottom(tater)){
                boolean shouldShoot = false;
                switch (difficultySetting){ // harder difficulty = more shooting
                    case EASY:
                        shouldShoot = ((randomShot.nextInt(1000) + 1) < 3); // 0.3% chance shoot each loop (
                        break;
                    case MEDIUM:
                        shouldShoot = ((randomShot.nextInt(1000) + 1) < 6); // 0.6%
                        break;
                    case HARD:
                        shouldShoot = ((randomShot.nextInt(1000) + 1) < 9); // 0.9%
                        break;
                }
                
                if (shouldShoot && game.canShoot(tater)){
                    game.getProjectiles().add(tater.shoot());
                }
                
            }
        }
    }
    
    /**
     * Return true if the wave has invaded, i.e. if the potater wave has reached
     * Gollum.
     * @return true if the wave has successfully invaded 
     */
    public boolean hasInvaded(){
        for (OrdinaryPotater tater : potaters){
            if ((tater.getYLocation() + ((Potater)tater).getSprite().getHeight()) > game.getGollum().getLocation()[1]){
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Return the potater that the given projectile has hit (or null if it
     * has not hit anything).
     * @param proj the projectile to check
     * @return the ordinary potater hit by the projectile (null if none hit)
     */
    public OrdinaryPotater projectileHitPotater(Projectile proj){
        for (OrdinaryPotater tater : potaters){
            if (proj.getRectRep().intersects(tater.getRectRep())){
                return tater;
            }
        }
        return null;
    }
    
    /**
     * Remove a killed potater from the wave.
     * @param casualty the potater killed
     * @return true if remove was successful
     */
    public boolean killPotater(OrdinaryPotater casualty){
        return potaters.remove(casualty);
    }
    
    /**
     * Alternate the sprites in the waves. Switch each potater's sprite version.
     */
    public void alternateSprites(){
        for (OrdinaryPotater tater : potaters){
            tater.switchSprite();
        }
    }
    
    /**
     * Return a list of potaters in the potater wave.
     * @return a list of potaters in the wave
     */
    public ArrayList<OrdinaryPotater> getPotaters(){
        return potaters;
    }
    
}
