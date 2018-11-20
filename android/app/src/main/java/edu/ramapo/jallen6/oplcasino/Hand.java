package edu.ramapo.jallen6.oplcasino;


import java.util.Observable;
import java.util.Vector;

public class Hand extends Observable {

    private Vector<CardType> hand;
    private Vector<Integer> selectedIndices;
    private boolean selectionLimitedToOne;
    private int removedIndex;

    /**
     Create the new object with selection limited to one
     */
    Hand(){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = true;
        selectedIndices = new Vector<Integer>(1,1);

    }

    /**
     Creates the deck with selection limited to true or false
     @param limitSelection if selection should be limited to one card only
     */
    Hand(boolean limitSelection){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
        selectedIndices = new Vector<Integer>(4,4);
    }

    /**
     Creates deck with selectionLimited specified and with save data
     @param limitSelection If selection should be limited
     @param data The data which to be read in
     */
    Hand(boolean limitSelection, String data){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
        selectedIndices = new Vector<Integer>(4,4);

        //Tokenize the data
        String[] tokens = data.split(" ");
        for(String token:tokens){
            //Make sure the token is valid size
            if(token.length() != 2){
                continue;
            }
            //If it is, add
            hand.add(new Card(token));
        }

    }

    /**
     Create hand with save data and selection limited
     @param data The save data
     */
    Hand(String data){

        //Init the member vars
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = false;
        selectedIndices = new Vector<Integer>(4,4);

        //Tokenize the data
        String[] tokens = data.split(" ");

        //Remove the spaces
        String spacesRemoved = "";
        for(String token:tokens){
            if(token.length() == 0){
                continue;
            }
            spacesRemoved += token;
        }


        //Init vars
        int buildsCount = 0; //Amount of pending builds
        int i =0; //The index in the string

        //All build buffers
        Vector<Card> cardBuffer = new Vector<Card>(4,1);
        Vector<Build> buildBuffer = new Vector<Build>(4,1);
        //Iterate through the string
        while(i< spacesRemoved.length()){

            //Get the current char
            char current = spacesRemoved.charAt(i);
            //If a new build is beginning
            if(current == '['){
                //Increment appropriate counters and continue
                buildsCount++;
                i++;
                continue;
            }else if(current ==']'){ //If build is ending
                if(cardBuffer.size() > 0){
                    //If cards in buffer, add them to the build
                    buildBuffer.add(new Build(new Vector<Card>(cardBuffer), "ADD OWNER"));
                    cardBuffer.clear();
                }

                //Update counters
                buildsCount--;
                i++;

                //If no builds remaining
                if(buildsCount == 0){
                    //And there is more than one build in the bugffer
                    if(buildBuffer.size() >1){
                        //Put all pending builds into a multi build
                        MultiBuild multiBuild = new MultiBuild(new Vector<Build>(buildBuffer),
                                "ADD OWNER");
                        //Get its owner from save file
                        multiBuild.setOwner(Serializer.getOwnerOfBuild(multiBuild.toString()));
                        //Clear buffer and add to table
                        buildBuffer.clear();
                        hand.add(multiBuild);
                    } else{ // Add the single build to the table
                        hand.add(buildBuffer.get(0));
                        //Get owner of the lone build
                        buildBuffer.get(0).setOwner(
                                Serializer.getOwnerOfBuild(buildBuffer.get(0).toString()));
                        buildBuffer.clear();
                    }

                }
                //Then go to next character (i already incremented)
                continue;

            }

            //Else must be a card
            //Get the current character and the one next to it
            String cardStr = "" + current + spacesRemoved.charAt(i+1);
            //Make a card with the string
            Card newCard = new Card(cardStr);
            //If any pending builds, add to card buffer
            if(buildsCount > 0){
                cardBuffer.add(newCard);
            }else{
                //Otherwise added directly to the table
                hand.add(newCard);
            }
            //Increment counter
            i+=2;
        }


    }

    /**
     Copy Constructor
     @param copy The hand object to copy
     */
    Hand(Hand copy){
        //Call copy constructor on complex objects
        hand = new Vector<CardType>(copy.hand);
        selectedIndices = new Vector<Integer>(copy.selectedIndices);
        //Copy the primitives
        selectionLimitedToOne = copy.selectionLimitedToOne;
        removedIndex = copy.removedIndex;
    }

    public int size(){
        return hand.size();
    }

    public boolean isEmpty(){
        return hand.size() == 0;
    }

    public boolean hasSelectedCards(){
        return selectedIndices.size() > 0;
    }

    /**
     Get the list of indices which are selected
     @return A copy of Vector of selected Indices. Null if vector is null
     */
    public Vector<Integer> getSelectedIndices(){
        if(selectedIndices == null){
            return null;
        }
        if(selectedIndices.size() == 0){
            return new Vector<Integer>(1,1);
        }
       return new Vector<Integer>(selectedIndices);
    }

    /**
     Select card at the index
     @param index the index of the card to select
     @return The index to deselect if selection limited. -1 if nothing to deselect
     */
    public int selectCard(int index){

        //If selection limited and something is already selected
        if(selectionLimitedToOne && selectedIndices.size() >0){

            //Replace the value
            int replacedVal = selectedIndices.remove(0);
            selectedIndices.clear();
            //If it different, add the new one
            if(replacedVal != index){
                selectedIndices.add(index);
                return replacedVal;
            } else{
                //Otherwise do nothing (deselect it)
                return -1;
            }

        } else{
            //If it is not already selected, select it
            if(!selectedIndices.contains(index)){
                selectedIndices.add(index);
            }

            return -1;
        }
    }

    public void unselectCard(int index){
        selectedIndices.remove(index);
    }

    public int getAmountSelect(){
        return selectedIndices.size();
    }

    public void unSelectAllCards(){

        selectedIndices.clear();
    }

    /**
     Add CardType to the hand
     @param add The CardType to add
     @return always true
     */
    public boolean addCard(CardType add){
        //Add the card
        hand.add(add);
        //Notify Observer
        this.setChanged();
        this.notifyObservers();
        return true;
    }

    /**
     Get a copy of specified card
     @param index the Index of the card to copy
     @return The CardType at the index, or null if invalid
     */
    public CardType peekCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }

        return hand.get(index);
    }

    /**
     Get the last removed index from the hand and clears the storage on read
     @return The last removed index.  -1 if no change
     */
    public  int fetchRemovedIndex(){
        int returnVal = removedIndex;
        removedIndex = -1;
        return returnVal;
    }

    /**
     Remove the card from the hand and return copy of the card
     @param index the index of the CardType to remove
     @return CardType which was removed. Null if nothing removed
     */
    public CardType removeCard(int index){
        // Make sure the index is valid
        if(index >= hand.size() || index < 0){
            return null;
        }
        //Get a copy of the ith card
        CardType removed = hand.get(index);
        //Remove from hand and store the removed index
        hand.remove(index);
        removedIndex = index;
        //Notify observer
        this.setChanged();
        this.notifyObservers();

        //Return the copy
        return removed;
    }

    /**
     Gets all cards in the hand space separated in a string
     @return Formatted string, empty string if hand is empty
     */
    public String toString(){
        String formatted = "";
        //Call toString on each index, space separated
        for(int i =0; i < hand.size(); i++){
            formatted += hand.get(i).toString() + " ";
        }
        return formatted;
    }

    /**
     Count how many times a specified suit appears in the hand
     @param target The Suit to count
     @return Amount of occurrences in the hand. 0 if none
     */
    public int countSuit (CardSuit target){
        int amount = 0;
        //Linear search through the hand
        for(int i=0; i < hand.size(); i++){
            //If suit matches, increment the count
            if(hand.get(i).getSuit() == target){
                amount++;
            }
        }
        return amount;
    }

    public boolean containsCard (Card check){
        return hand.contains(check);
    }


    /**
     Count the amount of time a values appear in the hand
     @param target the value to look for
     @return Amount of occurrences in the hand. 0 if none
     */
    public int countValue (int target){
        int amount = 0;
        //Linear search through the hand and increment on match
        for(int i=0; i < hand.size(); i++){
            if(hand.get(i).getValue() == target){
                amount++;
            }
        }
        return amount;
    }

    /**
     Serialize all builds in the hand
     @return Vector of strings of all builds toStrings + owner. Empty Vector if none
     */
    public Vector<String> serilizeBuilds(){
        //Allocate space
       Vector<String> buildStrings = new Vector<String>(2,1);

       //Iterate through the hand
       for(int i = 0; i < hand.size(); i++){

           CardType current = hand.get(i);
           //If current is a build
           if(current.getSuit() == CardSuit.build){
               //Add to vector
               buildStrings.add(current.toString() + current.getOwner());
           }
       }

       return buildStrings;



    }

}
