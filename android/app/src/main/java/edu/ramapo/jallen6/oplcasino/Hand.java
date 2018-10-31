package edu.ramapo.jallen6.oplcasino;


import java.util.Vector;

public class Hand {

    private Vector<CardType> hand;
    boolean selectionLimitedToOne;

    Hand(){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = true;
    }

    Hand(boolean limitSelection){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
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

    public CardType peekCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }

        return hand.get(index);
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
