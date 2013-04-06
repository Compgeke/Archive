package kenta.game.logic;

import kenta.game.object.Player;

/**
 *
 * @author Kevin Nash
 */
public class Score {
    public static final int SCORE_PER_SECOND_LEFT = 10, SCORE_PER_LIFE_LEFT = 250, SCORE_PER_PURSE = 100, 
            TOTAL_PURSE_SCORE = 2000;
    private int totalScore = 0, timeScore, lifeScore, purseReductions = 0;
    private boolean reset;
    
    public boolean shouldReset(){
        return reset;
    }
    
    public void setReset(boolean b){
        reset = b;
    }
    
    public int getTotalScore(){
        return totalScore + getCurrentScore();
    }
    
    public int getCurrentScore(){
        return timeScore + lifeScore + TOTAL_PURSE_SCORE - purseReductions;
    }
    
    public void resetCurrentScore(){
        timeScore = 0;
        lifeScore = 0;
        purseReductions = 0;
    }
    
    public void resetTotalScore(){
        totalScore = 0;
    }
    
    public void resetAll(){
        resetCurrentScore();
        resetTotalScore();
    }
    
    public void updateTotalScore(){
        totalScore += getCurrentScore();
    }
    
    public void adjustPurse(){
        if(purseReductions < TOTAL_PURSE_SCORE){
            purseReductions += SCORE_PER_PURSE;
        }
    }
    
    public void adjustTime(int time){
        timeScore = SCORE_PER_SECOND_LEFT * time;
    }
    
    public void adjustLife(){
        lifeScore = SCORE_PER_LIFE_LEFT * Player.getLife();
    }
}
