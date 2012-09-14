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

package CSCI161.spacepotaters.leaderboard;

import CSCI161.spacepotaters.toplevelgui.SpacePotaters;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.text.DateFormat;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;

/**
 * The leaderboard for the Space Potaters game. Contains a list of record holders.
 */
public class Leaderboard extends JPanel{
    
    private static final String LEADERBOARD_IMAGE_LOCATION = "leaderboardImages/leaderboardBackground.png";
    private static final String DEFAULT_SAVE_LOCATION = "leaderboard.sp";
    private static final String PLACEHOLDERS_FILE = "placeholders.txt";
    
    private Image leaderboardBackground;
    private JButton returnButton;
    
    private ArrayList<Record> records;
    private SpacePotaters spTop; // top-level GUI

    /**
     * Default constructor.
     * @param top the top-level GUI
     */
    public Leaderboard(SpacePotaters top){
        
        setLayout(null);
        setFocusable(true);
        spTop = top;
        
        if (!loadFromFile()){ // if loading not successful, sub in placeholders
            copyPlaceholders();
        }

        try {
            
            // create URL for resource (look for file in same package class is in)
            URL url = this.getClass().getResource(LEADERBOARD_IMAGE_LOCATION);
            
            // if can't find URL, then print error and exit game
            if (url == null){
                System.err.println("Can't find reference: " + LEADERBOARD_IMAGE_LOCATION);
                System.exit(0);
            }
            
            // read image in
            leaderboardBackground = ImageIO.read(url);
            
        } catch (IOException e){
            System.err.println("Can't find reference: " + LEADERBOARD_IMAGE_LOCATION);
            System.exit(0);
        }
        
        // add a return button
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
     * Paint the leaderboard stats to the graphics context. This paints the 
     * record holders over the background image.
     * @param g the graphics context
     */
    public void paintLeaders(Graphics g){
        
        // set up font
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        
        int startY = (int)(spTop.getWindowHeight() * .325);
        int verticalSpacing = (int)(spTop.getWindowHeight() * .0652);
        for (int i = 0; i < 10; i ++){
            int startX = (int)(spTop.getWindowWidth() * .13);
            // print rank
            g.drawString(Integer.toString(i + 1), startX, startY);
            // print name
            startX += (int)(spTop.getWindowWidth() * .13);
            g.drawString(records.get(i).getInitials(), startX, startY);
            // print score
            startX += (int)(spTop.getWindowWidth() * .225);
            g.drawString(Integer.toString(records.get(i).getScore()), startX, startY);
            // print date
            startX += (int)(spTop.getWindowWidth() * .225);
            g.drawString(DateFormat.getDateInstance(DateFormat.SHORT).format(records.get(i).getDate()), startX, startY);
            // move Y location
            startY += verticalSpacing;
        }
    }
    
    /**
     * Copy the placeholder records from placeholder file to the game leaderboard.
     */
    private void copyPlaceholders() {
        records = new ArrayList<Record>(10);
        Scanner scan = new Scanner(this.getClass().getResourceAsStream(PLACEHOLDERS_FILE));

        for (int i = 0; i < 10; i++) {
            String recordString = scan.nextLine();
            String inititals = recordString.substring(0, 3);
            int score = Integer.parseInt(recordString.substring(4));
            records.add(new Record(inititals, score, new Date()));
        }
    }
    
    /**
     * Load the leaderboard information from the leaderboard file.
     * 
     * Resources for serialization code:
     * http://www.devx.com/Java/Article/9931/1954
     * http://www.javadb.com/reading-objects-from-file-using-objectinputstream
     *
     * @return true if load was successful
     */
    private boolean loadFromFile() {
        boolean loadSuccessful = true;
        File saveFile = new File(DEFAULT_SAVE_LOCATION);
        if (saveFile.exists()) {

            try {

                // Read from disk using FileInputStream.
                FileInputStream fileStream = new FileInputStream(saveFile);

                // Read object using ObjectInputStream.
                ObjectInputStream objectStream = new ObjectInputStream(fileStream);
                Object obj = objectStream.readObject();

                // Perform appropriate casting.
                if (obj instanceof ArrayList) {
                    records = (ArrayList<Record>) obj;
                }

                objectStream.close();
                
            } catch (IOException e) {
                loadSuccessful = false;
            } catch (ClassNotFoundException e) {
                loadSuccessful = false;
            }

        } else { //file does not exist, substitute placeholders

            loadSuccessful = false;
        }
        return loadSuccessful;
    }


    /**
     * Save the leaderboard information from to the leaderboard file.
     * 
     * Resources for serialization code:
     * http://www.devx.com/Java/Article/9931/1954
     * http://www.javadb.com/reading-objects-from-file-using-objectinputstream
     *
     * @return true if save was successful
     */
    private boolean saveToFile() {
        boolean saveSuccessful = true;

        FileOutputStream fileOutput = null;
        ObjectOutputStream objectOutput = null;

        try {
            
            fileOutput = new FileOutputStream(DEFAULT_SAVE_LOCATION);
            objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(records);
            
        } catch (IOException e) {
            saveSuccessful = false;
        }
        return saveSuccessful;
    }
    
    /**
     * Determine if the player score is a new record.
     * @param playerScore the player score to check
     * @return true if score constitutes a new record
     */
    public boolean isNewRecord(int playerScore){
        // only need to check last element; records should always be full and sorted
        if (playerScore > records.get(records.size() - 1).getScore()){
            return true;
        }
        return false;
    }
    
    /**
     * Add a new record to the leaderboard. Inserts the record into array so that
     * correct ordering (descending score value) is maintained.
     * @param newRecord the record to add
     */
    public void addRecord(Record newRecord){
        records.remove(9); // know it will kick #10 out
        int newRecordScore = newRecord.getScore();
        int oneBefore;
        for (oneBefore = 8; oneBefore >= 0; oneBefore--) { 
            if (records.get(oneBefore).getScore() >= newRecordScore){
                break; // quite looping; found place
            }
        }
        
        records.add(oneBefore+1, newRecord);
        saveToFile();
    }

    /**
     * Paint the component by drawing the background image and then painting
     * the leaders over the background.
     * @param g the graphics context to paint to
     */
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(leaderboardBackground, 0, 0, spTop.getWindowWidth(), spTop.getWindowHeight(), null);
        paintLeaders(g);
    }
    
}
