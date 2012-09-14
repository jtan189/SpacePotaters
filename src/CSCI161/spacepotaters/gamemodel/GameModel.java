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
import CSCI161.spacepotaters.toplevelgui.SpacePotaters.Difficulty;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

/**
 * The model/logic of the game. Maintains the state of the game and enforces
 * the rules of the game. In an attempt to implement a model-view-controller
 * architecture, the game model is separate from component used to display the
 * game and the component used to get user input.
 */
public class GameModel {

    public static final int DEFAULT_BULLET_SPEED = 6;
    public static final int NUM_OF_BARRIERS = 3;
    public static final int DEFAULT_NUM_LIVES = 4; // starting life + 3 extra
    
    // game components
    private PotaterWave pWave; // wave of enemies moving downwards (5x11)
    private Gollum smeagol;
    private ArrayList<Barrier> barriers;
    private ArrayList<Projectile> projectiles;
    private BonusPotater bonus;
    
    // game state data    
    private int score;
    private int lives;
    private boolean gameOver;
    private boolean gamePaused;
    
    // game settings
    private Difficulty diffSetting;
    private int windowWidth;
    private int windowHeight;
    private int leftBoundary = 10;
    private int rightBoundary;
    private int bonusTopSpacer;
    private int topBoundary = 10;
    
    // current status of user input (monitored and set using GameView class)
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean firePressed;
    
    private Random rand;

    /**
     * Default constructor. Start game with medium difficulty.
     * @param windowWidth the width of the window the game operates within
     * @param windowHeight the height of the window the game operates within
     */
    public GameModel(int windowWidth, int windowHeight) {
        this(Difficulty.MEDIUM, windowWidth, windowHeight);
    }

    /**
     * Overloaded constructor. Creates game with given difficulty.
     * @param diff the difficulty setting
     * @param windowWidth the width of the window the game operates within
     * @param windowHeight the height of the window the game operates within
     */
    public GameModel(Difficulty diff, int windowWidth, int windowHeight) {
        
        rand = new Random();
        
        // initialize settings
        diffSetting = diff;
        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        rightBoundary = windowWidth - 10;
        bonusTopSpacer = SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollum.png").getHeight() + topBoundary; 

        // initially no user input
        leftPressed = false;
        rightPressed = false;
        firePressed = false;

        // initalize state data
        score = 0;
        lives = getAllottedLives(diff);

        // initialize game components:
        
        // initialize potater wave
        pWave = new PotaterWave(diff, this);

        // initialize projectiles
        projectiles = new ArrayList<Projectile>();
        
        // initialize Gollum
        smeagol = new Gollum(this);

        // initialize barriers
        barriers = new ArrayList<Barrier>(NUM_OF_BARRIERS);
        
        int spacerBarrierGollum = 30; // space between barrier and gollum
        Sprite barrierSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/barrier.png");
        
        int yLoc = smeagol.getYLocation() - barrierSprite.getHeight() - spacerBarrierGollum;
        
        // center barriers with equal spacing
        int barrierSpriteWidth = barrierSprite.getWidth();
        int barrierSpacing = ((rightBoundary - leftBoundary) - (NUM_OF_BARRIERS * barrierSpriteWidth)) / (NUM_OF_BARRIERS + 1);
        for (int i = 0; i < NUM_OF_BARRIERS; i++){
            int[] barrierLoc = {leftBoundary + barrierSpacing + (i * (barrierSpacing + barrierSpriteWidth)), yLoc};
            barriers.add(new Barrier(barrierLoc, barrierSprite));
        }
        
        // relocate Gollum under a barrier near the middle
        Barrier middle = barriers.get(NUM_OF_BARRIERS/2);
        smeagol.setXLocation(middle.getXLocation() + ((middle.getSprite().getWidth() - smeagol.getSprite().getWidth())/ 2));

    }
 
    /**
     * Return true if level is complete. This happens when the player has killed
     * all the potaters in the wave.
     * @return true if level is complete
     */
    public boolean completedLevel(){
        if (pWave.isEmpty()){
            return true;
        }
        
        return false;
    }

    /**
     * Load next level (of same difficulty). Keep current score, barrier state,
     * and lives in the transition.
     */
    public void nextLevel(){
        pWave = new PotaterWave(diffSetting, this);
    }

    /**
     * Return true if the projectile is off the screen.
     * @param proj the projectile to check
     * @return true if the projectile has moved off the screen
     */
    public boolean outOfRange(Projectile proj) {

        if (proj instanceof GollumProjectile) {
            // check if moved off top
            return (proj.getLocation()[1] + proj.getSprite().getHeight() < 0);
        } else { // proj instanceof PotaterProjectile
            // check if moved off bottom
            return (proj.getLocation()[1] > windowHeight);
        }
    }

    /**
     * Return true if the bonus potater has moved off the screen.
     * @param tater the bonus potater to check
     * @return true if bonus potater has moved off the left or right side of the
     * screen
     */
    public boolean outOfRange(BonusPotater tater) {
        if (tater.isMovingLeft()) { // check if too far left
            return (tater.getXLocation() - tater.getSprite().getWidth() < 0);
        } else { // moving right - check if too far right
            return (tater.getXLocation() > windowWidth);
        }
    }

    /**
     * Check whether the shooter has the ability to shoot. A shooter can shoot
     * only if it does not already have a projectile on the screen.
     * @param shooter the shooter to check
     * @return true if the shooter is allowed to shoot
     */
    public boolean canShoot(Fireable shooter) {
        for (Projectile proj : projectiles) {
            if (proj.getShooter() == shooter) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Return the allotted lives associated with a given difficulty setting. More
     * lives are given for easier difficulty settings.
     * @param diff the difficulty setting
     * @return the number of lives allotted
     */
    public final int getAllottedLives(Difficulty diff){
        lives = 0;
        switch(diff){
            case EASY:
                lives = DEFAULT_NUM_LIVES + 1;
                break;
            case MEDIUM:
                lives = DEFAULT_NUM_LIVES;
                break;
            case HARD:
                lives = DEFAULT_NUM_LIVES - 1;
                break;
        }
        return lives;
    }

    /**
     * Test whether or not the potater wave has reached Gollum.
     * @return true if potater wave has successfully invaded
     */
    public boolean invasionSuccessful() {
        return pWave.hasInvaded();
    }

    /**
     * Check each projectile to see if it has hit anything or gone off the screen.
     * If a projectile has hit something, remove it and the component it hit
     * from the game.
     */
    public void checkProjectiles() {

        // use for removal of bullets in bullet-bullet collision (avoids ConcurrentModificationException)
        LinkedList<Projectile> bulletsToRemove = new LinkedList<Projectile>();

        // for each projectile, determine what it has collided with (if anything)
        for (Iterator<Projectile> bulletIter = projectiles.iterator(); bulletIter.hasNext();) {
            Projectile bullet = bulletIter.next();
            Destroyable destroyed = collides(bullet); 
            if (destroyed != null) { // hit something

                // hit potater in wave
                if (destroyed instanceof OrdinaryPotater) {
                    bulletIter.remove();
                    score += ((Potater) destroyed).getPointValue();
                    pWave.killPotater((OrdinaryPotater) destroyed);
                    
                    // if killed last potater, load next level
                    if (completedLevel()){
                        nextLevel();
                    }
                    
                // hit bonus potater
                } else if (destroyed instanceof BonusPotater) {
                    bulletIter.remove();
                    score += ((BonusPotater)destroyed).getPointValue();
                    bonus = null;
                    
                // hit Gollum
                } else if (destroyed instanceof Gollum) {
                    bulletIter.remove();
                    lives--;
                    
                    if (lives == 0){ // just lost last life
                        gameOver = true;
                    } else {
                        smeagol.resetPosition();
                    }
                
                // hit barrier
                } else if (destroyed instanceof BarrierBlock) {
                    bulletIter.remove();
                    ((BarrierBlock)destroyed).setInactive();
                    
                // hit another projectile
                } else { // destroyed instanceof Projectile
                    bulletsToRemove.add(bullet);
                    bulletsToRemove.add((Projectile) destroyed);
                    // projectiles.remove((Projectile) destroyed); // might throw CME
                }
            }
        }

        projectiles.removeAll(bulletsToRemove); // remove bullets in bullet-bullet collisions
    }

    /**
     * Return the game component the projectile collided with.
     * @param bullet the projectile to check
     * @return the game component collided with (null if no collision)
     */
    public Destroyable collides(Projectile bullet) {

        Destroyable destroyed = null;

        // both Gollum and potater bullets can damage barriers
        for (Barrier barrier : barriers) {
            destroyed = barrier.projectileHitBlock(bullet);
            if (destroyed != null) {
                return destroyed;
            }
        }

        // bullets can damage other bullets - only need to check one type of bullet - will cover
        // both cases (if bullet A hit bullet B, then bullet B necessarily hits bullet A also)
        if (bullet instanceof GollumProjectile) {
            // if shot by gollum, check bullets shot by potater
            for (Projectile bulletTarget : projectiles) {
                if (bulletTarget instanceof PotaterProjectile) {
                    if (bullet.getRectRep().intersects(bulletTarget.getRectRep())) {
                        return bulletTarget;
                    }
                }
            }
        }

        // if Gollum shot, need to check potaters
        if (bullet instanceof GollumProjectile) {
            // check bonus
            if (bonus != null && bullet.getRectRep().intersects(bonus.getRectRep())) {
                return bonus;
            }
            // check pWave potaters
            destroyed = pWave.projectileHitPotater(bullet);
            if (destroyed != null) {
                return destroyed;
            }
        // if potater shot, need to check if hit Gollum
        } else { // bullet instanceof PotaterProjectile
            if (bullet.getRectRep().intersects(smeagol.getRectRep())) {
                return smeagol;
            }
        }

        return destroyed;
    }
    
    /**
     * Inflict damage on any parts of the barriers that a potater has overtaken.
     * @param tater the potater to check
     */
    public void destroyOvertakenBarrier(OrdinaryPotater tater) {

        // brute force collision detection (only type used in this game)
        for (Barrier barrier : barriers){
            // check each block within 2D array of blocks
            ArrayList<ArrayList<BarrierBlock>> blocks = barrier.getBlocks();
            for (ArrayList<BarrierBlock> blockColumn : blocks){
                for (BarrierBlock block : blockColumn){
                    if (tater.getRectRep().intersects(block.getRectRep())){
                        block.setInactive();
                    }
                }
            }
        }
    }

    /**
     * Update the game state (if game is not paused or over). Move components,
     * check for collisions, shoot projectiles, remove off-screen components.
     */
    public void gameUpdate() {
        if (!gameOver && !gamePaused) {
            
            // move potater wave
            pWave.move();
            if (invasionSuccessful()) {
                gameOver = true;
                return;
            }
            
            // if wave overlaps barriers, remove that part of barrier
            if (pWave.lowestPoint() >= barriers.get(0).getYLocation()){
                for (OrdinaryPotater tater : pWave.getPotaters()){
                    destroyOvertakenBarrier(tater);
                }
            }

            // move Gollum
            if (!(leftPressed && rightPressed)) { // if left and right are not both pressed

                if (leftPressed) {
                    smeagol.moveLeft();
                } else if (rightPressed) {
                    smeagol.moveRight();
                }
            }
            
            // move bonus potater (if exists)
            if (bonus != null){
                bonus.move();
                if (outOfRange(bonus)){ // remove if has traveled out of view
                    bonus = null;
                }
            }

            // move existing bullets
            // resource: http://helpdesk.objects.com.au/java/how-to-fix-concurrentmodificationexception
            Iterator<Projectile> iter = projectiles.iterator();
            while (iter.hasNext()) {
                Projectile proj = iter.next();
                // probably should have had GollumProjectile and PotaterProjectile
                // override Projectile's move() with movement in respective directions
                if (proj instanceof GollumProjectile) {
                    ((GollumProjectile) proj).moveUp();
                } else { // proj instanceof PotaterProjectile
                    ((PotaterProjectile) proj).moveDown();
                }

                // check if bullets have traveled off the screen; remove from game if have
                if (outOfRange(proj)) {
                    iter.remove();
                }
            }

            // check if bullets have hit anything
            checkProjectiles();

            // shoot Gollum bullet
            if (firePressed && canShoot(smeagol)) {
                projectiles.add(smeagol.shoot());
            }

            // randomly shoot potater bullets
            pWave.randomShootBottoms();
            
            // randomly create bonus potater
            randomDeployBonus();
            
        }
    }
    
    /**
     * Randomly create and deploy a bonus potater into the game.
     */
    public void randomDeployBonus() {
        if ( bonus == null && (rand.nextInt(10000) + 1) < 5) { //.05% chance
            
            // if moving right
            int xLoc = -1;
            boolean movingLeft = false;
            
            // 50% chance start at left side; change starting x coordinate
            if (rand.nextInt(2) == 0) { 
                xLoc = windowWidth + 1;
                movingLeft = true;
            }
            
            int[] location = {xLoc, bonusTopSpacer};
            bonus = new BonusPotater(location, movingLeft);          
        } 
    }


    /**
     * Set whether or not the left key is pressed.
     * @param leftPressed the state to set leftPressed to
     */
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    /**
     * Set whether or not the right key is pressed.
     * @param rightPressed the state to set rightPressed to
     */
    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    /**
     * Set whether or not the fire key is pressed.
     * @param firePressed the state to set firePressed to
     */
    public void setFirePressed(boolean firePressed) {
        this.firePressed = firePressed;
    }
    
    /**
     * Toggle in-game pausing.
     */
    public void togglePause(){
        gamePaused = !gamePaused;
    }
    
    /**
     * Return whether or not the game is paused.
     * @return true if game is paused
     */
    public boolean isPaused(){
        return gamePaused;
    }
    
    /**
     * End the game.
     */
    public void terminateGame(){
        gameOver = true;
    }
    
    /**
     * Return whether or not the game is over.
     * @return true if game is over
     */
    public boolean gameIsOver(){
        return gameOver;
    }
    
    /**
     * Set whether the game is paused or not.
     * @param isPaused the state of paused to set to
     */
    public void setIsPaused(boolean isPaused){
        gamePaused = isPaused;
    }
    
    /**
     * Get the left boundary of the game. Inwards from the actual game window edge.
     * @return the left game boundary
     */
    public int getLeftBoundary() {
        return leftBoundary;
    }

    /**
     * Get the right boundary of the game. Inwards from the actual game window edge.
     * @return the right game boundary
     */
    public int getRightBoundary() {
        return rightBoundary;
    }
    
    /**
     * Get the top boundary of the game. Inwards from the actual game window edge.
     * @return the top game boundary
     */
    public int getTopBoundary(){
        return topBoundary;
    }

    /**
     * Get the height of the window the game is being displayed in.
     * @return the window height
     */
    public int getWindowHeight() {
        return windowHeight;
    }
    
    /**
     * Get the width of the window the game is being displayed in.
     * @return the window width
     */
    public int getWindowWidth(){
        return windowWidth;
    }
    
    /**
     * Return the Gollum component.
     * @return Gollum component of the game
     */
    public Gollum getGollum() {
        return smeagol;
    }
    
    /**
     * Get the size (in pixels) of the spacer between the top of the screen and
     * the bonus potater.
     * @return the bonus potater - top of window spacer
     */
    public int getBonusTopSpacer(){
        return bonusTopSpacer;
    }
    
    /**
     * Return the bonus potater component of the game.
     * @return the bonus potater
     */
    public BonusPotater getBonusPotater(){
        return bonus;
    }

    /**
     * Return the potater wave component of the game.
     * @return the potater wave
     */
    public PotaterWave getPotaterWave() {
        return pWave;
    }

    /**
     * Get the current score of the game.
     * @return the current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Get the number of lives left before the game is over.
     * @return the number of lives left
     */
    public int getNumLivesLeft(){
        return lives;
    }

    /**
     * Return a list of the projectile components currently in the game.
     * @return the projectile components
     */
    public ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    
    /**
     * Return a list of the barrier components in the game.
     * @return the barrier components
     */
    public ArrayList<Barrier> getBarriers(){
        return barriers;
    }
}
