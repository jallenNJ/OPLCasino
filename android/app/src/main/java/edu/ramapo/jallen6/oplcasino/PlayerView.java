package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class PlayerView {
    private HandView hand;
    private int selectedIndex;
    private HandView pile;
    private Vector<Integer> reservedValues;
    private Player model;


    /**
     Create the object and bind to the model
     @param master The model to bind to
     */
    PlayerView(Player master){
        model = master;
        hand = new HandView(model.getHand(), true);
        pile = new HandView (model.getPile(), false);
        selectedIndex = -1;
        reservedValues = new Vector<Integer>(2,1);

    }

    public HandView getHand() {
        return hand;
    }

    public HandView getPile(){
        return pile;
    }

}
