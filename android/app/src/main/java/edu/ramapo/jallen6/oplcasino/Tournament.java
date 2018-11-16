package edu.ramapo.jallen6.oplcasino;

public class Tournament {


    private  int[] roundScores;
    private  int[] tourScores;

    private String[] names;
    private int[] startingScores;
    private int[] pendingScores;
    private int[] pileSizes ;
    private int[] spadeAmounts ;
    private boolean[] hasDX ;
    private boolean[] hasS2;
    private int[] aceAmount;


    Tournament(){
        initVars();
    }

    Tournament(Player[] players){
        initVars();
        scoreRound(players);
    }

    private void initVars(){

        roundScores = new int[2];
        tourScores = new int [2];

        names = new String[2];
        startingScores = new int [2];
        pendingScores = new int[2];
        pileSizes = new int[2];
        spadeAmounts = new int[2];
        hasDX = new boolean[2];
        hasS2 = new boolean[2];
        aceAmount = new int[2];

    }


    public int [] getRoundScores(){
        return roundScores;
    }

    public int[] getTourScores(){
        return tourScores;
    }

    public int[] getPileSizesScore(){
        return pileSizes;
    }


    public  void scoreRound(Player[] players){


        for(int i=0; i < 2; i++){
            names[i] = players[i].getName();
            startingScores[i] = players[i].getScore();
            pendingScores[i] = 0;
            pileSizes[i] = players[i].getPileSize();
            spadeAmounts[i] = players[i].countSpadesInPile();
            hasDX[i] = players[i].containsCardInPile(new Card(CardSuit.diamond, 10));
            hasS2[i] = players[i].containsCardInPile(new Card(CardSuit.spade, 2));
            aceAmount[i] = players[i].countAcesInPile();

        }


        if(pileSizes[0] > pileSizes[1]){
            pendingScores[0] +=3;
        } else if(pileSizes[0] < pileSizes[1]){
            pendingScores[1] +=3;
        }

        if(spadeAmounts[0] > spadeAmounts[1]){
            pendingScores[0] +=1;
        } else if(spadeAmounts[0] < spadeAmounts[1]){
            pendingScores[1] +=1;
        }

        if(hasDX[0]){
            pendingScores[0] +=2;
        } else{
            pendingScores[1] +=2;
        }

        if(hasS2[0]){
            pendingScores[0]+=1;
        }else{
            pendingScores[1] +=1;
        }

        pendingScores[0] += aceAmount[0];
        pendingScores[1] += aceAmount[1];


        roundScores = pendingScores;

        tourScores[0] = roundScores[0] + startingScores[0];
        tourScores[1] = roundScores[1] + startingScores[1];
    }


    public TourScoreCode getWinner(){
        if(tourScores[0] > 20 && tourScores[0] > tourScores[1]){
            return TourScoreCode.HumanWon;
        } else if(tourScores[1] > 20 && tourScores[0] < tourScores[1]){
            return TourScoreCode.ComputerWon;
        } else if(tourScores[0] > 20 && tourScores[0] == tourScores[1]){
            return TourScoreCode.Tie;
        } else{
            return TourScoreCode.NoWinner;
        }
    }

    public String toString(){
        String formatted = "Raw Score dump:\n\n";


        if(pileSizes[0] > pileSizes[1]){
           formatted += names[0] + " had more cards ";
        } else if(pileSizes[0] < pileSizes[1]){
            formatted += names[1] + " had more cards ";
        }

        formatted += "( " + names[0] + ": " + Integer.toString(pileSizes[0])
                +  " | " + names[1] + ": " + Integer.toString(pileSizes[1]) + "\n\n";

        if(spadeAmounts[0] > spadeAmounts[1]){
            formatted += names[0] + " had more spades ";
        } else if(spadeAmounts[0] < spadeAmounts[1]){
            formatted += names[1] + " had more spades ";
        }

        formatted += "( " + names[0] + ": " + Integer.toString(spadeAmounts[0])
                +  " | " + names[1] + ": " + Integer.toString(spadeAmounts[1]) + "\n\n";

        if(hasDX[0]){
           formatted += names[0] + " had the ten of Diamonds\n\n";
        } else{
            formatted += names[1] + " had the ten of Diamonds\n\n";
        }

        if(hasS2[0]){
            formatted += names[0] + " had the two of Spades\n\n";
        }else{
            formatted += names[1] + " had the two of Spades\n\n";
        }

        formatted += names[0] + " had " + Integer.toString(aceAmount[0]) + " aces\n";
        formatted += names[1] + " had " + Integer.toString(aceAmount[1]) + " aces\n";



        return formatted;
    }
}
