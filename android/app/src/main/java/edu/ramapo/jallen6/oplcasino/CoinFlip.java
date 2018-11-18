package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CoinFlip extends AppCompatActivity {

    private boolean humanFirst = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);
    }

    public void setText(View view){

        //Generate a random number and multiply by 10 so its on the interval [0, 10)
         double randNumRaw = Math.random();
         randNumRaw *= 10;
         int randNum = (int) randNumRaw;

         //Get if the coin landed on heads
         boolean flipIsHeads = false;
        if(randNum % 2 == 0){
            flipIsHeads = true;
        }

         TextView textView = findViewById(R.id.result);
         if(view.getId() == R.id.headsButton){

             //If guess is heads
             if(flipIsHeads){
                 textView.setText("Heads! Human goes first");
                 humanFirst = true;
             }else{
                 textView.setText("Heads! Computer goes first");
             }


         } else{ //If guess is tails

             if(flipIsHeads){
                 textView.setText("Heads! Computer goes first");
             } else{
                 textView.setText("Tails! Human goes first");
                 humanFirst = true;
             }
         }

         //Make the next screen button visible so the user can proceed
        Button startButton = findViewById( R.id.startButton);
         startButton.setVisibility(View.VISIBLE);

         //Make the call buttons faded and unclickable
         Button headsButton = findViewById(R.id.headsButton);
         headsButton.setAlpha(.5f);
         headsButton.setClickable(false);
         Button tailsButton = findViewById(R.id.tailsButton);
         tailsButton.setAlpha(.5f);
         tailsButton.setClickable(false);
    }


    public void startGame (View view){
        Intent intent = new Intent(this, GameLoop.class);
        //Store who won the cointoss and declare this is a new game
        intent.putExtra(GameLoop.humanFirstExtra, humanFirst );
        intent.putExtra(GameLoop.fromSaveGameExtra, false );
        intent.putExtra(GameLoop.humanPlayerStartScore, 0);
        intent.putExtra(GameLoop.compPlayerStartScore, 0);

        //Start the new scene and finish this one so the user can back into it
        startActivity(intent);
        finish();
    }
}
