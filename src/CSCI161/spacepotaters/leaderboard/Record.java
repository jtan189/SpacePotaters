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

import java.io.Serializable;
import java.util.Date;

/**
 * A record for the Space Potaters game. A record is achieved if a score places
 * in the top 10 ever achieved (among those records recorded).
 */
public class Record implements Serializable {
    
    private String userInitials; // three letters
    private int score;
    private Date date; // date record achieved
    
    /**
     * Default constructor.
     * @param initials the initials of the person who achieved the record
     * @param score the score associated with the record
     * @param date the date the record was achieved
     */
    public Record(String initials, int score, Date date){
        userInitials = initials;
        this.score = score;
        this.date = date;
    }
    
    /**
     * Return the score of the record.
     * @return the record's score
     */
    public int getScore(){
        return score;
    }
    
    /**
     * Return the initials of the record-scorer.
     * @return the record-scorer's initials
     */
    public String getInitials(){
        return userInitials;
    }
    
    /**
     * Return the date the record was achieved.
     * @return the date of the record
     */
    public Date getDate(){
        return date;
    }
    
}