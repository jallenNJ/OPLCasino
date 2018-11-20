package edu.ramapo.jallen6.oplcasino;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Vector;

public class BuildView extends CardView {
    BuildType model;
    Vector<CardView> cards;

    BuildView(BuildType master){
        // Save the model
        model = master;

        //Pull every card out of it, and make them into views
        cards = new Vector<CardView>(4,1);
        Vector<Card> models = model.getCards();
        for(int i =0; i < models.size(); i++){
            cards.add(new CardView(models.get(i)));
        }
    }

    public int getRequiredButtons(){
        return cards.size();
    }


    //BQ for strings on buttons
    public void drawBuild(LinearLayout layout){

        //Ensure the layout has enough buttons
        if(layout.getChildCount() != cards.size() +1){
            return;
        }
        //Put the sum of build and its toString onto the button
        Button labelButton = ((Button)layout.getChildAt(0));
        labelButton.setText(Integer.toString(model.getValue())+
                "\n" + model.toString()+
                "\n" + model.getOwner());

        labelButton.setTextColor(Color.BLACK);
        labelButton.setBackgroundResource(R.drawable.rounded_rectangle);
        labelButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //For every card, have it draw its image onto the correct button
        for(int i =0; i < cards.size(); i++){
            cards.get(i).setButton(((ImageButton) layout.getChildAt(i+1)));
        }


    }
}
