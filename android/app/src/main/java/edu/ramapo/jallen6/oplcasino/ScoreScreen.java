package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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

       // Hand humanPile = players[0].getPile();
       // Hand compPile = players[1].getPile();

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

    private void startNextRound(){
        Intent intent = new Intent(this, GameLoop.class);
        boolean humanCappedLast = lastCap == PlayerID.humanPlayer;
        intent.putExtra(GameLoop.humanFirstExtra, humanCappedLast );
        intent.putExtra(GameLoop.fromSaveGameExtra, false );
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
}
