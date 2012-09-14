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
 * The panel associated with viewing instructions.
 */
public class InstructionView extends JPanel{
    
    private static final String INSTRUCTIONS_IMAGE_LOCATION = "menuImages/instructionsBackground.png";
    
    private SpacePotaters spTop; // top-level GUI
    private Image instructionsBackground;
    
    // menu buttons
    private JButton returnButton;
    
    /**
     * Default constructor.
     * @param top 
     */
    public InstructionView(SpacePotaters top){
        
        spTop = top;
        setLayout(null);
        setFocusable(true);
 
        try {
            
            // create URL for resource
            URL url = this.getClass().getResource(INSTRUCTIONS_IMAGE_LOCATION);
            
            // if can't find URL, then print error and exit game
            if (url == null){
                System.err.println("Can't find reference: " + INSTRUCTIONS_IMAGE_LOCATION);
                System.exit(0);
            }
            
            // read image in
            instructionsBackground = ImageIO.read(url);
            
        } catch (IOException e){
            System.err.println("Can't find reference: " + INSTRUCTIONS_IMAGE_LOCATION);
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
    }
    
    /**
     * Paint the component. Draws a background image for the panel.
     * @param g the graphics context
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(instructionsBackground, 0, 0, spTop.getWindowWidth(), spTop.getWindowHeight(), null);
        
    }
    
}
