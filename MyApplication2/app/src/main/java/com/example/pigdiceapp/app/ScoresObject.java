package com.example.pigdiceapp.app;

/**
 * Created by jsham on 6/9/14.
 */
public class ScoresObject {
    private int playerScore;
    private int computerScore;
    public ScoresObject(int p, int c){
        playerScore = p;
        computerScore = c;
    }

    @Override
    public String toString() {
        return "Player Score: "+ playerScore+ "\nComputer Score: "+computerScore;
    }
}
