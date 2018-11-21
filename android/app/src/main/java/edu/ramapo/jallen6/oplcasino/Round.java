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
        int[] scores = {0,0};
        initRound(scores);
    }

    /**
     Creates a round with specified parameters
     @param round The current roundNum of the tour
     @param humanFirst If the Human is going first
     @param scores  Array of size 2 of current scores. [0] is human, [1] is computer
     */
    Round(int round, boolean humanFirst, int[] scores) {
        roundNum = round;
        if (humanFirst) {
            startingPlayer = PlayerID.humanPlayer;
        } else {
            startingPlayer = PlayerID.computerPlayer;
        }

        initRound(scores);
    }

    /**
     Get the last action that was done
     @return The action performed. Invalid if none were made
     */
    public PlayerActions getLastAction() {
        if (lastMove == null) {
            return PlayerActions.Invalid;
        }
        return lastMove.getAction();
    }


    /**
     Inits the round with the specifed scores, and will read parsed save data
     @param scores Array of size 2 of current scores. [0] is human, [1] is computer
     @return True if valid, false if not
     */
    private void initRound(int[] scores) {
        roundOver = false;
        players = new Player[amountOfPlayers];
        deck = new Deck();

        //Create the player objects
        players[humanID] = new Human();
        players[compID] = new Computer();
        players[humanID].setScore(scores[humanID]);
        players[compID].setScore(scores[compID]);

        //If no file is loaded, generate a fresh round
        if(!Serializer.isFileLoaded()){
            players[humanID].addCardsToHand(deck.getFourCards());
            players[compID].addCardsToHand(deck.getFourCards());
            table = new Hand(false);
            deck.dealFourCardsToHand(table);
            lastCapturer = PlayerID.humanPlayer;
            lastPlayerMove = PlayerID.computerPlayer;
        }else{
            //Pass the parsed data to objects which need to be told its a save file
            table = new Hand( Serializer.getTable());
            if(Serializer.isLastCapturerHuman()){
                lastCapturer = PlayerID.humanPlayer;
            } else{
                lastCapturer = PlayerID.computerPlayer;
            }
        }

        //Create and fill the move queue
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


    /**
     Has the advisor create a recomendation for the human player
     */
    public void generateHelpTip(){

        //Create advisor and get recommendation
        Computer advisor = new Computer(players[humanID]);
        PlayerMove advice = advisor.doMove(table);
        //Print to log
        advisor.addMoveToLog(advice, table);
    }


    /**
        Fill the move queue to two players in the correct order for a cycle
     @param start The player id of who goes first
     @return True if valid, false if not
     */
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

    /**
     Find all indices which are the same symbol
     @return Vector of all the indices, otherwise an empty vector
     */
    public Vector<Integer> findMatchingIndexOnTable() {
        int playedIndex = players[moveQueue.firstElement().ordinal()].getSelectedIndex();
        Vector<Integer> allMatching = new Vector<Integer>(2, 1);
        //If invalid index, return empty
        if (playedIndex < 0) {
            return allMatching;
        }

        //Get the played value
        int playedValue = players[moveQueue.firstElement().ordinal()].getHand()
                .peekCard(playedIndex).getValue();

        //Check for all cards that match on table
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

    /**
     Have the next player in the queue take their turn
     @return True if move completed, or false if invalid
     */
    public boolean doNextPlayerMove() {
        //If queue is empty, refill
        if (moveQueue.size() == 0) {
            fillMoveQueue(startingPlayer);
            //If still empty, round is over
            if (moveQueue.size() == 0) {
                roundOver = true;
                return true;
            }
        }


        //Get the id of the first player
        PlayerID currentId = moveQueue.get(0);
        int index = moveQueue.get(0).ordinal();


        //Have a player take their turn
        PlayerMove result;
        result = doPlayerMove(index);

        //If invalid
        if (!validateMove(result, index)) {
            //Log the failure and return the fail
            players[index].addMoveToLog(result, table);
            return false;
        }

        //Remove the player from the queue
        moveQueue.remove(0);

        //Add the success to the log
        players[index].addMoveToLog(result, table);
        Vector<Integer> indices = result.getTableCardIndices();

        //Handle move based on the action
        switch (result.getAction()){

            case Trail:
                //Remove from hand and add to table
                table.addCard(players[index].removeCardFromHand(result.getHandCardIndex()));
                break;

            case Capture:
                //Mark player as current id
                lastCapturer = currentId;

                //For all players, release the value
                for(int i=0; i < players.length ; i++) {
                    if (players[i].hasReservedValue()) {

                        players[i].releaseBuildValue(players[i].getHand().
                                peekCard(result.getHandCardIndex()));
                    }
                }


                players[index].addCardToPile(players[index].removeCardFromHand(result.getHandCardIndex()));
                //Player move indices are sorted descending, therefore can iterate normally

                //For all the selected indices
                for (int i = 0; i < result.getTableCardIndiciesSize(); i++) {

                    //Cast for the correct function
                    if(table.peekCard((indices.get(i))).getSuit() == build){
                        players[index].addCardsToPile( (((BuildType)table.removeCard(indices.get(i))).getCardsAsArray()));
                    } else{
                        players[index].addCardToPile((Card) table.removeCard(indices.get(i)));
                    }

                }
                break;
            case Build:

                //Get all the cards for the build
                Vector<Card> buildCards = new Vector<Card>(5,1);
                buildCards.add((Card)players[index].getHand().removeCard(result.getHandCardIndex()));
                //If this is true, a build is being extended
                if(result.getTableCardIndices().size() == 1 &&
                        table.peekCard(result.getTableCardIndices().get(0)).getSuit() == build){
                    //TODO: unreserve card
                    Vector<Card> extendedCards= ((Build) table.removeCard(result.getTableCardIndices().get(0))).getCards();
                    buildCards.addAll(extendedCards);


                } else{
                    //Add the cards from the table
                    for(int i =0; i < indices.size(); i++){
                        buildCards.add((Card)table.removeCard(indices.get(i)));
                    }
                }

                //Add build to the table and reserve the value in its owner
                Build newBuild = new Build(buildCards, players[index].getName());
                table.addCard(newBuild);
                players[index].reserveBuildValue(newBuild);

             break;

        }

        lastMove = new PlayerMove(result);


        //If hands are empty
        if (players[humanID].getHandSize() == 0 && players[compID].getHandSize() == 0) {
            //And enough cards to deal
            if (deck.size() >= 8) {
                //Deal cards
                players[humanID].addCardsToHand(deck.getFourCards());
                players[compID].addCardsToHand(deck.getFourCards());
            } else {
                //Give all cards to the last player to capture
                giveCardsToLastCapturer();
                roundOver = true;
                return true;
            }

        }


        //Fill the queue if its empty
        if (moveQueue.size() > 0) {
            return true;
        } else {
            fillMoveQueue(getOtherPlayerId(currentId));
            return true;
        }

    }

    public PlayerMove getLastPlayerMove() {
        return lastMove;
    }

    /**
        Have specifed player do their move
     @param playerId The id of the player so it can be accessed in the array
     @return The Move which was done. Action is invalid if its invalid
     */
    private PlayerMove doPlayerMove(int playerId) {
        //Get the move, and check if its valid
        PlayerMove result = players[playerId].doMove(table);
        if (validateMove(result, playerId)) {
            return result;
        } else {
            result.markInvalid();
            return result;
        }

    }


    /**
     * Takes any separate builds of same value and puts them into a multi build
     * @param targetVal the value of the builds to look for
     * @return The removed table indices, sorted descending,  size 0 if none
     */
    public Vector<Integer> condenseBuilds(int targetVal){
        //Initialize the values
        Vector<Build> matchingBuilds = new Vector<Build>(2,1);
        Vector<Integer> matchingIndices = new Vector<Integer>(2,1);

        //Find all builds with the same value
        for(int i =0; i < table.size(); i++){
            if(table.peekCard(i).getSuit() == build &&table.peekCard(i).getValue() == targetVal){
                matchingBuilds.addElement((Build) table.peekCard(i));
                matchingIndices.add(i);
            }
        }

        //If less than two match, clear
        if(matchingBuilds.size() < 2){
            matchingIndices.clear();
            return matchingIndices;
        }

        //Condense the builds into a multi build
        MultiBuild newMulti = new MultiBuild(matchingBuilds, matchingBuilds.get(0).getOwner());
        //Sort descending
        Collections.sort(matchingIndices, Collections.<Integer>reverseOrder());
        //Remove the cards
        for(int i =0; i < matchingIndices.size(); i++){
            table.removeCard(matchingIndices.get(i));
        }
        // Add new build and return
        table.addCard(newMulti);
        return matchingIndices;

    }


    /**
     * Checks if the player move is valid for the player
     * @param move The move being checked
     * @param playerID Id of the player
     * @return True if valid, false if not
     */
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


    /**
     * Check if a capture move was valid
     * @param move  The move being checked
     * @param playerID The id of the player being checked
     * @return True if valid, false if not
     *
     * Worked with Andrew Wild who came up with the *amazing* idea of using modulus to validate.
     */
    private boolean checkCapture(PlayerMove move, int playerID) {
        //Get the played value
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();

        //If ace low, make ace high
        if(playedValue == 1){
            playedValue = 14;
        }

        Vector<Integer> selected = move.getTableCardIndices();
        //If nothing selected, capture is invalid
        if (selected.size() == 0) {
            players[playerID].setRejectionReason("Did not select cards to capture");
            return false;
        }

        int aceCount = 0;
        int sum =0;
        Vector<Integer> valuesToRelease = new Vector<Integer>(1,1);
        for(int i =0; i < selected.size();i++){

            CardType current = table.peekCard(selected.get(i));
            //Add the value, and if ace increment ace count
            sum += current.getValue();
            if(current.getValue() == 1){
                aceCount++;
            }
            //If build and not equal sum, not a valid capture per game rules
            if(current.getSuit() == build){
                if(current.getValue() != playedValue){
                    players[playerID].setRejectionReason("Trying to use a build as part of a set");
                    return false;
                } else{
                    valuesToRelease.add(current.getValue());
                }

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

        //Valid if either matches as is, or the aceSets matched
        boolean moveValidity =  sum % playedValue == 0 || aceSetsCheck;
        if(!moveValidity){
            players[playerID].setRejectionReason("Not all selected cards are a matching symbol" +
                    " or a set that sum to target value");
        }else{
            for(int i =0; i < valuesToRelease.size(); i++){
                for(int j = 0; j < players.length; j++){
                    players[j].releaseBuildValue(valuesToRelease.get(i));
                }
            }
        }
        return moveValidity;

    }

    /**
     *  Check to see if a build move is valid
     * @param move The move being checked
     * @param playerID The Id of the player being checked
     * @return True if valid, false if not
     */
    private boolean checkBuild(PlayerMove move, int playerID){
        //Get the played value and check if its reserved, deny if it is
        int playedValue = players[playerID].getHand().peekCard(move.getHandCardIndex()).getValue();
        if(players[playerID].isReservedValue(playedValue)){
            players[playerID].setRejectionReason("Tried to use a card that a build sums to");
            return false;
        }

        //If no selected cards, reject as impossible
        Vector<Integer> selected = move.getTableCardIndices();
        if (selected.size() == 0) {
            players[playerID].setRejectionReason("Did not select any cards to build with");
            return false;
        }

        //Get the sum
        int sum = playedValue;
        for (int i = 0; i < selected.size(); i++) {
            sum += table.peekCard(selected.get(i)).getValue();
        }

        //If exceed's ace high value, always invalid
        if(sum > 14){
            players[playerID].setRejectionReason("Selected cards sum over 14");
            return false;
        }


        //If hand contains value, its valid
        boolean moveValidity = players[playerID].doesHandContain(sum);

        if(!moveValidity){
            players[playerID].setRejectionReason("Build sum to" + Integer.toString(sum)
                    + " which is not a value in the hand");
        }
        return moveValidity;

    }


    /**
     *  Check to see if a trail move is valid
     * @param move The move being checked
     * @param playerID the Id of the player in the array
     * @return True if valid, false if not
     */
    private boolean checkTrail(PlayerMove move, int playerID) {
        //TODO: Check identical
        //If reserved value, not allowed to trail
        boolean moveValidity =  !players[playerID].hasReservedValue();

        if(!moveValidity){
            players[playerID].setRejectionReason("Trying to trail while having a build");
        }
        return moveValidity;
    }


    /**
     * Gives the card to the player who captured last
     */
    private void giveCardsToLastCapturer() {

        ActionLog.addLog("Remaining table cards went to " + players[lastCapturer.ordinal()].getName());
        for (int i = table.size() - 1; i >= 0; i--) {
            //Builds will never be the last card on the table so cast is save
            players[lastCapturer.ordinal()].addCardToPile((Card) table.removeCard(i));
        }
    }

    /**
     * Have all objects that the round manages write their save data to the Serializer
     */
    public void serializeRoundState(){
        Serializer.setRoundNum(0);
        Serializer.setPlayers(players);
        Serializer.setDeck(deck.toString());
        Serializer.setTable(table.toString());
        Serializer.setBuildOwners(table.serilizeBuilds());
        Serializer.setLastCapturer(players[lastCapturer.ordinal()].getName());
        Serializer.setNextPlayer(players[moveQueue.get(0).ordinal()].getName());
    }

    /**
     * Get the other player id from the given one
     * @param current the ID number of the current player
     * @return The PlayerID of the other player
     */
    private PlayerID getOtherPlayerId(PlayerID current){
        if(current == PlayerID.humanPlayer){
            return PlayerID.computerPlayer;
        }else{
            return PlayerID.humanPlayer;
        }
    }

}