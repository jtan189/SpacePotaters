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

import CSCI161.spacepotaters.gamemodel.Barrier;
import CSCI161.spacepotaters.gamemodel.BonusPotater;
import CSCI161.spacepotaters.gamemodel.GameModel;
import CSCI161.spacepotaters.gamemodel.Projectile;
import CSCI161.spacepotaters.leaderboard.Leaderboard;
import CSCI161.spacepotaters.leaderboard.Record;
import CSCI161.spacepotaters.toplevelgui.SpacePotaters;
import CSCI161.spacepotaters.toplevelgui.SpacePotaters.Difficulty;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Date;
import javax.swing.JPanel;

/**
 * The view component of the Space Potaters game. Separate from the game model/logic.
 * Contains the animator thread. This panel uses active rendering to paint to
 * the screen. It also monitors the user's keyboard input.
 * 
 * Heavy use of the following resource was used in implementing the core game animation loop:
 * Killer Game Programming in Java (KGPJ), written by Andrew Davison
 * Chapter 2 - An Animation Framework was particularly helpful. The code developed in
 * this chapter (to maintain a constant frame rate/update rate using active
 * rendering) was used as a template.
 */
public class GameView extends JPanel implements KeyListener, Runnable{
    
    /* Number of frames with a delay of 0 ms before the animation thread yields
    to other running threads. */
    private static final int NO_DELAYS_PER_YIELD = 16;
    
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered
    private static final int MAX_FRAME_SKIPS = 5;
    
    private static final int DEFAULT_FPS = 80; // default frames per second
    
    private SpacePotaters spTop; // top-level GUI
    private GameModel game; // game logic to present
    
    private int startXLivesDisplay; // x-coordinate to start lives display at
    private static final int LIVES_DISPLAY_SPACER = 5; // space between lives display elements
    
    private Thread animator; // animation loop
    private boolean gameRunning;
    private boolean gameOver;
    
    // used to double-buffer (off-screen rendering)
    private Graphics dbGraphics;
    private Image dbImage;
    
    private long period; // period of a game loop (in ns)
    
    /**
     * Constructor using default FPS.
     * @param spTop the top-level GUI contained in
     */
    public GameView(SpacePotaters spTop){
        this(spTop, 1000000000/DEFAULT_FPS); // period (ns) = 10^9/fps
    }
    
    /**
     * Constructor specifying period.
     * @param spTop the top-level GUI contained in
     * @param period the period of a game loop (in ns)
     */
    public GameView(SpacePotaters spTop, long period){
        this.spTop = spTop;
        this.period = period;

        setBackground(Color.BLACK);
        setFocusable(true);
//        addKeyListener(this);
    }
    
    /**
     * Initialize a game model to be presented and determine the placement of the
     * lives graphics based on the number of lives for the specific game.
     * @param diff the difficulty of the game to be created
     */
    public void setUpGame(Difficulty diff){
        game = new GameModel(diff, spTop.getWindowWidth(), spTop.getWindowHeight());
        startXLivesDisplay = getStartXLivesDisplay();
        
    }
    
    /**
     * Determine the starting x-coordinate of the "lives display" ("LIVES:" string
     *  + lives images) using the number of lives allotted to the player in a
     * specific game.
     * @return the starting x-coordinate of the lives display
     */
    public int getStartXLivesDisplay(){
         
        int numLivesLeft = game.getNumLivesLeft() - 1;
        int lifeSpriteWidth = SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollum.png").getWidth();
        int livesWordSpriteWidth = SpriteLibrary.getSpriteLibrary().getSprite("sprites/lives.png").getWidth();
        return game.getWindowWidth() - (livesWordSpriteWidth + (numLivesLeft * (LIVES_DISPLAY_SPACER + lifeSpriteWidth)));
    }
    
    /**
     * Draw the lives display for the number of lives left at a specific point in
     * the game.
     * @param g the graphics context to draw to 
     */
    private void drawLives(Graphics g){
        // calculate where to start
        Sprite lifeSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/gollum.png");
        Sprite livesWordSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/lives.png");
        int startY = game.getTopBoundary(); // height of lifeSprite and livesWordSprite should be the same
        int startX = startXLivesDisplay;
        
        // draw "LIVES:" image
        g.drawImage(livesWordSprite.getSpriteImage(), startX, game.getTopBoundary(), null);
        // draw lives images (image of Gollum)
        for (int i = 0; i < (game.getNumLivesLeft() - 1); i++){
            if (i == 0){
                startX += LIVES_DISPLAY_SPACER + livesWordSprite.getWidth();
            } else {
                startX += LIVES_DISPLAY_SPACER + lifeSprite.getWidth();
            }
            g.drawImage(lifeSprite.getSpriteImage(), startX, startY, null);
        }
    }
    
    /**
     * Draw the current score into the top left corner of the screen.
     * @param g the graphics context to draw to
     */
    private void drawScore(Graphics g){
        int startY = game.getTopBoundary();
        Sprite scoreSprite = SpriteLibrary.getSpriteLibrary().getSprite("sprites/score.png");
        // draw "SCORE:" image
        g.drawImage(scoreSprite.getSpriteImage(), game.getLeftBoundary(), startY, null);
        
        // draw current score
        g.setColor(Color.WHITE);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        g.drawString(Integer.toString(game.getScore()), scoreSprite.getWidth() + 10 , startY + 15);
    }
    
    /**
     * Print "GAME OVER" to the center of the screen. 
     */
    public void printGameOver(){
        Graphics context = getGraphics();
        // draw string
        if (context != null){
            context.setColor(Color.RED);
            context.setFont(new Font(Font.MONOSPACED, Font.BOLD, 72));
            context.drawString("GAME OVER", (int)(spTop.getWindowWidth() * .32) , spTop.getWindowHeight() / 2);
        }
        context.dispose();
        
        // wait a bit before continuing
        try {
            Thread.sleep(4000); // display game over screen for 4 seconds
        } catch (InterruptedException ex) {
            System.err.println("Sleep error.");
        }
    }
    
    /**
     * Start a game that has already been set up. Begins animation of the game.
     */
    public void startGame(){

        if (animator == null || !gameRunning) {
            animator = new Thread(this);
            animator.start();
            requestFocus(); // the JPanel now has focus, so receives key events
        }
    }
    
    /**
     * Stop the game loop. Will end the "update, render, sleep" cycle.
     */
    public void stopGame(){
        gameRunning = false;
    }
    
    /**
     * Resume the game from a paused state.
     */
    public void resumeGame(){
        game.setIsPaused(false);
    }
    
    /**
     * Pause the game. This freezes the game animation and any updates to the
     * game model until the game is unpaused.
     */
    public void pauseGame(){
        game.setIsPaused(true);
    }
    
    /**
     * Render the current state of the game model to the off-screen image (used
     * in double buffering).
     */
    public void gameRender(){
        
        // if the double buffer image does not exist, create one that is the
        // same width and height of the game window
        if (dbImage == null){
            dbImage = createImage(spTop.getWindowWidth(), spTop.getWindowHeight());
            if (dbImage == null){ //The return value may be null if the component is not displayable.
                System.err.println("The dbImage is null."); 
                return;
            } else {
                dbGraphics = dbImage.getGraphics();
            }
        }
        
        // clear the image (i.e. paint it all black)
        dbGraphics.setColor(Color.BLACK);
        dbGraphics.fillRect(0, 0, spTop.getWindowWidth(), spTop.getWindowHeight());
        
        // draw score
        drawScore(dbGraphics);
        
        // draw lives
        drawLives(dbGraphics);
        
        // draw components:
        
        // draw Gollum
        game.getGollum().draw(dbGraphics);
        
        // draw barriers (show before wave so potaters appear above barriers)
        for (Barrier barrier : game.getBarriers()){
            barrier.draw(dbGraphics);
        }
        
        // draw potater wave
        game.getPotaterWave().draw(dbGraphics);
        
        // draw bonus
        BonusPotater bonus = game.getBonusPotater();
        if (bonus != null){
            bonus.draw(dbGraphics);
        }
        
        // draw projectiles
        for (Projectile proj : game.getProjectiles()){
            proj.draw(dbGraphics);
        }
        
        // if game paused, show "Paused" in the center of the window
        if (game.isPaused()){
            dbGraphics.setColor(Color.WHITE);
            dbGraphics.setFont(new Font(Font.MONOSPACED, Font.BOLD, 36));
            dbGraphics.drawString("Paused", (int)(spTop.getWindowWidth() * .43) , spTop.getWindowHeight() / 2);
        }
        
    }
    
    /**
     * Paint the screen using the (already rendered) double buffer image.
     * Actively renders to screen (passive rendering would override JPanel's
     * paintComponent(Graphics g) method).

     *
     * Note: should use invokeLater( ) whenever update GUI from thread that is
     * not the UI thread (EDT). Animator thread is not the EDT.
     * 
     * Note2: using invokeLater in this method seems to break animation when run
     * on Linux.
     */
    public void paintScreen() {

//        java.awt.EventQueue.invokeLater(new Runnable() {
//
//            @Override
//            public void run() {

             //   Toolkit.getDefaultToolkit().sync();  // sync the display on some systems (source: KGPJ)


                // get fresh graphics context for GameView each time
                Graphics context = getGraphics();
                if (context != null && dbImage != null) {
                    context.drawImage(dbImage, 0, 0, null);
                    context.dispose();
                }

//            }
//        });

         Toolkit.getDefaultToolkit().sync();  // sync the display on some systems (source: KGPJ)

    }

    /**
     * Run the animator thread. This method performs four main functions:
     * 1) Update the game model
     * 2) Render the game model display to an off-screen image.
     * 3) Paint the off-screen image to the screen (double buffering).
     * 4) Sleep for a variable amount of time.
     * 
     * The method of determining the sleep time necessary to maintain a desired
     * frame rate (or update rate, if this is not possible) was heavily influenced
     * by Chapter 2 of KGPJ, as well as the WormPanel.java code developed in it.
     * 
     * After the animation ends, this method will retrieve information for a new record
     * (if necessary) and then return the player to the main menu.
     */
    @Override
    public void run() {
        gameRunning = true;
        
        long beforeTime, afterTime; // before and after loop logic
        long timeDiff; // measures time taken during update, render, and sleep
        long sleepTime; // measures actual time spent sleeping
        long oversleepTime = 0L; // measures time that have overslept
        long excessAwake = 0L; // measures excess amount of time that have not slept
        int noDelays = 0; // counts number of loops without a delay (without sleeping)
        
        beforeTime = System.nanoTime();
        
        while (gameRunning) {
            // update, render, sleep
            game.gameUpdate();
            gameRender();
            paintScreen();

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - oversleepTime;
            
            if (sleepTime > 0){ // if time left to sleep
                try {
                    Thread.sleep(sleepTime / 1000000L); //ns -> ms
                } catch (InterruptedException ex){
                    System.err.println("Sleep error.");
                }
                oversleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else { //sleepTime not positive (i.e. frame took longer than the period)
                excessAwake -= sleepTime;
                oversleepTime = 0;
                
                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();   // give another a chance to run
                    noDelays = 0;
                }
                
            }
            
            beforeTime = System.nanoTime();
            
            /* If frame animation is taking too long, update the game state
            without rendering it, to get the updates/sec nearer to
            the required FPS. */
            int skips = 0;
            while ((excessAwake > period) && (skips < MAX_FRAME_SKIPS)) { // skipping too many frames = choppy animation
                excessAwake -= period;
                game.gameUpdate();    // update state but don't render
                skips++;
            }
            
            
            
            if (game.gameIsOver()){
                // print game over on screen
                printGameOver();
                gameOver = true;
                gameRunning = false;
            }
        }
        
        // check if player achieved new record (create if has)
        Leaderboard leaderboard = spTop.getLeaderboard();
        int score = game.getScore();
        if (leaderboard.isNewRecord(score)){
            String initials = getUserInitials();
            leaderboard.addRecord(new Record(initials, score, new Date()));    
        } 
        
        // return to main menu
        spTop.switchToMainMenu();
 
    }

    /**
     * Pop up an input window to get user initials. Forces user to enter only three
     * characters.
     * @return the user's (3) initials
     * 
     * Resource: http://stackoverflow.com/questions/2620448/retrieve-input-entered-in-a-jdialog
     */
    public String getUserInitials(){
        GetInitials initialsWindow = new GetInitials(spTop, true);
        initialsWindow.setLocationRelativeTo(this); // center modal relative to parent
        initialsWindow.setVisible(true);
        // Next code will be executed when data is filled in and modal closed.
        return initialsWindow.getEnteredInitials();
    }
    
    /**
     * Return the game model associated with this game view.
     * @return the game model
     */
    public GameModel getGame(){
        return game;
    }
    
    /**
     * If 'p' key is typed, pause the game.
     * @param e the KeyEvent for typing a key
     */
    @Override
    public void keyTyped(KeyEvent e) {
        if (!gameOver){
            if (e.getKeyChar() == 'p'){
                game.togglePause();
            }
        }
    }

    /**
     * If left, right, or space is pressed, then set the respective flags within the game
     * model. Also, terminate the game if the user presses the escape key.
     * @param e the KeyEvent for pressing a key
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                game.setLeftPressed(true);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                game.setRightPressed(true);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.setFirePressed(true);
            } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE){
                game.terminateGame();
            }
        }
    }

    /**
     * If left, right, or space is released, then unset the respective flags within the game
     * model.
     * @param e the KeyEvent for releasing a key
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                game.setLeftPressed(false);
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                game.setRightPressed(false);
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                game.setFirePressed(false);
            }
        }
    }
    
}
