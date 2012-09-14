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
 * The main menu panel of the game.
 */
public class MainMenu extends JPanel{
    
    private static final String MAIN_MENU_IMAGE_LOCATION = "menuImages/mainMenuBackground.png";
    
    private SpacePotaters spTop; // top-level GUI
    private Image mainMenuBackground;
    
    // menu buttons
    private JButton quickStartButton;
    private JButton chooseDifficultyButton;
    private JButton viewInstructionsButton;
    private JButton viewLeaderboardsButton;
    private JButton exitButton;
    
    public MainMenu(SpacePotaters top){
        
        spTop = top;
        setLayout(null);
        setFocusable(true);
 
        try {
            
            // create URL for resource
            URL url = this.getClass().getResource(MAIN_MENU_IMAGE_LOCATION);
            
            // if can't find URL, then print error and exit game
            if (url == null){
                System.err.println("Can't find reference: " + MAIN_MENU_IMAGE_LOCATION);
                System.exit(0);
            }
            
            // read image in
            mainMenuBackground = ImageIO.read(url);
            
        } catch (IOException e){
            System.err.println("Can't find reference: " + MAIN_MENU_IMAGE_LOCATION);
            System.exit(0);
        }
        
        // calculate ideal button starting x-coordinate and width
        int buttonStartX = (int)(spTop.getWindowWidth() * .623);
        int buttonWidth = (int) (spTop.getWindowWidth() * .28);
        
        // add quickstart button
        quickStartButton = new JButton("Quickstart");
        quickStartButton.setBounds(buttonStartX, (int)(spTop.getWindowHeight() * .2), buttonWidth, 50);
        quickStartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToGameView(Difficulty.MEDIUM);
            }
        });
        add(quickStartButton);
        
        // add choose-difficulty start button
        chooseDifficultyButton = new JButton("Start Game (Custom Difficulty)");
        chooseDifficultyButton.setBounds(buttonStartX, (int)(spTop.getWindowHeight() * .31), buttonWidth, 50);
        chooseDifficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.chooseDifficulty();
            }
        });
        add(chooseDifficultyButton);
        
        
        // add leaderboard button
        viewLeaderboardsButton = new JButton("View Leaderboard");
        viewLeaderboardsButton.setBounds(buttonStartX, (int)(spTop.getWindowHeight() * .42), buttonWidth, 50);
        viewLeaderboardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.switchToLeaderboard();
            }
        });
        add(viewLeaderboardsButton);
        
        // add a view-instructions button
        viewInstructionsButton = new JButton("View Instructions");
        viewInstructionsButton.setBounds(buttonStartX, (int)(spTop.getWindowHeight() * .53), buttonWidth, 50);
        viewInstructionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                spTop.viewInstructions();
            }
        });
        add(viewInstructionsButton);
        
        // add an exit button
        exitButton = new JButton("Exit");
        exitButton.setBounds(buttonStartX, (int)(spTop.getWindowHeight() * .64), buttonWidth, 50);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        add(exitButton);
    }
    
    /**
     * Paint the component. Draws a background image for the panel.
     * @param g the graphics context
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(mainMenuBackground, 0, 0, spTop.getWindowWidth(), spTop.getWindowHeight(), null);
        
    }
    
}