package edu.ramapo.jallen6.oplcasino;

public class Card extends CardType {
    /**
     Default constructor which makes an invalid card
     */
    Card(){
        suit = CardSuit.invalid;
        numericValue = 0;
        owner = "";
    }

    /**
     Initialize Cards to given suit and value
     @param su is the CardSuit of the card
     @param value the value to set the card to
     */
    Card(CardSuit su, int value){
        suit = su;
        if(!setValue(value)){
            numericValue = 0;
        }
        owner = "";
    }

    /**
     Copy constructor
     @param copy The Card to be copied
     */
    Card (Card copy){
        suit = copy.getSuit();
        numericValue = copy.getValue();
        owner = copy.getOwner();
    }

    /**
     Create a card from its Strings representation
     @param strCard The string representation of the card
     */
    Card(String strCard){
        //If wrong size, the card is invalid
        if(strCard.length() != 2){
            suit = CardSuit.invalid;
            numericValue = 0;
            return;
        }
        //Get the chars from the string
        char suitChar = strCard.charAt(0);
        char suitSym = strCard.charAt(1);

        //Convert to the enums
        suit = charToSuit(suitChar);
        numericValue = symbolToValue(suitSym);

        //No owner as its a card
        owner = "";
    }


    //@androidx.annotation.NonNull
    @Override
    public String toString() {
        return ""+suitToChar() + getSymbol();
    }
}
