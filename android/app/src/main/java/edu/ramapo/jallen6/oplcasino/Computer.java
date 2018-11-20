package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Computer extends Player {

    Computer(){
        super();
        name = "Computer";

        if(Serializer.isFileLoaded()){
            //If a save, get the save data and pass to all the member objects
            PlayerSaveData saveData = Serializer.getComputerSaveData();
            score = saveData.getScore();
            hand = new Hand(true, saveData.getHand());
            pile = new Hand(false, saveData.getPile());
        }
    }

    /**
     Create an advisor to a player.
     @param advisee The player object to copy its data and give a recommendation
     */
    Computer(Player advisee){
        //super();
        //If the advisor to the human, copy their data
        name = "Advisor";
        hand = new Hand(advisee.getHand());
        pile = new Hand(advisee.getPile());

    }

    /**
     Check to find a build oppertunity for the AI
     @param table the cards on the table
     @return the move for the computer to make. Action is invalid if no move is available.
     */
    private PlayerMove checkBuildOptions(final Hand table){
        //Check every card in the hand as the sum to
        for(int i = 0; i < hand.size(); i ++){
            //Store in a local var
            Card sumTo = (Card)hand.peekCard(i);
            //Check every other card to see if a build can be made
            for(int j =0; j < hand.size(); j++){
                //If same card, skip
                if(i==j){
                    continue;
                }
                //Store the card in a local var
                Card played = (Card)hand.peekCard(j);
                //If the card is reserved, skip it
                if(reservedValues.contains(played.getValue())){
                    continue;
                }
                //If this card value exceeds what summing to, skip
                if(played.getValue() >= sumTo.getValue()){
                    continue;
                }

                //Check if any sets exist for these two cards
                Vector<Integer> indices = findSetThatSum(table,
                        sumTo.getValue() - played.getValue(), true);

                //If one does, return the move
                if(indices.size() > 0){
                    actionReason = "Saw an opportunity to make a build";
                    return new PlayerMove(PlayerActions.Build, j, indices);
                }
            }
        }

        //If reached this point, no valid moves
        return new PlayerMove(PlayerActions.Invalid, 0, new Vector<Integer>(1,1));
    }

    /**
     Check the table for any valid moves
     @param table The cards on the table
     @return The move to make, action is Invalid if no moves can be made
     */
    private PlayerMove checkCaptureOptions(final Hand table){

        //For every card
        for(int i =0; i < hand.size(); i++){
            //See if any sets summed
            Vector<Integer> result;
            if(hand.peekCard(i).getValue() != 1){
               result = findSetThatSum(table, hand.peekCard(i).getValue(), false);
            } else{
                result = findSetThatSum(table, 14, false);
            }

            //If any were found, capture the set
            if(result.size() != 0){
                actionReason = "As it was an opportunity to capture a set.";
                //Before capturing, ensure all required cards are selected
                for(int j =0; j < table.size(); j++){
                    if(table.peekCard(j).getValue() == hand.peekCard(i).getValue()){
                        if(!result.contains(new Integer(j))){
                            result.add(j);
                        }
                    }
                }
                return new PlayerMove(PlayerActions.Capture, i, result);
            }
        }

        //If no sets, check for any identical options

        Vector<Integer> options = new Vector<>(4,1);
        //For ever card
        for(int i =0; i < hand.size(); i++){
            //Get the value and clear the vector
            int target = hand.peekCard(i).getValue();
            options.clear();
            //For every card on the table
            for(int j=0; j < table.size(); j++){
                //If it matches, add the card
                if(table.peekCard(j).getValue() == target){
                    options.add(j);
                }
            }
            //If any found, capture them
            if(options.size() > 0){
                actionReason = "As it was an opportunity to capture.";
                return new PlayerMove(PlayerActions.Capture, i, options);
            }


        }

        //If this point is reached, no valid captures
        return new PlayerMove(PlayerActions.Invalid, 0, new Vector<Integer>(1,1));
    }


    /**
     Entry point to the Computer to have the AI generate its move
     @param table The cards on the table
     @return The player move to make. Will always return a valid move, unless it has no cards.

        Checks for moves in order of:
            Build Opportunities (Make / Extend )
            Capture Sets
            Capture Identical / Builds
            Trail
     */
    public PlayerMove doMove(final Hand table) {

        //See if any build options and make it if it exists
        PlayerMove potentialMove;
        potentialMove = checkBuildOptions(table);
        if(potentialMove.getAction() == PlayerActions.Build){
            return potentialMove;
        }

        //If not, check for captures
        potentialMove = checkCaptureOptions(table);
        if(potentialMove.getAction() == PlayerActions.Capture){
            return potentialMove;
        }


        //If neither are options, trail the 0th card
        actionReason = "As there are no other moves.";
        return  new PlayerMove(PlayerActions.Trail, 0, new Vector<Integer>(1,1));


    }

    /**
     Find a set of cards that sum to value up to size 3
     @param table The cards on the table
     @param target The target value to sum to
     @param forBuild If this set is being used to capture or build
     @return Vector of indices of table cards. If no sets, returns vector of size 0;
     */
    private Vector<Integer> findSetThatSum(final Hand table, int target, boolean forBuild){

        Vector<Integer> indices = new Vector<Integer>(4,1);
        //If target is outside of valid bounds
        if(target < 1 || target > 14) {
            return indices;
        }

        //For every card on table
        for(int i =0; i < table.size(); i++){
            //Clear past loop, and peek the current card
            indices.clear();
            CardType start = table.peekCard(i);

            //If not making a build, skip over any builds as they cannot be used in a set capture
            if(!forBuild){
                if(start.getSuit() == CardSuit.build){
                    continue;
                }
            }

            //Difference between target value and current
            int remaining = target - start.getValue();
            //Exceeded limit, skip card
            if(remaining < 0){
                continue;
            }

            //Else add the card
            indices.add(i);

            //If match value
            if(remaining == 0){

                //Return if making build, skip if doing set capture
                if(forBuild){
                    return indices;
                } else{
                    continue;
                }
            }

            //Store the remaining in a cache so it can be reset in the inner loop
            int remainingCache = remaining;

            //For every card after it
            for(int j = i+1; j < table.size(); j++){
                //Reset to starting value
                remaining = remainingCache;
                //Store the card for readability
                CardType firstCard = table.peekCard(j);

                //If not for a build skip over
                if(!forBuild){
                    if(firstCard.getSuit() == CardSuit.build){
                        continue;
                    }
                }
                //If match perfectly, return
                if(remaining - firstCard.getValue() == 0){
                    indices.add(j);
                    return indices;
                }else if(remaining - firstCard.getValue() <0){ //If exceed, skip
                    continue;
                }

                //Decrement by value of card
                remaining -= firstCard.getValue();
                //Repeat for one more card, starting to the card to the right
                for(int k = j+1; k < table.size(); k++){

                    CardType current = table.peekCard(k);
                    //Skip over build in set capture
                    if(!forBuild){
                        if(current.getSuit() == CardSuit.build){
                            continue;
                        }
                    }
                    //If match, add, otherwise skip
                    if(remaining - current.getValue() == 0){
                        indices.add(k);
                        return indices;
                    }
                }
            }
        }

        //Must be for a set capture at this point
        //Therefore min size is 2, so if found is below that, clear and return empty.
        if(indices.size() < 2){
            indices.clear();
        }
        return indices;
    }
}
