package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Round {
    private int roundNum = 0;
    private PlayerID startingPlayer;
    private Deck deck;

    private Hand table;
    private Player[] players;

    private Vector<PlayerID> moveQueue;

    private PlayerMove lastMove;

    final int humanID = PlayerID.humanPlayer.ordinal();
    final int compID = PlayerID.computerPlayer.ordinal();

    private PlayerID lastPlayerMove;
    private PlayerID lastCapturer;
    private boolean roundOver;

    Round() {
        roundNum = 0;
        startingPlayer = PlayerID.humanPlayer;
        initRound();
    }

    Round(int round, boolean humanFirst) {
        roundNum = round;
        if (humanFirst) {
            startingPlayer = PlayerID.humanPlayer;
        } else {
            startingPlayer = PlayerID.computerPlayer;
        }
        initRound();
    }

    public PlayerActions getLastAction() {
        if (lastMove == null) {
            return PlayerActions.Invalid;
        }
        return lastMove.getAction();
    }

    private void initRound() {
        roundOver = false;
        players = new Player[2];

        deck = new Deck();


        players[humanID] = new Human();
        players[humanID].addCardsToHand(deck.getFourCards());


        players[compID] = new Computer();
        players[compID].addCardsToHand(deck.getFourCards());


        table = new Hand(false);
        deck.dealFourCardsToHand(table);


        moveQueue = new Vector<PlayerID>(2, 1);
        lastCapturer = PlayerID.humanPlayer;
        lastPlayerMove = PlayerID.computerPlayer;
        lastMove = null;
        fillMoveQueue(startingPlayer);
    }

    public Player[] getPlayers() {
        return players;
    }

    public PlayerID getLastCapturer(){
        return lastCapturer;
    }

    public Hand getTable() {
        return table;
    }

    public Deck getDeck() {
        return deck;
    }


    public void generateHelpTip(){
        Computer advisor = new Computer(players[humanID]);
        PlayerMove advice = advisor.doMove(table);
        advisor.addMoveToLog(advice, table);
    }


    private void fillMoveQueue(PlayerID start) {
        PlayerID other;
        if (start == PlayerID.humanPlayer) {
            other = PlayerID.computerPlayer;
        } else {
            other = PlayerID.humanPlayer;
        }

        if (players[start.ordinal()].getHandSize() > 0) {
            moveQueue.add(start);
        }

        if (players[other.ordinal()].getHandSize() > 0) {
            moveQueue.add(other);
        }
    }


    public boolean isRoundOver() {
        return roundOver;
    }

    public Vector<Integer> findMatchingIndexOnTable() {
        int playedIndex = players[moveQueue.firstElement().ordinal()].getSelectedIndex();
        Vector<Integer> allMatching = new Vector<Integer>(2, 1);
        if (playedIndex < 0) {
            return allMatching;
        }
        int playedValue = players[moveQueue.firstElement().ordinal()].getHand()
                .peekCard(playedIndex).getValue();
        for (int i = 0; i < table.size(); i++) {
            if (table.peekCard(i).getValue() == playedValue) {
                allMatching.add(i);
            }
        }
        return allMatching;

    }

    public void setMoveActionForCurrentPlayer(PlayerActions pass) {
        players[moveQueue.get(0).ordinal()].setMoveToUse(pass);
    }

    public int getSelectedTableSize(){
        return table.getAmountSelect();
    }
    public boolean doNextPlayerMove() {
        if (moveQueue.size() == 0) {
            fillMoveQueue(startingPlayer);
            if (moveQueue.size() == 0) {
                roundOver = true;
                return true;
            }
        }

        PlayerID currentId = moveQueue.get(0);
        int index = moveQueue.get(0).ordinal();

        PlayerMove result;
        result = doPlayerMove(index);
        if (!validateMove(result, index)) {
            //TODO: Make sure user re prompts instead of continuing on.
            players[index].addMoveToLog(result, table);
            return false;
        }
        moveQueue.remove(0);

        players[index].addMoveToLog(result, table);
        Vector<Integer> indices = result.getTableCardIndices();
        switch (result.getAction()){

            case Trail:
                table.addCard(players[index].removeCardFromHand(result.getHandCardIndex()));
                break;

            case Capture:
                lastCapturer = currentId;
                if(players[index].hasReservedValue()){

                    players[index].releaseBuildValue(players[index].getHand().
                                peekCard(result.getHandCardIndex()));
                }
                players[index].addCardToPile(players[index].removeCardFromHand(result.getHandCardIndex()));
                //Player move indices are sorted descending, therefore can iterate normally

                for (int i = 0; i < result.getTableCardIndiciesSize(); i++) {
                    //TODO: Check cast option when builds are added
                    //TODO: FIX NULL POINTER; something to do with reserved build values and move being rejected
                    //  possibly not being cleared
                    if(table.peekCard((indices.get(i))).getSuit() == CardSuit.build){
                     //   players[index].addCardsToPile((Card[]) (((Build)table.removeCard(indices.get(i))).getCards().toArray()));
                        players[index].addCardsToPile( (((Build)table.removeCard(indices.get(i))).getCardsAsArray()));
                    } else{
                        players[index].addCardToPile((Card) table.removeCard(indices.get(i)));
                    }

                }
                break;
            case Build:
                Vector<Card> buildCards = new Vector<Card>(5,1);
                buildCards.add((Card)players[index].getHand().removeCard(result.getHandCardIndex()));
                for(int i =0; i < indices.size(); i++){
                    buildCards.add((Card)table.removeCard(indices.get(i)));
                }
                Build newBuild = new Build(buildCards);
                table.addCard(newBuild);
                players[index].reserveBuildValue(newBuild);






             break;



        }



        lastMove = new PlayerMove(result);


        if (players[humanID].getHandSize() == 0 && players[compID].getHandSize() == 0) {
            if (deck.size() >= 8) {
                players[humanID].addCardsToHand(deck.getFourCards());
                players[compID].addCardsToHand(deck.getFourCards());
            } else {
                giveCardsToLastCapturer();
                return true;
            }

        }

        if (moveQueue.size() > 0) {
            return true;
        } else {
            fillMoveQueue(startingPlayer);
            return true;
        }

        //return  false;
    }

    public PlayerMove getLastPlayerMove() {
        return lastMove;
    }

    private PlayerMove doPlayerMove(int playerId) {
        PlayerMove result = players[playerId].doMove(table);
        if (validateMove(result, playerId)) {
            return result;
        } else {
            result.markInvalid();
            return result;
        }


    }


    private boolean validateMove(PlayerMove move, int playerID) {
        PlayerActions action = move.getAction();

        if (action == PlayerActions.Capture) {
            return checkCapture(move, playerID);
        } else if (action == PlayerActions.Build) {
            return checkBuild(move, playerID);
        } else if (action == PlayerActions.Trail) {
            return checkTrail(move, playerID);
        } else {
            return false;
        }
    }

    private boolean checkCapture(PlayerMove move, int playerID) {
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();
        Vector<Integer> selected = move.getTableCardIndices();
        if (selected.size() == 0) {
            return false;
        }
        for (int i = 0; i < selected.size(); i++) {
            if (table.peekCard(selected.get(i)).getValue() != playedValue) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBuild(PlayerMove move, int playerID){
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();
        if(players[playerID].isReservedValue(playedValue)){
            return false;
        }
        Vector<Integer> selected = move.getTableCardIndices();
        if (selected.size() == 0) {
            return false;
        }

        int sum = playedValue;
        for (int i = 0; i < selected.size(); i++) {
            sum += table.peekCard(selected.get(i)).getValue();
        }
        if(sum > 14){
            return false;
        }
        return players[playerID].doesHandContain(sum);

    }

    private boolean checkTrail(PlayerMove move, int playerID) {
        return !players[playerID].hasReservedValue();
        //return true;
    }


    private void giveCardsToLastCapturer() {
        for (int i = table.size() - 1; i >= 0; i--) {
            //TODO: Check case works with builds;
            players[lastCapturer.ordinal()].addCardToPile((Card) table.removeCard(i));
        }
    }


    public void serializeRoundState(){
        Serializer.setRoundNum(0);
        //Serializer.setComputerPlayer(p);
        Serializer.setPlayers(players);
        Serializer.setDeck(deck.toString());
        Serializer.setTable(table.toString());
        Serializer.setLastCapturer(players[lastCapturer.ordinal()].getName());
        Serializer.setNextPlayer(players[moveQueue.get(0).ordinal()].getName());
    }

}