package edu.ramapo.jallen6.oplcasino;

public class Tournament {




    public static int[] scoreRound(Player[] players){
        int[] startingScores = new int [2];
        int[] pendingScores = new int[2];
        int[] pileSizes = new int[2];
        int[] spadeAmounts = new int[2];
        boolean[] hasDX = new boolean[2];
        boolean[] hasS2 = new boolean[2];
        int[] aceAmount = new int[2];


        for(int i=0; i < 2; i++){
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



        int[] returnArr = new int[2];

        returnArr[0] = pendingScores[0] + startingScores[0];
        returnArr[1] = pendingScores[1] + startingScores[1];

        return returnArr;
    }

}
