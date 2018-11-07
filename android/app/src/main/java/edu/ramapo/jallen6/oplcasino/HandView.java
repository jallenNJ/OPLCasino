package edu.ramapo.jallen6.oplcasino;

import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
;import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class HandView extends BaseView implements Observer {

    private Hand model;
    private Vector<CardView> hand;
    boolean limitSelection;
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
        int debug = model.countObservers();

        if(model.size() >= hand.size()){
            createViewsFromModel();
            displayPool = hand.get(hand.size()-1);
        }else{
            removeCardFromHand(model.fetchRemovedIndex());

        }

    }
    private void constructorHelper(boolean limit){
        hand = new Vector<CardView>(4,1);
        limitSelection = limit;
        displayPool = null;

        model.addObserver(this);
        createViewsFromModel();
    }


    public int size(){
        return hand.size();
    }
    public void displayCard(ImageButton button, int cardIndex){
        if(button == null || cardIndex < 0 || cardIndex >= hand.size() ) {
            return;
        }
        hand.get(cardIndex).setButton(button);
       // Button button = view.findViewById(view.getId());
       // button.setText(model.peekCard(0).toString());
    }

    public void displaySelected(ImageButton button){
        if(button == null || displayPool == null){
            return;
        }
      //displayCard(button, hand.indexOf(displayPool));
        displayPool.setButton(button);
        displayPool = null;


    }

   /* public void addCard(Card add){
        model.addCard(add);
        hand.add(new CardView((Card)add));
        displayPool = new CardView((Card) add);
    }*/
  //  public void addCard(CardView add){
    //   model.addCard(new Card(add.getModelSuit(), add.getModelValue()));
     //  hand.add(add);
     //  displayPool = new CardView(add);
   // }

    public CardView removeCardFromHand(int index){
        if(index >= hand.size() || index < 0){
            return null;
        }
        CardView removed = hand.get(index);
        hand.remove(index);
      //  model.removeCard(index);
        return removed;
    }

    public void createViewsFromModel(){
        hand.clear();
        int index = 0;
        while(true){
            CardType current = model.peekCard(index);
            if(current == null){
                return;
            }
            hand.add(new CardView((Card) current));
            index++;
        }

    }



}
