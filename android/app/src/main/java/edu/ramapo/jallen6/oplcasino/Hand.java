package edu.ramapo.jallen6.oplcasino;


import java.util.Observable;
import java.util.Vector;

public class Hand extends Observable {

    private Vector<CardType> hand;
    private Vector<Integer> selectedIndices;
    private boolean selectionLimitedToOne;
    private int removedIndex;

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

    public boolean hasSelectedCards(){
        return selectedIndices.size() > 0;
    }

    public Vector<Integer> getSelectedIndices(){
        if(selectedIndices == null){
            return null;
        }
        if(selectedIndices.size() == 0){
            return new Vector<Integer>(1,1);
        }
       return new Vector<Integer>(selectedIndices);
    }

    public int selectCard(int index){
        if(selectionLimitedToOne && selectedIndices.size() >0){
            int replacedVal = selectedIndices.remove(0);
            selectedIndices.clear();
            if(replacedVal != index){
                selectedIndices.add(index);
                return replacedVal;
            } else{
                return -1;
            }

        } else{
            selectedIndices.add(index);
            return -1;
        }
    }

    public void unSelectAllCards(){

        selectedIndices.clear();
    }
    public boolean addCard(CardType add){
        hand.add(add);
        this.setChanged();
        this.notifyObservers();
        return true;
    }

    public CardType peekCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }

        return hand.get(index);
    }
public  int fetchRemovedIndex(){
        int returnVal = removedIndex;
        removedIndex = -1;
        return returnVal;
       // return removedIndex;
}

    public CardType removeCard(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }
        CardType removed = hand.get(index);
        hand.remove(index);
        removedIndex = index;
        this.setChanged();
        this.notifyObservers();
        return removed;
    }

}
