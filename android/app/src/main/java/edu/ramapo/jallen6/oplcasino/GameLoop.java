package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Vector;

public class GameLoop extends AppCompatActivity {
    Round currentRound;
    final int selectedColor = Color.CYAN;
    final int normalColor = Color.WHITE;
    private Vector<Integer> humanHandIds;
    private Vector<Integer> compHandIds;
    private Vector<Integer> tableButtonIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra("humanFirst", true);
        currentRound = new Round(0 , humanStarting );
        initDisplayCards();


    }

    private int getTableCardCount(){
        LinearLayout view =  findViewById(R.id.tableScroll);
        return view.getChildCount();
    }

    private ImageButton addButtonToTable(){
        ImageButton newButton = new ImageButton(this);
       newButton.setImageResource(R.drawable.cardback);
        LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(intAsDP(50), intAsDP(80));
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
        newButton.setBackgroundColor(normalColor);
        // testSpawn.setTag(i);

        newButton.setId(View.generateViewId());
        tableButtonIds.add(newButton.getId());
        newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        LinearLayout view =  findViewById(R.id.tableScroll);
        view.addView(newButton);
        return newButton;
    }

    private void initDisplayCards(){
        HandView handler = currentRound.getHumanHandHandler();
        humanHandIds = new Vector<Integer>(4,1);
        humanHandIds.add(R.id.hcard1);
        humanHandIds.add(R.id.hcard2);
        humanHandIds.add(R.id.hcard3);
        humanHandIds.add(R.id.hcard4);
        handler.displayCard((ImageButton) findViewById(R.id.hcard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.hcard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.hcard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.hcard4), 3);

        handler = currentRound.getComputerHandHandler();
        compHandIds = new Vector<Integer>(4,1);
        compHandIds.add(R.id.ccard1);
        compHandIds.add(R.id.ccard2);
        compHandIds.add(R.id.ccard3);
        compHandIds.add(R.id.ccard4);
        handler.displayCard((ImageButton) findViewById(R.id.ccard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.ccard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.ccard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.ccard4), 3);
        setClickableForVector(compHandIds, false);

        handler = currentRound.getTableHandHandler();
        tableButtonIds = new Vector<Integer>(4,4);
        for(int i =0; i < 4; i++){
            handler.displayCard(addButtonToTable(), i);
        }

        setSubmitButton(false);
    }
    public void displayCard(View view) {
        HandView viewHandler = null;

        boolean isHuman = false;
        if((view.getTag()).toString().equals("Human")){
            viewHandler = currentRound.getHumanHandHandler();
            isHuman = true;
        }else{
            viewHandler = currentRound.getComputerHandHandler();
        }

        if (viewHandler == null) {
            return;
        }
        ImageButton chosen = findViewById(view.getId());
        toggleButtonColor(chosen);
        int selectedId = view.getId();
        int index =0;
        switch (selectedId) {
            case R.id.hcard1:
            case R.id.ccard1:
                index = 0;
                break;
            case R.id.hcard2:
            case R.id.ccard2:
                index = 1;
                break;
            case R.id.hcard3:
            case R.id.ccard3:
                index =2;
                break;
            case R.id.hcard4:
            case R.id.ccard4:
                index = 3;
                break;
        }
        ImageButton previous;
        if(isHuman){
           previous =  indexToButton(humanHandIds, viewHandler.selectCard(index));
        } else{
            previous =  indexToButton(compHandIds, viewHandler.selectCard(index));
        }
        if(previous != null && previous != chosen){

            toggleButtonColor(previous);
            setSubmitButton(true);
        }

        if(isSelected(chosen)){
            setSubmitButton(true);
        } else {
            setSubmitButton(false);
        }

    }

    public void tableCardClick(View view){
        ImageButton chosen = findViewById(view.getId());

        toggleButtonColor(chosen);
    }

    //TODO: make toggle button color use this function
    private boolean isSelected (ImageButton ref){
        if(ref == null){
            return false;
        }
        ColorDrawable background = (ColorDrawable)ref.getBackground();
        int bgID = background.getColor();
        return bgID == selectedColor;
    }
    private void toggleButtonColor(ImageButton ref){
        if(ref == null){
            return;
        }
        ColorDrawable background = (ColorDrawable)ref.getBackground();
        int bgID = background.getColor();

        if(bgID == selectedColor){
            ref.setBackgroundColor(normalColor);
        } else{
            ref.setBackgroundColor(selectedColor);
        }

    }

    private int intAsDP(int target){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, target,
                getResources().getDisplayMetrics());
    }

    private ImageButton indexToButton(Vector<Integer> listOfIds, int index){
        if(index < 0 || index >= listOfIds.size()){
            return null;
        }
        return findViewById(listOfIds.get(index));
    }

    private void setClickableForVector(Vector<Integer> buttonIds, boolean value){
        for(int i =0; i < buttonIds.size(); i++){
            ImageButton current = findViewById(buttonIds.get(i));
            current.setClickable(value);
        }
    }

    private void setSubmitButton(boolean asConfirm){
        Button submit = findViewById(R.id.submitButton);
        if(asConfirm){
            submit.setBackgroundColor(Color.GREEN);
            submit.setText(R.string.confirmButtonText);
        } else{
            submit.setBackgroundColor(Color.LTGRAY);
            submit.setText(R.string.menuButtonText);
        }
    }

}