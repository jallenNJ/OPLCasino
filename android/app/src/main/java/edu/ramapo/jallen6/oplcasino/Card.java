package edu.ramapo.jallen6.oplcasino;

public class Card extends CardType {

    Card(){
        suit = CardSuit.invalid;
        numericValue = 0;
    }

    Card(CardSuit su, int value){
        suit = su;
        if(!setValue(value)){
            numericValue = 0;
        }
    }
    Card (Card copy){
        suit = copy.getSuit();
        numericValue = copy.getValue();
    }
    //@androidx.annotation.NonNull
    @Override
    public String toString() {
        return ""+suitToChar() + getSymbol();
    }
}
