package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameLoop extends AppCompatActivity {
    private HandView testView;
    private CardView cardViewTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);
        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra("humanFirst", true);
        Round firstRound = new Round();

        Deck deck = new Deck();
        Hand test = new Hand();
        deck.dealFourCardsToHand(test);
        //testView = new HandView(test);
        cardViewTest = new CardView((Card)test.peekCard(0));
    }

    public void displayCard(View view){
      //  cardViewTest.setButton((Button)findViewById(view.getId()));
        cardViewTest.setButton((ImageButton)findViewById(view.getId()));
    }
  //  public void drawHand(View view){
   //     testView.displayCard(view);
   // }
}
