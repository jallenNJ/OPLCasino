package edu.ramapo.jallen6.oplcasino;

import android.media.Image;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
;import java.util.Vector;

public class HandView extends BaseView {

    private Hand model;
    private Vector<CardView> hand;
    boolean limitSelection;

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

    private void constructorHelper(boolean limit){
        hand = new Vector<CardView>(4,1);
        limitSelection = limit;
        createViewsFromModel();
    }
    public void displayCard(ImageButton button, int cardIndex){
        if(button == null || cardIndex < 0 || cardIndex >= hand.size() ) {
            return;
        }
        hand.get(cardIndex).setButton(button);
       // Button button = view.findViewById(view.getId());
       // button.setText(model.peekCard(0).toString());
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
