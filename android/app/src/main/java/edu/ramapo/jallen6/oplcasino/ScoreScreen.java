package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ScoreScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_screen);
    }

    public void openActionLog(View view){

        Intent intent = new Intent(this, ActionLogPopup.class);
        startActivityForResult(intent,RESULT_CANCELED);

    }
}
