package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class Build extends CardType {
    Vector<Card> cards;

    Build(Vector<Card> inputCards){
        /*for(int i =0; i < inputCards.size(); i++){
            cards.add(inputCards.get(i));

        }*/
        cards = new Vector<Card>(4,1);
        cards.addAll(inputCards);
        int sum = 0;
        for(int i =0; i < cards.size(); i++){
            sum += cards.get(i).getValue();
        }
        numericValue = sum;
        suit = CardSuit.build;

    }

    public Vector<Card> getCards(){
        return new Vector<Card>(cards);
    }


}
