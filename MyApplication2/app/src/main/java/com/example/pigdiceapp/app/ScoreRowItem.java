package com.example.pigdiceapp.app;

/**
 * Created by jsham on 6/10/14.
 */
public class ScoreRowItem {
    private String playerText;
    private String compText;
    private int playerValue;
    private int compValue;

    public ScoreRowItem(String player, String comp, int playerValue, int compValue){
        playerText=player;
        compText=comp;
        this.playerValue = playerValue;
        this.compValue = compValue;
    }

    public String getPlayerText() {
        return playerText;
    }

    public String getCompText() {
        return compText;
    }

    public int getPlayerValue() {
        return playerValue;
    }

    public int getCompValue() {
        return compValue;
    }
}
