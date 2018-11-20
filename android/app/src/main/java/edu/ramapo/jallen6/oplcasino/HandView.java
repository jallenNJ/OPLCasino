package edu.ramapo.jallen6.oplcasino;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
;import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class HandView implements Observer {

    private Hand model;
    private Vector<CardView> hand;
    private boolean limitSelection;
    private CardView displayPool;

    HandView(){
        model = new Hand();
        constructorHelper(true);
    }


    HandView(Hand master, boolean limit){
        model = master;
        constructorHelper(true);
    }

    public int selectCard(int index){
        return model.selectCard(index);
    }

    public void unSelectAllCards(){model.unSelectAllCards();}

    public void update (Observable o, Object arg){

        if(model.size() >= hand.size()){
            createViewsFromModel();
            displayPool = hand.get(hand.size()-1);
        }else{
            removeCardFromHand(model.fetchRemovedIndex());

        }

    }

    /**
     Handles shared tasks between all constructors
     @param limit if Selection is limisted
     */
    private void constructorHelper(boolean limit){
        hand = new Vector<CardView>(4,1);
        limitSelection = limit;
        displayPool = null;

        //Bind to model
        model.addObserver(this);
        createViewsFromModel();
    }


    public int size(){
        return hand.size();
    }
    public int getLastIndex(){
        return hand.size()-1;
    }

    /**
     Draw card at index to provided card
     @param button The button to draw the image too
     @param cardIndex The index of the card
     */
    public void displayCard(ImageButton button, int cardIndex){
        //Ensure valid parameters
        if(button == null || cardIndex < 0 || cardIndex >= hand.size() ) {
            return;
        }
        hand.get(cardIndex).setButton(button);
    }

    /**
     Draw a build to a linear layout
     @param layout The layout to draw the build to
     @param index The index of the card
     */
    public void displayBuild(LinearLayout layout, int index){
        //Ensure valid parameters
        if(layout == null || index < 0 || index >= hand.size() ) {
            return;
        }
        ((BuildView)hand.get(index)).drawBuild(layout);
    }


    /**
     Get amount of buttons needed at the given index
     @param index index to check
     @return the amount needed. 0 if invalid index
     */
    public int getNeededButtonForIndex(int index){
        if(index< 0 || index >= hand.size()){
            return 0;
        }

        return hand.get(index).getRequiredButtons();
    }

    /**
     Display any card in the display bool to the button
     @param button The button to draw the iamge to
     */
    public void displaySelected(ImageButton button){
        if(button == null || displayPool == null){
            return;
        }

        //Draw and clear pool
        displayPool.setButton(button);
        displayPool = null;

    }

    /**
     Remove card at index and return a copy of it
     @param index index of card to remove
     @return CardView that was removed, null if invalid
     */
    public CardView removeCardFromHand(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }
        CardView removed = hand.get(index);
        hand.remove(index);
        return removed;
    }

    /**
     Updated the view based on the current state of the model
     */
    public void createViewsFromModel(){
        //Remove all views
        hand.clear();
        int index = 0;
        //Iterate through the model
        while(true){
            CardType current = model.peekCard(index);
            //If null, reached end of hand
            if(current == null){
                return;
            }

            //Add appropriate view based on if its a build or not
            if(current.getSuit() == CardSuit.build){
                hand.add(new BuildView((BuildType)current));
            } else{
                hand.add(new CardView((Card) current));
            }

            index++;
        }
    }
}
