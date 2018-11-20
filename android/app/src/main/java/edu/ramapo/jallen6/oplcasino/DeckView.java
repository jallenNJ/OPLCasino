package edu.ramapo.jallen6.oplcasino;

import android.widget.ImageButton;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

public class DeckView implements Observer {
    private Deck model;
    private Vector<CardView> deck;

    /**
     Binds to the model
     @param master The model to watch
     */
    DeckView(Deck master){
        model = master;
        deck = new Vector<CardView>(52,1);
        createViewsFromModel();
    }


    /**
     Create card views to match the current state of the deck
     */
    public void createViewsFromModel(){
        //Delete all current views
        deck.clear();
        int index = 0;
        while(true){
            //Get the current card
            CardType current = model.peekCard(index);
            //If its null, hit the end of hand, so end
            if(current == null){
                return;
            }
            //Make card view, increment index
            deck.add(new CardView((Card) current));
            index++;
        }

    }

    /**
     Update cards whens model changes
     @param o The object which triggered the updated
     @param arg Unused, but required in overload
     */
    public void update (Observable o, Object arg){

      createViewsFromModel();

    }

    public int size(){
        return deck.size();
    }

    /**
     Has specified card draw itself to passed button
     @param button The button which to draw the image to
     @param cardIndex The index of the card which needs to be drawn
     */
    public void displayCard(ImageButton button, int cardIndex){
        //Ensure valid parameters
        if(button == null || cardIndex < 0 || cardIndex >= deck.size() ) {
            return;
        }
        //Draw the iamge
        deck.get(cardIndex).setButton(button);
    }


}
