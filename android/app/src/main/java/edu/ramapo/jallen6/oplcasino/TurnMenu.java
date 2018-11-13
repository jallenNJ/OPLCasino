package edu.ramapo.jallen6.oplcasino;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TurnMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turn_menu);
    }


    public void quitGame (View view){
        finishAffinity();
        System.exit(0);
    }


}
