package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class GameLoop extends AppCompatActivity {
    private HandView testView;
    Round currentRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);
        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra("humanFirst", true);
        currentRound = new Round();

        Deck deck = new Deck();
        Hand test = new Hand();
        deck.dealFourCardsToHand(test);
        currentRound.playRound();

    }

    public void displayCard(View view) {
        HandView viewHandler = null;

        if((view.getTag()).toString().equals("Human")){
            viewHandler = currentRound.getHumanHandHandler();
        }else{
            viewHandler = currentRound.getComputerHandHandler();
        }

        if (viewHandler == null) {
            return;
        }
        ImageButton chosen = findViewById(view.getId());
        int selectedId = view.getId();
        switch (selectedId) {
            case R.id.hcard1:
            case R.id.ccard1:
                viewHandler.displayCard(chosen, 0);
                break;
            case R.id.hcard2:
            case R.id.ccard2:
                viewHandler.displayCard(chosen, 1);
                break;
            case R.id.hcard3:
            case R.id.ccard3:
                viewHandler.displayCard(chosen, 2);
                break;
            case R.id.hcard4:
            case R.id.ccard4:
                viewHandler.displayCard(chosen, 3);
                break;
        }
    }

}