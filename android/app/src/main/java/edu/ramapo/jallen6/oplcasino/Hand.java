package edu.ramapo.jallen6.oplcasino;


import java.util.Vector;

public class Hand {

    private Vector<CardType> hand;
    private Vector<Integer> selectedIndices;
    boolean selectionLimitedToOne;

    Hand(){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = true;
        selectedIndices = new Vector<Integer>(1,1);
    }

    Hand(boolean limitSelection){
        hand = new Vector<CardType>(4,1);
        selectionLimitedToOne = limitSelection;
        selectedIndices = new Vector<Integer>(4,4);
    }

    public int size(){
        return hand.size();
    }
    public boolean isEmpty(){
        return hand.size() == 0;
    }

    public int selectCard(int index){
        if(selectionLimitedToOne && selectedIndices.size() >0){
            int replacedVal = selectedIndices.remove(0);
            selectedIndices.clear();
            selectedIndices.add(index);
            return replacedVal;
        } else{
            selectedIndices.add(index);
            return -1;
        }
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
