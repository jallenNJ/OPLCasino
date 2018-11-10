package edu.ramapo.jallen6.oplcasino;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.PlayerActions.Build;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Capture;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Invalid;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Trail;

public class GameLoop extends AppCompatActivity {
    RoundView currentRoundView;
    Round   currentRound;
    final int selectedColor = Color.CYAN;
    final int normalColor = Color.WHITE;
    private Vector<Integer> humanHandIds;
    private Vector<Integer> compHandIds;
    private Vector<Integer> tableButtonIds;

    private Vector<ImageButton> humanHandButtons;
    private Vector<ImageButton> compHandButtons;

    private boolean humanButtonsAreClickable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);

        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra("humanFirst", true);
        currentRound = new Round(0, humanStarting);
        currentRoundView = new RoundView(currentRound);
        ActionLog.init();
        ActionLog.addLog("New game started!");
        updateLogButton();

        RadioGroup radio = findViewById(R.id.actionRadio);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int id) {
                onRadioButtonChange(id);
            }
        });


        initDisplayCards();
        updateDeckScroll();
        setClickabilityForMove(humanStarting);


    }


    private void updateLogButton(){
        ((Button)findViewById(R.id.logButton)).setText(ActionLog.getLast());
    }

    public void openActionLog(View view){

        Intent intent = new Intent(this, ActionLogPopup.class);
        startActivityForResult(intent,RESULT_CANCELED);

    }


    public void updateDeckScroll(){
        DeckView deckHandler = currentRoundView.getDeckViewHandler();
        LinearLayout view = findViewById(R.id.deckLayout);
        TextView label = findViewById(R.id.deckLabel);
        label.setText("Deck: "+ deckHandler.size() + "Cards");

        //TODO: Find a way to clear or update the view
        deckHandler.createViewsFromModel();
        view.removeAllViewsInLayout();
        view.addView(label);
        for(int i =0; i < deckHandler.size(); i++){
            ImageButton current = generateButton();
            current.setClickable(false);
            deckHandler.displayCard(current,i);
            view.addView(current);
        }
    }

    private void setClickabilityForMove(boolean humanTurn) {
        if (humanTurn) {
            setClickableForVector(humanHandIds, true);
            setClickableForVector(tableButtonIds, true);
            humanButtonsAreClickable = true;
            setSubmitButton(false, true);
        } else {
            //For computer
            setClickableForVector(humanHandIds, false);
            setClickableForVector(tableButtonIds, false);
            humanButtonsAreClickable = false;
            setSubmitButton(true, false);
        }

    }

    private void setUpButtonsForNextPlayer() {
        setClickabilityForMove(!humanButtonsAreClickable);
    }


    private int getTableCardCount() {
        LinearLayout view = findViewById(R.id.tableScroll);
        return view.getChildCount();
    }


    private ImageButton generateButton(){
        //Generate the button and give it an ID
        ImageButton newButton = new ImageButton(this);
        newButton.setId(View.generateViewId());

        //Give it a default image
        newButton.setImageResource(R.drawable.cardback);

        //Set the margins and size
        LinearLayout.LayoutParams lp = new
                LinearLayout.LayoutParams(intAsDP(50), intAsDP(80));
        lp.setMargins(20, 0, 20, 0);
        newButton.setLayoutParams(lp);

        //Ensure the background is the normal color and the crop is correct
        newButton.setBackgroundColor(normalColor);
        newButton.setScaleType(ImageView.ScaleType.CENTER_CROP);

        return newButton;

    }


    private ImageButton addButtonToTable() {

        ImageButton newButton = generateButton();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableCardClick(view);
            }
        };
        newButton.setOnClickListener(clickListener);
        tableButtonIds.add(newButton.getId());

        LinearLayout view = findViewById(R.id.tableScroll);
        view.addView(newButton);
        return newButton;
    }


    private void removeButtonsFromTable(Vector<Integer> targets){
        if(targets == null){
            return;
        }
        Collections.sort(targets, Collections.<Integer>reverseOrder());
        LinearLayout view = findViewById(R.id.tableScroll);
        for(int i =0; i < targets.size(); i++){

            ImageButton current = findViewById(tableButtonIds.get(targets.get(i)));
            view.removeView(current);
            tableButtonIds.remove(new Integer(current.getId()));
            if(currentRound.getLastCapturer() == PlayerID.humanPlayer){
                addCardToPile(true);
            } else{
                addCardToPile(false);
            }
        }
    }

    private void addCardToPile(boolean forHuman){
        LinearLayout view;
        HandView pile;
        if(forHuman){
            view = (LinearLayout)findViewById(R.id.humanPileLayout);
            pile = currentRoundView.getHumanPileHandler();
        } else{
            //computer here
            view = (LinearLayout)findViewById(R.id.compPileLayout);
            pile = currentRoundView.getComputerHandHandler();
        }

        pile.createViewsFromModel();
       // ref.setClickable(false);
        int startingIndex = view.getChildCount() - 1;
        for(int i = startingIndex; i < pile.size(); i++){
            ImageButton newCard = generateButton();
            newCard.setClickable(false);
            pile.displayCard(newCard, i);
            view.addView(newCard);
        }

    }


    public void onRadioButtonChange(int id){

        switch (id){
            case R.id.captureRadio:
                currentRound.setMoveActionForCurrentPlayer(Capture);
                break;
            case R.id.buildRadio:
                currentRound.setMoveActionForCurrentPlayer(Build);
                break;
            case R.id.trailRadio:
                currentRound.setMoveActionForCurrentPlayer(Trail);
                break;
            default:
                currentRound.setMoveActionForCurrentPlayer(Invalid);
                break;
        }


    }
    private void updateHandButtons(boolean forHuman, boolean resetHand) {
        if (currentRound == null) {
            return;
        }
        HandView handler;
        Vector<ImageButton> buttons;
        if (forHuman) {
            handler = currentRoundView.getHumanHandHandler();
            buttons = humanHandButtons;
        } else {
            handler = currentRoundView.getComputerHandHandler();
            buttons = compHandButtons;
        }


        if (handler == null) {
            return;
        }
        int invisibleButtons = 0;
        handler.unSelectAllCards();

        for (int i = 0; i < buttons.size(); i++) {
            ImageButton current = buttons.get(i);
            current.setBackgroundColor(normalColor);
            if (current.getVisibility() == View.INVISIBLE) {
                invisibleButtons++;
                current.setVisibility(View.VISIBLE);
            }
        }

        int validButtons;
        if (resetHand) {
            validButtons = buttons.size();
        }else{
            validButtons = buttons.size() - invisibleButtons;
        }

        for (int i = 0; i < validButtons; i++) {
            ImageButton current = buttons.get(i);
            handler.displayCard(current, i);
        }

        for (int i = validButtons; i < buttons.size(); i++) {
            ImageButton current = buttons.get(i);
            current.setVisibility(View.INVISIBLE);
        }


    }

    private void initDisplayCards(){
        HandView handler = currentRoundView.getHumanHandHandler();
        humanHandIds = new Vector<Integer>(4,1);
        humanHandIds.add(R.id.hcard1);
        humanHandIds.add(R.id.hcard2);
        humanHandIds.add(R.id.hcard3);
        humanHandIds.add(R.id.hcard4);

        humanHandButtons = new Vector<ImageButton>(4,1);
        humanHandButtons.add((ImageButton) findViewById(R.id.hcard1));
        humanHandButtons.add((ImageButton) findViewById(R.id.hcard2));
        humanHandButtons.add((ImageButton) findViewById(R.id.hcard3));
        humanHandButtons.add((ImageButton) findViewById(R.id.hcard4));

        handler.displayCard((ImageButton) findViewById(R.id.hcard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.hcard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.hcard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.hcard4), 3);

        handler = currentRoundView.getComputerHandHandler();
        compHandIds = new Vector<Integer>(4,1);
        compHandIds.add(R.id.ccard1);
        compHandIds.add(R.id.ccard2);
        compHandIds.add(R.id.ccard3);
        compHandIds.add(R.id.ccard4);

        compHandButtons= new Vector<ImageButton>(4,1);
        compHandButtons.add((ImageButton) findViewById(R.id.ccard1));
        compHandButtons.add((ImageButton) findViewById(R.id.ccard2));
        compHandButtons.add((ImageButton) findViewById(R.id.ccard3));
        compHandButtons.add((ImageButton) findViewById(R.id.ccard4));


        handler.displayCard((ImageButton) findViewById(R.id.ccard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.ccard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.ccard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.ccard4), 3);
        setClickableForVector(compHandIds, false);

        handler = currentRoundView.getTableHandHandler();
        tableButtonIds = new Vector<Integer>(4,4);
        for(int i =0; i < 4; i++){
            handler.displayCard(addButtonToTable(), i);
        }

        setSubmitButton(false, false);
    }

    private void clearTableSelection(){
        currentRoundView.getTableHandHandler().unSelectAllCards();
      //  LinearLayout table = findViewById(R.id.tableScroll);
        for(int i =0; i < tableButtonIds.size(); i++){
            ImageButton current = findViewById(tableButtonIds.get(i));
            current.setBackgroundColor(normalColor);
            current.setClickable(true);
        }
        ((RadioButton) findViewById(R.id.trailRadio)).setClickable(true);
    }

    private void selectRequiredCards(){
        Vector<Integer> targetCards = currentRound.findMatchingIndexOnTable();
        HandView handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < targetCards.size(); i++){
            ImageButton current = findViewById(tableButtonIds.get(targetCards.get(i)));
            current.setBackgroundColor(selectedColor);
            current.setClickable(false);
            handler.selectCard(targetCards.get(i));

        }
        if(currentRound.getSelectedTableSize() > 0){
          //  ((RadioButton) findViewById(R.id.captureRadio)).setChecked(true);
            ((RadioGroup) findViewById(R.id.actionRadio)).check(R.id.captureRadio);
            ((RadioButton) findViewById(R.id.trailRadio)).setClickable(false);
        }
    }

    public void displayCard(View view) {
        HandView viewHandler = null;

        boolean isHuman = false;
        if((view.getTag()).toString().equals("Human")){
            viewHandler = currentRoundView.getHumanHandHandler();
            isHuman = true;
        }else{
            viewHandler = currentRoundView.getComputerHandHandler();
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
            setSubmitButton(true, isHuman);
        }

        clearTableSelection();
        if(isSelected(chosen)){

            selectRequiredCards();
            setSubmitButton(true, isHuman);
        } else {
            setSubmitButton(false, isHuman);
        }

    }

    public void tableCardClick(View view){
        ImageButton chosen = findViewById(view.getId());

        toggleButtonColor(chosen);
        int index = tableButtonIds.indexOf(view.getId());
        if(isSelected(chosen)){
            currentRoundView.getTableHandHandler().selectCard(index);
        }else{
            //currentRound.getTableHandHandler().
        }
        //
    }


    private int getBackgroundColorID(View v){
        if(v == null){
            return -1;
        }
        ColorDrawable background = (ColorDrawable)v.getBackground();
        return background.getColor();
    }

    private boolean isSelected (ImageButton ref){
        if(ref == null){
            return false;
        }
       // ColorDrawable background = (ColorDrawable)ref.getBackground();
        //int bgID = background.getColor();
        return getBackgroundColorID(ref) == selectedColor;
    }
    private void toggleButtonColor(ImageButton ref){
        if(ref == null){
            return;
        }

        if(isSelected(ref)){
            ref.setBackgroundColor(normalColor);
        } else{
            ref.setBackgroundColor(selectedColor);
        }
        return;

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

    private void setSubmitButton(boolean asConfirm, boolean humanMove){

        //NOTE: Some functions that call asConfirm as false, always pass false for humanMove
        Button submit = findViewById(R.id.submitButton);
        RadioGroup actionGroup = findViewById(R.id.actionRadio);
        if(asConfirm){
            submit.setBackgroundColor(Color.GREEN);
            submit.setText(R.string.confirmButtonText);
            if(humanMove){
                actionGroup.setVisibility(View.VISIBLE);
            }else{
                actionGroup.setVisibility(View.INVISIBLE);
            }

        } else{
            submit.setBackgroundColor(Color.LTGRAY);
            submit.setText(R.string.menuButtonText);
            actionGroup.setVisibility(View.INVISIBLE);
        }
    }


    public void handleSubmitClick(View view){
        if(view.getId() != R.id.submitButton){
            return;
        }


        Button submit = (Button) view;
        if(getBackgroundColorID(submit) == Color.GREEN){
            //Do move

           // RadioGroup radioGroup = findViewById(R.id.actionRadio);
            //radioGroup.
            //currentRound.setMoveActionForCurrentPlayer(radioGroup.se);
            boolean moveResultState = currentRound.doNextPlayerMove();
            updateLogButton();
            if(moveResultState){
                //Valid move



                //Todo:Check for round over

                if(currentRound.getLastAction() == PlayerActions.Trail){
                    currentRoundView.getTableHandHandler().displaySelected(addButtonToTable());
                } else if(currentRound.getLastAction() == Build){
                    //Handle builds here
                } else{
                    //Capture
                    removeButtonsFromTable(currentRound.getLastPlayerMove().getTableCardIndices());
                }

                int playedCardIndex = currentRound.getLastPlayerMove().getHandCardIndex();
                if(humanButtonsAreClickable){
                    findViewById(humanHandIds.get(playedCardIndex)).setVisibility(View.INVISIBLE);
                    updateHandButtons(true, false);
                } else{
                    findViewById(compHandIds.get(playedCardIndex)).setVisibility(View.INVISIBLE);
                    updateHandButtons(false, false);

                }

                if(currentRoundView.getHumanHandHandler().size() == 4 && currentRoundView.getComputerHandHandler().size() ==4){
                    currentRoundView.updateViews();
                    updateHandButtons(true, true);
                    updateHandButtons(false, true);

                    HandView human = currentRoundView.getHumanHandHandler();
                    HandView comp = currentRoundView.getComputerHandHandler();
                    for(int i=0; i < 4; i++){
                        human.displayCard(humanHandButtons.get(i), i);
                        comp.displayCard(compHandButtons.get(i), i);
                    }


                }

                setUpButtonsForNextPlayer();
                clearTableSelection();
                updateDeckScroll();
               // setSubmitButton(false. false);


            } else{
                // invalid move or round over;
                if(currentRound.isRoundOver()){

                } else{
                    updateHandButtons(true, false);
                    setSubmitButton(false, false);
                    clearTableSelection();
                }




            }

        }else{
            //Show menu
            return;
        }
    }

}