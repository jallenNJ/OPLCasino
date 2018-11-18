package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Build extends BuildType {
    private Vector<Card> cards;

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

    public Vector<Card> getCards(){
        return new Vector<Card>(cards);
    }
    public Card[] getCardsAsArray(){
        //Iterate through the vector and put them into an array
        Card[] returnVal = new Card[cards.size()];
        for(int i =0; i < cards.size(); i++){
            returnVal [i] = new Card(cards.get(i));
        }
        return returnVal;
    }

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
