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

/*
 * Much thanks to Karen Clark for her help in this project. Karen willfully
 * donated her time to serve as the graphic and creative designer of Space Potaters.
 * This includes all the sprite and background images. Without her the game would
 * probably be limited to showing different colored squares shooting at each other.
 */

package CSCI161.spacepotaters.toplevelgui;

import CSCI161.spacepotaters.leaderboard.Leaderboard;
import CSCI161.spacepotaters.menusystem.ChooseDifficulty;
import CSCI161.spacepotaters.menusystem.InstructionView;
import CSCI161.spacepotaters.menusystem.MainMenu;
import CSCI161.spacepotaters.presentation.GameView;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * The top-level GUI for Space Potaters. The window size is set here. This class
 * also handles switching the visible panel (using CardLayout). The main method
 * resides within this class.
 * 
 * Note:
 * Uses a hybrid approach to rendering (passive and active). Uses passive rendering
 * for switching between panelViews/"cards" (using CardLayout). Only when switching
 * to gameView is gameView's active-rendering behavior turned on. When exiting gameView and
 * returning to another card, the active-rendering of gameView is turned back off.
 * Mixing passive and active rendering is universally discouraged, but hopefully
 * no issues arise since both strategies are never used concurrently.
 */
public class SpacePotaters extends JFrame {
    
    public final static int WINDOW_WIDTH = 1024;
    public final static int WINDOW_HEIGHT = 768;
    
    private GameView gameView; //display component of game
    private MainMenu mainMenu;
    private Leaderboard leaderboard;
    private InstructionView instructions;
    private ChooseDifficulty difficulty;
    private JPanel panelViews;
    private CardLayout cLayout;
    
    /**
     * Default constructor. Initializes components and adds them to a JPanel
     * managed by a CardLayout layout manager. Contains methods for switching
     * between the different components.
     * 
     * Resource: http://docs.oracle.com/javase/tutorial/uiswing/layout/card.html
     */
    public SpacePotaters(){
        super("Space Potaters");
        Container content = getContentPane();
        
        // initialize components
        gameView = new GameView(this);
        gameView.addKeyListener(gameView);
        
        mainMenu = new MainMenu(this);
        leaderboard = new Leaderboard(this);
        instructions = new InstructionView(this);
        difficulty = new ChooseDifficulty(this);
        cLayout = new CardLayout();
        
        // add "cards" to CardLayout manager
        panelViews = new JPanel(cLayout);
        panelViews.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        panelViews.add("gameView", gameView);
        panelViews.add("mainMenu", mainMenu);
        panelViews.add("leaderboard", leaderboard);
        panelViews.add("instructions", instructions);
        panelViews.add("difficulty", difficulty);
        
        // initially display menu menu
        content.add(panelViews);
        cLayout.show(panelViews,"mainMenu");
                    
        pack();
        setResizable(false);
        
        // relocate window to center of screen
        setLocationRelativeTo(getRootPane());
    }
    
    /**
     * Switch to the GameView panel (and start game with given difficulty setting).
     * @param diffSetting the difficulty setting of the game to be started
     */
    public void switchToGameView(Difficulty diffSetting) {
        gameView.setUpGame(diffSetting);
        cLayout.show(panelViews, "gameView");
        gameView.startGame();
    }
    
    /**
     * Switch to the main menu panel.
     */
    public void switchToMainMenu(){
        cLayout.show(panelViews, "mainMenu");
    }
    
    /**
     * Switch to the leaderboard panel.
     */
    public void switchToLeaderboard(){
        cLayout.show(panelViews, "leaderboard");
    }
    
    /**
     * Switch to the instructions panel.
     */
    public void viewInstructions(){
        cLayout.show(panelViews, "instructions");
    }
    
    /**
     * Switch to the custom difficulty panel.
     */
    public void chooseDifficulty(){
        cLayout.show(panelViews, "difficulty");
    }
    
    /**
     * Return the leaderboard of Space Potaters.
     * @return the leaderboard
     */
    public Leaderboard getLeaderboard(){
        return leaderboard;
    }
    
    /**
     * Return the width of the Space Potaters window.
     * @return the width of the main window
     */
    public int getWindowWidth(){
        return WINDOW_WIDTH;
    }
    
    /**
     * Return the height of the Space Potaters window.
     * @return the height of the main window
     */
    public int getWindowHeight(){
        return WINDOW_HEIGHT;
    }
    
   /**
     * Begin the Space Potaters GUI.
     * 
     * Note: should use invokeLater( ) whenever update GUI from thread that is
     * not the UI thread (EDT).
     * 
     * Resources:
     * http://stackoverflow.com/questions/3018165/why-do-people-run-java-guis-on-the-event-queue
     * http://www.java2s.com/Code/Java/Event/UsingEventQueueinvokeLatertostartaSwingapplication.htm
     * http://stackoverflow.com/questions/3541373/should-we-use-eventqueue-invokelater-for-any-gui-update-in-java-desktop-applicat
     * 
     * @param args the command line args
     */
    public static void main(String[] args){
        
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                SpacePotaters sp = new SpacePotaters();
                 sp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                sp.setVisible(true);   
            }             
        });
    }
    
    /**
     * Different types of difficulty settings that can be used for a game.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
}
