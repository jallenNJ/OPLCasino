package edu.ramapo.jallen6.oplcasino;

public class Round {
    private int roundNum = 0;
    private PlayerID startingPlayer;
    private Deck deck;

    Round(){
        roundNum = 0;
        startingPlayer = PlayerID.humanPlayer;
        deck = new Deck();
    }
    Round(int round, boolean humanFirst){
        roundNum = round;
        if(humanFirst){
            startingPlayer = PlayerID.humanPlayer;
        } else{
            startingPlayer = PlayerID.computerPlayer;
        }
        deck = new Deck();
        playRound();
    }

    public void playRound(){
        Deck deck = new Deck();
        Hand test = new Hand();
        deck.dealFourCardsToHand(test);
        HandView testView = new HandView(test);
    }


}
