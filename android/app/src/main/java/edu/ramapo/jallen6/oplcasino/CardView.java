package edu.ramapo.jallen6.oplcasino;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CardView extends BaseView {
    Card model;

    CardView(){
        model = new Card();
    }
    CardView(Card master){
        model = master;
    }


    void setButton (ImageButton ref){
        if(ref != null){
                ref.setImageResource(R.drawable.d3);

        }

    }
}
