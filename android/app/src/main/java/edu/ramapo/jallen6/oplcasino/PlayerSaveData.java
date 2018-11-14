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
