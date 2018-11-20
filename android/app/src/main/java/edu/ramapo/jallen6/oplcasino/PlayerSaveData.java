package edu.ramapo.jallen6.oplcasino;

public class PlayerSaveData {
    private String name;
    private int score;
    private String hand;
    private String pile;

    PlayerSaveData(){
        name = "";
        score = 0;
        hand = "";
        pile = "";
    }

    /**
     Create the object with all fields specified
     @param playerName The name of the player
     @param currentScore The score of the player
     @param playerHand The player's hand
     @param playerPile The player's pile
     */
    PlayerSaveData(String playerName, int currentScore, String playerHand, String playerPile){
        name = playerName;
        score = currentScore;
        hand = playerHand;
        pile = playerPile;
    }

    public String getName(){
        return name;
    }
    public int getScore(){
        return score;
    }
    public String getHand(){
        return hand;
    }
    public String getPile(){
        return pile;
    }

    public void setName(String n){
        name = n;
    }
    public void setScore (int s){
        score = s;
    }

    public void setHand (String h){
        hand = h;
    }

    public void setPile (String p){
        pile = p;
    }


}
