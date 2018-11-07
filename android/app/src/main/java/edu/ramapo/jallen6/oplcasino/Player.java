package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public abstract class Player {

    protected Hand hand;
    protected int selectedIndex;
    protected Hand pile;
    protected Vector<Integer> reservedValues;

    Player(){
        hand = new Hand();
        pile = new Hand();
        selectedIndex = -1;
        reservedValues = new Vector<Integer>(2,1);

    }

    public Hand getHand() {
        return hand;
    }

    public Hand getPile(){
        return pile;
    }

    public boolean addCardToHand(Card add){
        hand.addCard(add);
        return true;
    }
    public boolean addCardsToHand(Card[] add){
        for(int i =0; i < add.length; i++){
            addCardToHand(add[i]);
        }
        return true;
    }
    public boolean addCardToPile(Card add){
        pile.addCard(add);
        return true;
    }

    public boolean addCardsToPile(Card[] add){
        for(int i =0; i < add.length; i++){
            addCardToPile(add[i]);
        }
        return true;
    }

    public Card removeCardFromHand(int i){
        return (Card)hand.removeCard(i);
    }
    public int getHandSize(){
        return hand.size();
    }

    public int getSelectedIndex() {
        return hand.getSelectedIndices().get(0);
    }

    public boolean selectCard(int index){
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
