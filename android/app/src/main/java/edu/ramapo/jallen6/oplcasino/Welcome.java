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
    }

    /** Called when the user taps the Send button */
    public void newGame(View view) {
        Intent intent = new Intent(this, CoinFlip.class);
       // EditText editText = (EditText) findViewById(R.id.editText);
     //   String message = editText.getText().toString();
       // intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }

}
