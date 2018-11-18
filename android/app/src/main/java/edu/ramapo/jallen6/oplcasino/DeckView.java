package edu.ramapo.jallen6.oplcasino;

import android.widget.ImageButton;

import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class DeckView implements Observer {
    Deck model;
    Vector<CardView> deck;

    DeckView(Deck master){
        model = master;
        deck = new Vector<CardView>(52,1);
        createViewsFromModel();
    }


    public void createViewsFromModel(){
        deck.clear();
        int index = 0;
        while(true){
            CardType current = model.peekCard(index);
            if(current == null){
                return;
            }
            deck.add(new CardView((Card) current));
            index++;
        }

    }

    public void update (Observable o, Object arg){

      createViewsFromModel();

    }

    public int size(){
        return deck.size();
    }

    public void displayCard(ImageButton button, int cardIndex){
        if(button == null || cardIndex < 0 || cardIndex >= deck.size() ) {
            return;
        }
        deck.get(cardIndex).setButton(button);
    }


}
