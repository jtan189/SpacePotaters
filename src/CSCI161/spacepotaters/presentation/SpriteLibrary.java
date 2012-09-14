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
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * The library for storing sprites. Contains a static instance of itself so that
 * sprites need to be loaded into memory only once. Afterwards, they can be
 * retrieved using only the sprite name.
 * 
 * Resources:
 * http://www.cokeandcode.com/info/showsrc/showsrc.php?src=../spaceinvaders/org/newdawn/spaceinvaders/SpriteStore.java
 * http://www.javaworld.com/javaworld/javaqa/2002-11/02-qa-1122-resources.html
 */
public class SpriteLibrary {
    
    private static SpriteLibrary storage = new SpriteLibrary();
    private HashMap<String,Sprite> nameToSprite;
    
    /**
     * Default constructor. Initializes name to sprite hashmap.
     */
    public SpriteLibrary(){
        nameToSprite = new HashMap<String,Sprite>();
    }
    
    /**
     * Return the static instance of SpriteLibrary (in which sprites may already
     * be loaded into memory).
     * @return the static instance of SpriteLibrary
     */
    public static SpriteLibrary getSpriteLibrary(){
        return storage;
    }
    
    /**
     * Get the sprite corresponding to string name. First checks to see if the
     * sprite has already been loaded into memory and inserted into hashmap. If it
     * has, it just returns this sprite. Otherwise, it loads the sprite from the
     * sprite image file.
     * @param name the name of the sprite
     * @return the sprite corresponding to the given name
     */
    public Sprite getSprite(String name){
        // if already have the sprite specified loaded, return it
        if (nameToSprite.containsKey(name)){
            return nameToSprite.get(name);
        }
        
        // otherwise load the sprite first and then return it
        Image spriteImage = null;
        
        try {
            
            // create URL for resource
            URL url = this.getClass().getResource(name);
            
            // if can't find URL, then print error and exit game
            if (url == null){
                System.err.println("Can't find reference: " + name);
                System.exit(0);
            }
            
            // read image in
            spriteImage = ImageIO.read(url);
            
        } catch (IOException e){
            System.err.println("Can't find reference: " + name);
            System.exit(0);
        }
        
        // associate spriteImage with sprite and store in nameToSprite
        Sprite sprite = new Sprite(spriteImage);
        nameToSprite.put(name, sprite);
        
        return sprite;
    }
}
