package edu.ramapo.jallen6.oplcasino;


import java.util.Vector;

public class Hand {

    private Vector<CardType> hand;

    Hand(){
        hand = new Vector<CardType>(4,1);
    }

    public int size(){
        return hand.size();
    }
    public boolean isEmpty(){
        return hand.size() == 0;
    }
    public boolean addCard(CardType add){
        hand.add(add);
        return true;
    }

    public CardType removeCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }
        CardType removed = hand.get(index);
        hand.remove(index);
        return removed;
    }

}
