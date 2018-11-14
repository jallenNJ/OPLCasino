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

    Hand(Hand copy){
        hand = new Vector<CardType>(copy.hand);
        selectedIndices = new Vector<Integer>(copy.selectedIndices);
        selectionLimitedToOne = copy.selectionLimitedToOne;
        removedIndex = copy.removedIndex;
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
            if(!selectedIndices.contains(index)){
                selectedIndices.add(index);
            }

            return -1;
        }
    }

    public void unselectCard(int index){
        selectedIndices.remove(index);
    }

    public int getAmountSelect(){
        return selectedIndices.size();
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

    public String toString(){
        String formatted = "";
        for(int i =0; i < hand.size(); i++){
            formatted += hand.get(i).toString() + " ";
        }
        return formatted;
    }

}
