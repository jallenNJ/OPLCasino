package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameLoop extends AppCompatActivity {
    private HandView testView;
    private CardView cardViewTest1;
    private CardView cardViewTest2;
    private CardView cardViewTest3;
    private CardView cardViewTest4;
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
        cardViewTest1 = new CardView((Card)test.peekCard(0));
        cardViewTest2 = new CardView((Card)test.peekCard(1));
        cardViewTest3 = new CardView((Card)test.peekCard(2));
        cardViewTest4 = new CardView((Card)test.peekCard(3));
        //cardViewTest1.setButton();
    }

    public void displayCard(View view){
      //  cardViewTest.setButton((Button)findViewById(view.getId()));
       // cardViewTest.setButton((ImageButton)findViewById(view.getId()));
        ImageButton chosen = findViewById(view.getId());
        int selectedId = view.getId();
        switch (selectedId){
            case R.id.hcard1:
                cardViewTest1.setButton(chosen);
                break;
            case R.id.hcard2:
                cardViewTest2.setButton(chosen);
                break;
            case R.id.hcard3:
                cardViewTest3.setButton(chosen);
                break;
            case R.id.hcard4:
                cardViewTest4.setButton(chosen);
                break;
        }
    }
  //  public void drawHand(View view){
   //     testView.displayCard(view);
   // }
}
