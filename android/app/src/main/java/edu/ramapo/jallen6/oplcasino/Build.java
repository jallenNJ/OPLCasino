package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Build extends BuildType {

    //All the cards in the build
    private Vector<Card> cards;

    /**
     Creates a build with cards and who owns it
     @param inputCards, all the cards to add into the build
     @param ownerName, The name of the owner
     */
    Build(Vector<Card> inputCards, String ownerName){
        //Add all the cards which were passed in
        cards = new Vector<Card>(4,1);
        cards.addAll(inputCards);

        //Sum the cards, and set the value
        int sum = 0;
        for(int i =0; i < cards.size(); i++){
            sum += cards.get(i).getValue();
        }
        numericValue = sum;

        //Mark the suit as build and record the owner
        suit = CardSuit.build;
        owner = ownerName;

    }

    /**
     Get a copy of the cards in the build
     @return A copy of the internal vector
     */
    public Vector<Card> getCards(){
        return new Vector<Card>(cards);
    }

    /**
     Get all the cards in the build as an array instead of as a vector
     @return Returns the cards as an array.
     */
    public Card[] getCardsAsArray(){
        //Iterate through the vector and put them into an array
        Card[] returnVal = new Card[cards.size()];
        for(int i =0; i < cards.size(); i++){
            returnVal [i] = new Card(cards.get(i));
        }
        return returnVal;
    }

    /**
     Overload of the toString for custom data

     @return Returns a string in form [ card1 card2 cardN ]
     */
    public String toString(){
        //Add leading bracket
        String formatted = "[ ";

        //To string each card and space separate them
        for(int i=0; i < cards.size(); i++){
            formatted += cards.get(i).toString() + " ";
        }
        //Add the closing bracket
        formatted += "]";
        return formatted;


    }

}
