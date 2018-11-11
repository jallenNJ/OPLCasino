package edu.ramapo.jallen6.oplcasino;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Vector;

public class BuildView extends CardView {
    Build model;
    Vector<CardView> cards;

    BuildView(Build master){
        model = master;
        cards = new Vector<CardView>(4,1);
        Vector<Card> models = model.getCards();
        for(int i =0; i < models.size(); i++){
            cards.add(new CardView(models.get(i)));
        }
    }

    public int getRequiredButtons(){
        return cards.size();
    }

    public void drawBuild(LinearLayout layout){
        if(layout.getChildCount() != cards.size() +1){
            return;
        }
        ((Button)layout.getChildAt(0)).setText(Integer.toString(model.getValue()));
        for(int i =0; i < cards.size(); i++){
            cards.get(i).setButton(((ImageButton) layout.getChildAt(i+1)));
        }


    }
}


/*
        //Test build code
        Button newButton = new Button(this);
        newButton.setId(View.generateViewId());
        newButton.setText("9");


        ImageButton testImage = generateButton();
        testImage.setImageResource(R.drawable.s3);
        ImageButton testImage2 = generateButton();
        testImage2.setImageResource(R.drawable.h6);


      LinearLayout test = new LinearLayout(this);
        test.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                test.setOrientation(LinearLayout.HORIZONTAL);
                test.addView(newButton);
                test.addView(testImage);
                test.addView(testImage2);
                ((LinearLayout) findViewById(R.id.tableScroll)).addView(test);

                GradientDrawable border = new GradientDrawable();
                border.setColor(Color.BLUE); //white background
                border.setStroke(15, 0xFF000000); //black border with full opacity
                test.setBackground(border);
 */