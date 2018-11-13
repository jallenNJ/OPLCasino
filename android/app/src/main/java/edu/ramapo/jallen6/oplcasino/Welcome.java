package edu.ramapo.jallen6.oplcasino;

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
        Serializer.init();
    }

    /** Called when the user taps the Send button */
    public void newGame(View view) {
        Intent intent = new Intent(this, CoinFlip.class);
        startActivity(intent);
        finish();
    }

    public void loadGame(View view){
        Intent intent = new Intent(this, LoadScreen.class);
        startActivity(intent);
        finish();
    }

}
