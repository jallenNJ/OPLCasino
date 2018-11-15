package edu.ramapo.jallen6.oplcasino;

public class Card extends CardType {

    Card(){
        suit = CardSuit.invalid;
        numericValue = 0;
        owner = "";
    }

    Card(CardSuit su, int value){
        suit = su;
        if(!setValue(value)){
            numericValue = 0;
        }
        owner = "";
    }
    Card (Card copy){
        suit = copy.getSuit();
        numericValue = copy.getValue();
        owner = copy.getOwner();
    }

    Card(String strCard){
        if(strCard.length() != 2){
            suit = CardSuit.invalid;
            numericValue = 0;
            return;
        }
        char suitChar = strCard.charAt(0);
        char suitSym = strCard.charAt(1);
        suit = charToSuit(suitChar);
        numericValue = symbolToValue(suitSym);
        owner = "";
    }
    //@androidx.annotation.NonNull
    @Override
    public String toString() {
        return ""+suitToChar() + getSymbol();
    }
}
