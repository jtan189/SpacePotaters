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

package CSCI161.spacepotaters.presentation;

import java.awt.Image;

/**
 * A sprite image in the game. Acts as a wrapper for the actual sprite image and
 * provides convenience methods for getting the height and width of the image.
 */
public class Sprite {
    
    private Image spriteImage;
    
    /**
     * Default constructor.
     * @param img the sprite image
     */
    public Sprite(Image img){
        spriteImage = img;
    }
    
    /**
     * Return the sprite's image.
     * @return the sprite's image
     */
    public Image getSpriteImage(){
        return spriteImage;
    }
    
    /**
     * Return the height of the sprite (in pixels).
     * @return the height of the sprite
     */
    public int getHeight(){
        return spriteImage.getHeight(null);
    }
    
    /**
     * Return the width of the sprite (in pixels).
     * @return the width of the sprite
     */
    public int getWidth(){
        return spriteImage.getWidth(null);
    }
}
