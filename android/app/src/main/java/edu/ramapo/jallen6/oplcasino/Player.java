package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public abstract class Player {

    private Hand hand;
    private int selectedIndex;
    private Hand pile;
    private Vector<Integer> reservedValues;

    public boolean addCardToHand(Card add){
        hand.addCard(add);
        return true;
    }
    public boolean addCardToPile(Card add){
        pile.addCard(add);
        return true;
    }

    public boolean selectedCard(int index){
        if(index < 0 || index >= hand.size()){
            return false;
        }
        selectedIndex = index;
        return true;
    }

    public boolean unselectCard(){
        selectedIndex = -1;
        return true;
    }

    public boolean reserveBuildValue(Card res){
        reservedValues.add(res.getValue());
        return true;
    }
    public boolean releaseBuildValue(Card res){
        reservedValues.remove(res.getValue());
        return true;
    }

    public abstract PlayerMove doMove();
}
