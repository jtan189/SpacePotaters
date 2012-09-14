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

import java.applet.AudioClip;
import java.util.HashMap;
import javax.swing.JApplet;

/**
 * The library for storing sound effects.
 * 
 * Note: This class is incomplete.
 */
public class SoundEffectsLibrary extends JApplet{
    
    private static SoundEffectsLibrary soundStorage;
    private HashMap<String,AudioClip> nameToSound;
    
    /**
     * Play the sound corresponding the sound name.
     * @param name the sound name
     */
    public void playSound(String name){
        // to do
    }
}
