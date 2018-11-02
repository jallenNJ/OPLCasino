package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GameLoop extends AppCompatActivity {
    Round currentRound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra("humanFirst", true);
        currentRound = new Round();
        initDisplayCards();

        for(int i =0; i < 20; i++){
            addButtonToTable();
        }


    }

    private int getTableCardCount(){
        LinearLayout view =  findViewById(R.id.tableScroll);
        return view.getChildCount();
    }

    private void addButtonToTable(){
        ImageButton newButton = new ImageButton(this);
       newButton.setImageResource(R.drawable.cardback);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()), (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, getResources().getDisplayMetrics()));
        lp.setMargins(20,0,20,0);
       newButton.setLayoutParams(lp);
        //   testSpawn.setOnClickListener(ClickListener);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableCardClick(view);
            }
        };
        newButton.setOnClickListener(clickListener);
        newButton.setBackgroundColor(Color.WHITE);
        // testSpawn.setTag(i);
        newButton.setId(View.generateViewId());
        newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout view =  findViewById(R.id.tableScroll);
        view.addView(newButton);
    }

    private void initDisplayCards(){
        HandView handler = currentRound.getHumanHandHandler();
        handler.displayCard((ImageButton) findViewById(R.id.hcard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.hcard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.hcard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.hcard4), 3);

        handler = currentRound.getComputerHandHandler();
        handler.displayCard((ImageButton) findViewById(R.id.ccard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.ccard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.ccard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.ccard4), 3);

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

    public void tableCardClick(View view){
        ImageButton chosen = findViewById(view.getId());
       // chosen.setBackgroundColor(Color.GREEN);
        HandView viewHandler = currentRound.getHumanHandHandler();
        viewHandler.displayCard(chosen, 1);
    }

}