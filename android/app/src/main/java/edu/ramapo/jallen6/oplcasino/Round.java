package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Round {
    private int roundNum = 0;
    private PlayerID startingPlayer;
    private Deck deck;
    private HandView tableView;
    private Hand table;
    private Player[] players;
    private PlayerView[] playerViews;
    private Vector<PlayerID> moveQueue;
    private PlayerMove lastMove;

    final int humanID = PlayerID.humanPlayer.ordinal();
    final int compID = PlayerID.computerPlayer.ordinal();

    private PlayerID lastPlayerMove;
    private PlayerID lastCapturer;
    private boolean roundOver;

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
        roundOver = false;
        players = new Player[2];
        playerViews = new PlayerView[2];
        deck = new Deck();

        players[humanID]= new Human();
        players[humanID].addCardsToHand(deck.getFourCards());
        playerViews[humanID] = new PlayerView(players[humanID]);

        players[compID]= new Computer();
        players[compID].addCardsToHand(deck.getFourCards());
        playerViews[compID] = new PlayerView(players[compID]);

        table = new Hand();
        deck.dealFourCardsToHand(table);
        tableView = new HandView(table, false);

        moveQueue = new Vector<PlayerID>(2,1);
        lastCapturer = PlayerID.humanPlayer;
        lastPlayerMove = PlayerID.computerPlayer;
        lastMove = null;
        fillMoveQueue(startingPlayer);
    }
    private void fillMoveQueue(PlayerID start){
        PlayerID other;
        if(start == PlayerID.humanPlayer){
            other = PlayerID.computerPlayer;
        }else{
            other = PlayerID.humanPlayer;
        }

        if(players[start.ordinal()].getHandSize() > 0){
            moveQueue.add(start);
        }

        if(players[other.ordinal()].getHandSize() > 0){
            moveQueue.add(other);
        }
    }

    public HandView getHumanHandHandler(){
        return playerViews[0].getHand();
    }

    public HandView getComputerHandHandler(){
       return playerViews[1].getHand();
    }

    public HandView getTableHandHandler(){
        return tableView;
    }

    public void updateViews(){
       // playerViews[0] = new PlayerView(players[0]);
        playerViews[0].getHand().createViewsFromModel();
        playerViews[1].getHand().createViewsFromModel();
        //playerViews[1] = new PlayerView(players[1]);
    }

    public boolean isRoundOver(){
        return roundOver;
    }

    public Vector<Integer> findMatchingIndexOnTable(){
        int playedIndex = players[moveQueue.firstElement().ordinal()].getSelectedIndex();
        Vector<Integer> allMatching = new Vector<Integer>(2,1);
        if(playedIndex < 0 ){
            return allMatching;
        }
        int playedValue = players[moveQueue.firstElement().ordinal()].getHand()
                .peekCard(playedIndex).getValue();
        for(int i =0; i < table.size();i++){
            if(table.peekCard(i).getValue() == playedValue){
                allMatching.add(i);
            }
        }
        return allMatching;

    }

    public boolean doNextPlayerMove(){
        if(moveQueue.size() == 0){
            fillMoveQueue(startingPlayer);
            if(moveQueue.size() == 0){
                roundOver = true;
                return  true;
            }
        }

        PlayerID first = moveQueue.get(0);
        int index = moveQueue.remove(0).ordinal();

        PlayerMove result;
        result = doPlayerMove(index);
        if(!validateMove(result, index)){
            //TODO: Make sure user re prompts instead of continuing on.
            return false;
        }


       // tableView.addCard(playerViews[index].removeCardFromHand(result.getHandCardIndex()));
        table.addCard(players[index].removeCardFromHand(result.getHandCardIndex()));
       // tableView.addCard(playerViews[index].)
        lastMove = new PlayerMove(result);


        if(players[humanID].getHandSize() == 0 && players[compID].getHandSize() == 0){
            players[humanID].addCardsToHand(deck.getFourCards());
            players[compID].addCardsToHand(deck.getFourCards());
        }

        if(moveQueue.size() > 0){
            return true;
        }else{
            fillMoveQueue(startingPlayer);
            return true;
        }

        //return  false;
    }

    public PlayerMove getLastPlayerMove(){
        return lastMove;
    }
    private PlayerMove doPlayerMove(int playerId){
        while(true){
            PlayerMove result = players[playerId].doMove();
            if(validateMove(result, playerId) ){
                return result;
            }
        }

    }


    private boolean validateMove(PlayerMove move, int playerID){
        PlayerActions action = move.getAction();

        if(action == PlayerActions.Capture){
            return false;
        } else if(action == PlayerActions.Build){
            return false;
        } else if(action == PlayerActions.Trail){
            return checkTrail(move, playerID);
        } else{
            return false;
        }
    }


    private boolean checkTrail(PlayerMove move, int playerID){
        return true;
    }
}
