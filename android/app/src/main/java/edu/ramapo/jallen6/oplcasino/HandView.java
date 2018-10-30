package edu.ramapo.jallen6.oplcasino;

import android.view.View;
import android.widget.Button;
;

public class HandView extends BaseView {

    Hand model;

    HandView(){
        model = new Hand();
    }

    HandView(Hand master){
        model = master;
    }

    public void displayCard(View view){
        Button button = view.findViewById(view.getId());
        button.setText(model.peekCard(0).toString());
    }



}
