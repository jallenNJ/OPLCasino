package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.graphics.Color.WHITE;

public class ScoreScreen extends AppCompatActivity {
    private static Player[] players;
    private static PlayerID lastCap;
    private Tournament tour;

    public static void setPlayers (Player[] playersArr){
        players = playersArr;
    }

    public static void setLastCap(PlayerID lastCapturer){
        lastCap = lastCapturer;
    }
    public static void clearData(){
        players = null;
        lastCap = null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);



        tour = new Tournament(players);

        int[] roundscore = tour.getRoundScores();
        int[] tourScore = tour.getTourScores();
        TourScoreCode winner = tour.getWinner();

        initPiles((LinearLayout) findViewById(R.id.scoreBottomLayout), players[0]);
        initPiles((LinearLayout) findViewById(R.id.scoreTopLayout), players[1]);

        String roundScores;
        roundScores = "Human scored " + Integer.toString(roundscore[0]) + " points " +
                "\n Computer scored " + Integer.toString(roundscore[1]) +  "points\n";

        boolean newRound = false;
        String tourResultStr;
        if(winner == TourScoreCode.NoWinner){
            tourResultStr = "\n";
            newRound = true;
        } else if(winner == TourScoreCode.HumanWon){
            tourResultStr = "Human won the tournament!\n";
        } else if(winner == TourScoreCode.ComputerWon){
            tourResultStr = "Computer won the tournament!\n";
        } else{
            tourResultStr = "The tournament was a tie!\n";
        }

        tourResultStr += "Human total score is " + Integer.toString(tourScore[0]) + " points " +
                "\n Computer total score is " + Integer.toString(tourScore[1]) + " points.";

        ((TextView) findViewById(R.id.scoreAnnoucment)).setText(roundScores + "\n" + tourResultStr);

        ((TextView) findViewById(R.id.rawScoresText)).setText(tour.toString());

        Button leaveScreen = findViewById(R.id.leaveScoreButton);
        if(newRound){
            leaveScreen.setText("Next Round!");

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startNextRound();
                }
            };
            leaveScreen.setOnClickListener(clickListener);


        } else{
            leaveScreen.setText("Main Menu!");

            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goToMainMenu();
                }
            };
            leaveScreen.setOnClickListener(clickListener);
        }






    }



    public void openActionLog(View view){

        Intent intent = new Intent(this, ActionLogPopup.class);
        startActivityForResult(intent,RESULT_CANCELED);

    }

    private void initPiles(LinearLayout layout, Player player){
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        textView.setText(player.getName() + "\npile:");

        layout.addView(textView);



        //Set the margins and size
        LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(intAsDP(50), intAsDP(80));
        lp.setMargins(20, 0, 20, 0);
        Hand pile = player.getPile();
        for(int i =0; i < pile.size(); i++){
            //Should be unneeded, but rather safe than sorry for the presentation
            Card current = null;
            try{
               current = (Card)pile.peekCard(i);
            } catch (Exception e){
                continue;
            }
            if(current == null){
                continue;
            }

            CardView cardView = new CardView(current);
            ImageButton imageButton = new ImageButton(this);
            imageButton.setId(View.generateViewId());
            cardView.setButton(imageButton);

            imageButton.setLayoutParams(lp);

            //Ensure the background is the normal color and the crop is correct
            imageButton.setBackgroundColor(WHITE);
           imageButton.setScaleType(ImageView.ScaleType.CENTER_CROP);
            layout.addView(imageButton);

        }



    }

    private void startNextRound(){
        Intent intent = new Intent(this, GameLoop.class);
        boolean humanCappedLast = lastCap == PlayerID.humanPlayer;
        intent.putExtra(GameLoop.humanFirstExtra, humanCappedLast );
        intent.putExtra(GameLoop.fromSaveGameExtra, false );
        intent.putExtra(GameLoop.humanPlayerStartScore, players[0].getScore());
        intent.putExtra(GameLoop.compPlayerStartScore, players[1].getScore());

        startActivity(intent);
        clearData();
        finish();
    }

    private void goToMainMenu(){
        Intent intent = new Intent(this, Welcome.class);
        startActivity(intent);
        clearData();
        finish();
    }

    //Taken from gameLoop
    private int intAsDP(int target){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, target,
                getResources().getDisplayMetrics());
    }
}
