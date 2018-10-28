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
        //view.toString();
         TextView textView = findViewById(R.id.result);
         if(view.getId() == R.id.headsButton){
             textView.setText("Heads");
         } else{
             textView.setText("Tails");
         }

    }
}
