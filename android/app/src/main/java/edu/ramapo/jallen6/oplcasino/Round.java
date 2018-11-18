package edu.ramapo.jallen6.oplcasino;


import java.util.Collections;
import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.CardSuit.build;

public class Round {
    private final int amountOfPlayers = 2;
    private int roundNum;
    private PlayerID startingPlayer;
    private Deck deck;

    private Hand table;
    private Player[] players;

    private Vector<PlayerID> moveQueue;

    private PlayerMove lastMove;

    private final int humanID = PlayerID.humanPlayer.ordinal();
    private final int compID = PlayerID.computerPlayer.ordinal();

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
        players = new Player[amountOfPlayers];

        deck = new Deck();


        players[humanID] = new Human();
        players[compID] = new Computer();

        if(!Serializer.isFileLoaded()){
            players[humanID].addCardsToHand(deck.getFourCards());
            players[compID].addCardsToHand(deck.getFourCards());
            table = new Hand(false);
            deck.dealFourCardsToHand(table);
            lastCapturer = PlayerID.humanPlayer;
            lastPlayerMove = PlayerID.computerPlayer;
        }else{
            table = new Hand( Serializer.getTable());
            if(Serializer.isLastCapturerHuman()){
                lastCapturer = PlayerID.humanPlayer;
            } else{
                lastCapturer = PlayerID.computerPlayer;
            }
        }


        moveQueue = new Vector<PlayerID>(2, 1);

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

                for(int i=0; i < players.length ; i++) {
                    if (players[i].hasReservedValue()) {

                        players[i].releaseBuildValue(players[i].getHand().
                                peekCard(result.getHandCardIndex()));
                    }
                }
                /*if(players[index].hasReservedValue()){

                    players[index].releaseBuildValue(players[index].getHand().
                                peekCard(result.getHandCardIndex()));
                }*/
                players[index].addCardToPile(players[index].removeCardFromHand(result.getHandCardIndex()));
                //Player move indices are sorted descending, therefore can iterate normally

                for (int i = 0; i < result.getTableCardIndiciesSize(); i++) {
                    //TODO: Check cast option when builds are added
                    //TODO: FIX NULL POINTER; something to do with reserved build values and move being rejected
                    //TODO: Also check to make sure capturing other builds releases correctly
                    //  possibly not being cleared
                    if(table.peekCard((indices.get(i))).getSuit() == build){
                     //   players[index].addCardsToPile((Card[]) (((Build)table.removeCard(indices.get(i))).getCards().toArray()));
                        players[index].addCardsToPile( (((BuildType)table.removeCard(indices.get(i))).getCardsAsArray()));
                    } else{
                        players[index].addCardToPile((Card) table.removeCard(indices.get(i)));
                    }

                }
                break;
            case Build:
                //TODO: Change this to make multibuilds
                Vector<Card> buildCards = new Vector<Card>(5,1);
                buildCards.add((Card)players[index].getHand().removeCard(result.getHandCardIndex()));
                if(result.getTableCardIndices().size() == 1 &&
                        table.peekCard(result.getTableCardIndices().get(0)).getSuit() == build){
                    //TODO: unreserve card
                    Vector<Card> extendedCards= ((Build) table.removeCard(result.getTableCardIndices().get(0))).getCards();
                    buildCards.addAll(extendedCards);


                } else{
                    for(int i =0; i < indices.size(); i++){
                        buildCards.add((Card)table.removeCard(indices.get(i)));
                    }
                }

                Build newBuild = new Build(buildCards, players[index].getName());
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
                roundOver = true;
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


    public Vector<Integer> condenseBuilds(int targetVal){
        Vector<Build> matchingBuilds = new Vector<Build>(2,1);
        Vector<Integer> matchingIndices = new Vector<Integer>(2,1);

        for(int i =0; i < table.size(); i++){
            if(table.peekCard(i).getSuit() == build &&table.peekCard(i).getValue() == targetVal){
                matchingBuilds.addElement((Build) table.peekCard(i));
                matchingIndices.add(i);
            }
        }

        if(matchingBuilds.size() < 2){
            matchingIndices.clear();
            return matchingIndices;
        }

        MultiBuild newMulti = new MultiBuild(matchingBuilds, matchingBuilds.get(0).getOwner());
        Collections.sort(matchingIndices, Collections.<Integer>reverseOrder());
        for(int i =0; i < matchingIndices.size(); i++){
            table.removeCard(i);
        }
        table.addCard(newMulti);
        return matchingIndices;

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

    //Worked with AW
    private boolean checkCapture(PlayerMove move, int playerID) {
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();
        if(playedValue == 1){
            playedValue = 14;
        }
        Vector<Integer> selected = move.getTableCardIndices();
        if (selected.size() == 0) {
            players[playerID].setRejectionReason("Did not select cards to capture");
            return false;
        }

        int aceCount = 0;
        int sum =0;
        for(int i =0; i < selected.size();i++){
            CardType current = table.peekCard(selected.get(i));
            sum += current.getValue();
            if(current.getValue() == 1){
                aceCount++;
            }
            if(current.getSuit() == build && current.getValue() != playedValue){
                players[playerID].setRejectionReason("Trying to use a build as part of a set");
                return false;
            }
        }

        //If played card is ace, check all combinations of aces are
        // in sets or being captured for being identical
        boolean aceSetsCheck = false;
        int aceSumCheck = sum;
        if(playedValue == 14){ // If ace
            for(int i =0; i < aceCount; i++){
                //Add thirteen as that is the difference between Ace High and Low
                aceSumCheck += 13;
                aceSetsCheck = aceSumCheck % playedValue == 0;
                if(aceSetsCheck){
                    break;
                }

            }
        }


        boolean moveValidity =  sum % playedValue == 0 || aceSetsCheck;
        if(!moveValidity){
            players[playerID].setRejectionReason("Not all selected cards are a matching symbol" +
                    " or a set that sum to target value");
        }
        return moveValidity;
        /*
        for (int i = 0; i < selected.size(); i++) {
            if (table.peekCard(selected.get(i)).getValue() != playedValue) {
                return false;
            }
        }
        return true;*/
    }

    private boolean checkBuild(PlayerMove move, int playerID){
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();
        if(players[playerID].isReservedValue(playedValue)){
            players[playerID].setRejectionReason("Tried to use a card that a build sums to");
            return false;
        }
        Vector<Integer> selected = move.getTableCardIndices();
        if (selected.size() == 0) {
            players[playerID].setRejectionReason("Did not select any cards to build with");
            return false;
        }

        int sum = playedValue;
        for (int i = 0; i < selected.size(); i++) {
            sum += table.peekCard(selected.get(i)).getValue();
        }
        if(sum > 14){
            players[playerID].setRejectionReason("Selected cards sum over 14");
            return false;
        }


        boolean moveValidity = players[playerID].doesHandContain(sum);

        if(!moveValidity){
            players[playerID].setRejectionReason("Build sum to" + Integer.toString(sum)
                    + " which is not a value in the hand");
        }
        return moveValidity;

    }

    private boolean checkTrail(PlayerMove move, int playerID) {
        //TODO: Check identical
        boolean moveValidity =  !players[playerID].hasReservedValue();

        if(!moveValidity){
            players[playerID].setRejectionReason("Trying to trail while having a build");
        }
        return moveValidity;
    }


    private void giveCardsToLastCapturer() {

        ActionLog.addLog("Remaining table cards went to " + players[lastCapturer.ordinal()].getName());
        for (int i = table.size() - 1; i >= 0; i--) {
            //TODO: Check case works with builds;
            players[lastCapturer.ordinal()].addCardToPile((Card) table.removeCard(i));
        }
    }


    public void serializeRoundState(){
        Serializer.setRoundNum(0);
        Serializer.setPlayers(players);
        Serializer.setDeck(deck.toString());
        Serializer.setTable(table.toString());
        Serializer.setLastCapturer(players[lastCapturer.ordinal()].getName());
        Serializer.setNextPlayer(players[moveQueue.get(0).ordinal()].getName());
    }

}