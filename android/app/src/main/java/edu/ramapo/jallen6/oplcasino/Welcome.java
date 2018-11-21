package edu.ramapo.jallen6.oplcasino;

/*
 ************************************************************
 * Name:  Joseph Allen jallen6@ramapo.edu                   *
 * Project:  Android / Java Casino Fa 18                    *
 * Class:  CMPS 366-01 FA 18                                *
 * Date:  2018-11-20                                        *
 ************************************************************
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Welcome extends AppCompatActivity {
  //  public static final String EXTRA_MESSAGE = "edu.ramapo.jallen6.oplcasino.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //Make sure the static class is initialized
        Serializer.init();
    }


    /**
     * Start a new game with a coin toss
     * @param view The button that was clicked
     */
    public void newGame(View view) {
        Intent intent = new Intent(this, CoinFlip.class);
        startActivity(intent);
        finish();
    }

    /**
     * Have the user choose the save game to load
     * @param view The button that was clicked
     */
    public void loadGame(View view){
        Intent intent = new Intent(this, LoadScreen.class);
        startActivity(intent);
        finish();
    }

}
