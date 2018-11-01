package edu.ramapo.jallen6.oplcasino;

public class Round {
    private int roundNum = 0;
    private PlayerID startingPlayer;
    private Deck deck;
    HandView testView;
    HandView testView2;

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
        Hand test2 = new Hand();
        deck.dealFourCardsToHand(test);
        deck.dealFourCardsToHand(test2);
        testView = new HandView(test, true);
        testView.createViewsFromModel();
        testView2 = new HandView(test2, true);
        testView2.createViewsFromModel();
    }

    public HandView getHumanHandHandler(){
        //This should be replaced with the view
        // in the humanHand once that is implemented
        return testView;
    }

    public HandView getComputerHandHandler(){
        //This should be replaced with the view
        // in the compHand once that is implemented
        return testView2;
    }


}
