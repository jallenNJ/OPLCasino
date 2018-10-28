package edu.ramapo.jallen6.oplcasino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CoinFlip extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
    }

    public void setText(View view){
         double randNumRaw = Math.random();
         randNumRaw *= 10;
         int randNum = (int) randNumRaw;
         boolean flipIsHeads = false;
        if(randNum % 2 == 0){
            flipIsHeads = true;
        }

         TextView textView = findViewById(R.id.result);
         if(view.getId() == R.id.headsButton){
             if(flipIsHeads){
                 textView.setText("Heads! Human goes first");
             }else{
                 textView.setText("Heads! Computer goes first");
             }


         } else{
             if(flipIsHeads){
                 textView.setText("Heads! Computer goes first");
             } else{
                 textView.setText("Tails! Human goes first");
             }
         }

    }
}
