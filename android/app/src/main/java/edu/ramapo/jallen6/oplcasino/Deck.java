package edu.ramapo.jallen6.oplcasino;


import java.util.Collections;
import java.util.Vector;

public class Deck {
    private Vector<Card> deck;


    Deck(){
        deck = new Vector<Card>(52, 5);
        for(CardSuit i : CardSuit.values() ){
            if(i == CardSuit.invalid || i == CardSuit.build){
                continue;
            }
            for(int j = 1; j < 14; j++){
                deck.add(new Card(i, j));
            }
        }
        Collections.shuffle(deck);


    }


    public boolean isEmpty(){
        return deck.size() == 0;
    }
    public Card drawCard(){
        if(isEmpty()){
            return null;
        }
        Card returnCard = deck.firstElement();
        deck.remove(0);
        return returnCard;
    }

    public void dealFourCardsToHand(Hand target){
        for(int i = 0; i < 4; i++){
            target.addCard(drawCard());
        }
        return;
    }

}
