package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class PlayerView {
    private HandView hand;
    private int selectedIndex;
    private HandView pile;
    private Vector<Integer> reservedValues;
    private Player model;


    PlayerView(Player master){
        model = master;
        hand = new HandView(model.getHand(), true);
        pile = new HandView (model.getHand(), false);
        selectedIndex = -1;
        reservedValues = new Vector<Integer>(2,1);

    }

    public HandView getHand() {
        return hand;
    }

    public boolean addCardToHand(Card add){
        model.addCardToHand(add);
        hand.createViewsFromModel();
        return true;
    }
    public boolean addCardToPile(Card add){
       // pile.addCard(add);
        return true;
    }




}
