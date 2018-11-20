package edu.ramapo.jallen6.oplcasino;



import java.util.Collections;
import java.util.Observable;
import java.util.Vector;

public class Deck extends Observable {
    //All cards in the deck
    private Vector<Card> deck;


    /**
     Creates deck for both loading in a file and a new deck
     */
    Deck(){
        //Declare the vector
        deck = new Vector<Card>(52, 5);

        //Load save file if it exists, else generate a new deck
        if(Serializer.isFileLoaded()){
            loadSavedDeck();
        } else{
            makeNewDeck();
        }

    }

    /**
     Creates a new deck of 52 cards, shuffle randomly
     */
    private void makeNewDeck(){
        //Ensure it is empty
        deck.clear();

        //For every card suit
        for(CardSuit i : CardSuit.values() ){
            //If invalid or build enum, skip over it
            if(i == CardSuit.invalid || i == CardSuit.build){
                continue;
            }
            //Make cards from 1-13
            for(int j = 1; j < 14; j++){
                deck.add(new Card(i, j));
            }
        }
        //Shuffle the vector
        Collections.shuffle(deck);

    }

    /**
     Load in a save file from the deck
     */
    private void loadSavedDeck(){
        //Get the string from the serializer and tokenize it
        String deckStr = Serializer.getDeck();
        String[] tokens = deckStr.split(" ");

        //For every token
        for(String token:tokens){
            //If invalid length, skip it
            if (token.length() != 2){
                continue;
            }
            //Else make and add card to the deck
            deck.add(new Card(token));
        }

    }


    public boolean isEmpty(){
        return deck.size() == 0;
    }

    /**
     Draw one card from the top of the deck
     @return Card on top of the deck. Null if deck is empty
     */
    private Card drawCard(){
        if(isEmpty()){
            return null;
        }
        //Copy, and remove from vector
        Card returnCard = deck.firstElement();
        deck.remove(0);
        return returnCard;
    }

    /**
     Deals for cards into the hand that is passed in
     @param target The hand object to recieve the four cards
     */
    public void dealFourCardsToHand(Hand target){
        //Draw four cards into the hand
        //Hand is responsible for rejecting NULL value
        for(int i = 0; i < 4; i++){
            target.addCard(drawCard());
        }
        //Update the deck observer
        this.setChanged();
        this.notifyObservers();
        return;
    }

    /**
     Get four cards from the top of the deck as an array
     @return Array of four cards. If cards can't be drawn, null is put at index
     */
    public Card[] getFourCards(){
        //Put four cards into the array
        Card[] returnVal = new Card[4];
        for(int i = 0;i < 4; i++ ){
            returnVal[i] = drawCard();
        }
        //Update observer
        this.setChanged();
        this.notifyObservers();
        return returnVal;
    }

    public int size(){
        return deck.size();
    }

    /**
     Get a copy of the card at index
     @param index index of card to copy
     @return Copy of card at location, null if index is invalid
     */
    public Card peekCard(int index){
        //Make sure index is in bounds of the array
        if(index < 0 || index >= deck.size()){
            return null;
        }
        return deck.get(index);
    }


    /**
     Goes through every card and calls its toString with a space between them
     @return The formatted string. If no cards, returns the empty string
     */
    public String toString(){
        String formatted = "";
        for(int i =0; i < deck.size(); i++){
            formatted += deck.get(i).toString() + " ";
        }
        return formatted;
    }

}
