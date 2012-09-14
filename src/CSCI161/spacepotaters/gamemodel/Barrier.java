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
import java.util.ArrayList;

/**
 * A protective barrier that blocks projectiles. As a barrier takes damage, it
 * grows smaller. A barrier consists of a 2D array of BarrierBlock objects that
 * can be either active or inactive. Destruction of part of a barrier is simulated
 * by setting the relevant BarrierBlock objects to be inactive.
 */
public class Barrier implements Drawable{
    
    private static final int BLOCK_SIDE_LENGTH = 10; // LENGTH AND WIDTH MUST BE MULTIPLES OF THIS (in pixels)
    
    private ArrayList<ArrayList<BarrierBlock>> blocks; // 2D array of blocks
    private Sprite sprite; 
    private int[] location;
    
    /**
     * Default constructor.
     * @param location the window coordinates of this barrier (top-left corner)
     * @param sprite the sprite associated with this barrier
     */
    public Barrier(int[] location, Sprite sprite){
        
        int barrierHeight = sprite.getHeight();
        int barrierWidth = sprite.getWidth();
        
        // Barrier cannot be broken down exactly into sub-blocks if sides are
        // not multiples of the block side length
        if ((barrierHeight % BLOCK_SIDE_LENGTH != 0) || (barrierWidth % BLOCK_SIDE_LENGTH != 0)){
            System.err.println("Barrier image width or height not a multiple of  " + BLOCK_SIDE_LENGTH + " (in pixels).");
            System.exit(0);
        }
        
        this.sprite = sprite;
        this.location = location;
                
        // initialize blocks (2D array)
        // resource: http://stackoverflow.com/questions/6232257/2d-dynamic-array-using-arraylist-in-java
        int columns = barrierWidth/BLOCK_SIDE_LENGTH;
        int rows = barrierHeight/BLOCK_SIDE_LENGTH;
        blocks = new ArrayList<ArrayList<BarrierBlock>>(columns); // initialize rows
        for (int i = 0; i < columns; i++){
            blocks.add(new ArrayList<BarrierBlock>(rows)); // initialize columns
        }
        
        // initilize each block
        for (int i = 0; i < columns; i++){
            for (int j = 0; j < rows; j++){
                int[] blockLoc = {location[0] + (i*BLOCK_SIDE_LENGTH), location[1] + (j*BLOCK_SIDE_LENGTH)};
                // initialize 2D array elements
                boolean added = blocks.get(i).add( new BarrierBlock(BLOCK_SIDE_LENGTH, blockLoc)); 
            }
        }
    }

    /**
     * Draw the sprite image onto the graphics context provided.
     * @param g the graphics context
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(sprite.getSpriteImage(), location[0], location[1], null);
        drawDamage(g);
    }
    
    /**
     * Return the barrier block hit by a projectile.
     * @param proj the projectile to check
     * @return the barrier block hit (null if none hit)
     */
    public BarrierBlock projectileHitBlock(Projectile proj){
        
        BarrierBlock destroyed = null;
        
        for (ArrayList<BarrierBlock> barrierColumn : blocks){
            // get first active block from the top:
            // if bullet completely misses to the left or right, skip rest of iteration
            boolean bulletMissedLeft = proj.getXLocation() + proj.getSprite().getWidth() < barrierColumn.get(0).getXLocation();
            boolean bulletMissedRight = proj.getXLocation() > (barrierColumn.get(0).getXLocation() + barrierColumn.get(0).getWidth());
            if (bulletMissedLeft || bulletMissedRight){
                continue;
            }
            
            // get first active:
            BarrierBlock firstActive = null;
            
            // get first active for PotaterProjectiles:
            if (proj instanceof PotaterProjectile) {
                for (BarrierBlock block : barrierColumn) {
                    if (block.isActive()) {
                        firstActive = block;
                        break;
                    }
                }
            } else { // proj instanceof GollumProjectile
                // get first active for GollumProjectile
                for (int i = barrierColumn.size() - 1; i >= 0; i--){
                    BarrierBlock block = barrierColumn.get(i);
                    if (block.isActive()){
                        firstActive = block;
                        break;
                    }
                }
            }
           
           // if there is active block in column, check vertical components of location
           // to determine if there was a collision
           if (firstActive != null){
               
               boolean bulletFarEnough = false; //should never really get past an active block without first destroying it (i.e. making it inactive)
               if (proj instanceof PotaterProjectile){
                   bulletFarEnough = proj.getYLocation() + proj.getSprite().getHeight() >= firstActive.getYLocation();
               } else { // proj instanceof GollumProjectile
                   bulletFarEnough = proj.getYLocation() <= firstActive.getYLocation() + firstActive.getHeight();
               }               
               if (bulletFarEnough){
                   destroyed = firstActive;
               }
           }
           
           break; // if make it this far, have found right column. don't need to continue checking the rest of the columns.
        }
        
        return destroyed;
    }
   
    /**
     * Destroy a block. Equivalent to setting it to inactive.
     * @param block the block to destroy
     */
    public void removeBlock(BarrierBlock block){
        block.setInactive();
    }
    
    /**
     * Draw the damage onto the barrier. For each barrier block that is inactive,
     * paint the block the same color as the game background.
     * @param g 
     */
    public void drawDamage(Graphics g){
        for (ArrayList<BarrierBlock> barrierColumn : blocks){
            for (BarrierBlock block : barrierColumn){
                block.drawDamage(g);
            }
        }
    }
    
    /**
     * Return the blocks associated with this barrier.
     * @return the associated 2D array of blocks
     */
    public ArrayList<ArrayList<BarrierBlock>> getBlocks(){
        return blocks;
    }
    
    /**
     * Return the x-coordinate of this barrier.
     * @return the x-coordinate
     */
    public int getXLocation(){
        return location[0];
    }
    
    /**
     * Return the y-coordinate of this barrier.
     * @return the y-coordinate
     */
    public int getYLocation(){
        return location[1];
    }
    
    /**
     * Return the sprite associated with this barrier.
     * @return the barrier's sprite
     */
    public Sprite getSprite(){
        return sprite;
    }
    
}
