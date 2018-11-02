package edu.ramapo.jallen6.oplcasino;

public class Round {
    private int roundNum = 0;
    private PlayerID startingPlayer;
    private Deck deck;
    private HandView testView;
    private HandView testView2;
    private Player[] players;
    private PlayerView[] playerViews;
    final int humanID = PlayerID.humanPlayer.ordinal();
    final int compID = PlayerID.computerPlayer.ordinal();

    Round(){
        roundNum = 0;
        startingPlayer = PlayerID.humanPlayer;
        initRound();
    }
    Round(int round, boolean humanFirst){
        roundNum = round;
        if(humanFirst){
            startingPlayer = PlayerID.humanPlayer;
        } else{
            startingPlayer = PlayerID.computerPlayer;
        }
       initRound();
    }

    private void initRound(){
        players = new Player[2];
        playerViews = new PlayerView[2];
        deck = new Deck();

        players[humanID]= new Human();
        players[humanID].addCardsToHand(deck.getFourCards());
        playerViews[humanID] = new PlayerView(players[humanID]);

        players[compID]= new Human();
        players[compID].addCardsToHand(deck.getFourCards());
        playerViews[compID] = new PlayerView(players[compID]);

    }


    public HandView getHumanHandHandler(){
        return playerViews[0].getHand();
    }

    public HandView getComputerHandHandler(){
       return playerViews[1].getHand();
    }


}
