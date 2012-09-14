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

package CSCI161.spacepotaters.menusystem;

import CSCI161.spacepotaters.toplevelgui.SpacePotaters;
import CSCI161.spacepotaters.toplevelgui.SpacePotaters.Difficulty;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * The panel associated with choosing a custom difficulty for a game.
 */
public class ChooseDifficulty extends JPanel{
    
    private static final String DIFFICULTY_IMAGE_LOCATION = "menuImages/difficultyBackground.png";
    
    private SpacePotaters spTop; // top-level GUI
    private Image difficultyBackground;
    
    // menu buttons
    private JButton returnButton;
    private JButton easyButton;
    private JButton mediumButton;
    private JButton hardButton;
    
    /**
     * Default constructor.
     * @param top the top-level GUI embedded into
     */
    public ChooseDifficulty(SpacePotaters top){
        
        spTop = top;
        setLayout(null);
        setFocusable(true);
 
        try {
            
            // create URL for resource
            URL url = this.getClass().getResource(DIFFICULTY_IMAGE_LOCATION);
            
            // if can't find URL, then print error and exit game
            if (url == null){
                System.err.println("Can't find reference: " + DIFFICULTY_IMAGE_LOCATION);
                System.exit(0);
            }
            
            // read image in
            difficultyBackground = ImageIO.read(url);
            
        } catch (IOException e){
            System.err.println("Can't find reference: " + DIFFICULTY_IMAGE_LOCATION);
            System.exit(0);
        }
        
        // add return button
        returnButton = new JButton("Return to Main Menu");
        returnButton.setBounds(spTop.getWindowWidth() - 175, (int)(spTop.getWindowHeight() * .05), 150, 30);
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToMainMenu();
            }
        });
        add(returnButton);
        
        // determine difficulty button widths
        int diffButtonWidth = (int)(spTop.getWindowWidth() * .35);
        
        // add easy button
        easyButton = new JButton("Couch Potato (Easy)");
        easyButton.setBounds((int)(spTop.getWindowWidth() * .33), (int)(spTop.getWindowHeight() * .3), diffButtonWidth, 100);
        easyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToGameView(Difficulty.EASY);
            }
        });
        add(easyButton);
        
        // add medium button
        mediumButton = new JButton("Potato Chip (Medium)");
        mediumButton.setBounds((int)(spTop.getWindowWidth() * .33), (int)(spTop.getWindowHeight() * .5), diffButtonWidth, 100);
        mediumButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToGameView(Difficulty.MEDIUM);
            }
        });
        add(mediumButton);
        
        // add hard button
        hardButton = new JButton("Baked Potato (Hard)");
        hardButton.setBounds((int)(spTop.getWindowWidth() * .33), (int)(spTop.getWindowHeight() * .7), diffButtonWidth, 100);
        hardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToGameView(Difficulty.HARD);
            }
        });
        add(hardButton);
    }
    
    /**
     * Paint the component. Draws a background image for the panel.
     * @param g the graphics context
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(difficultyBackground, 0, 0, spTop.getWindowWidth(), spTop.getWindowHeight(), null);
        
    }
    
}