package edu.ramapo.jallen6.oplcasino;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.PlayerActions.Build;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Capture;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Invalid;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Trail;

public class GameLoop extends AppCompatActivity {
    public static final String humanFirstExtra = "humanFirst";
    public static final String fromSaveGameExtra = "fromSaveGame";


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
        boolean humanStarting = intent.getBooleanExtra(humanFirstExtra, true);
        boolean loadedSaveFile = intent.getBooleanExtra(fromSaveGameExtra, false);


        ActionLog.init();
        initDisplayCardVariables();
        if(loadedSaveFile){
            loadSavedData(humanStarting);
        } else{
            startFreshGame(humanStarting);
        }



        if(humanStarting){
            findViewById(R.id.roundAskForHelp).setVisibility(View.VISIBLE);
            findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
            setClickableForVector(humanHandIds, true);
            setClickableForVector(tableButtonIds, true);
            humanButtonsAreClickable = true;
        }else{
            setClickableForVector(humanHandIds, false);
            setClickableForVector(tableButtonIds, false);
            humanButtonsAreClickable = false;
        }
        setPlayerLabel(humanStarting);
        updateLogButton();


        RadioGroup radio = findViewById(R.id.actionRadio);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int id) {
                onRadioButtonChange(id);
            }
        });


        updateDeckScroll();

        setClickabilityForMove(humanStarting);

        View.OnClickListener compMovecheckBox = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSubmitButton(((CheckBox)(findViewById(R.id.compMovecheckBox))).isChecked(), false);

            }
        };

        findViewById(R.id.compMovecheckBox).setOnClickListener(compMovecheckBox);

    }

    private void startFreshGame(boolean humanStarting){
        currentRound = new Round(0, humanStarting);
        currentRoundView = new RoundView(currentRound);
        initNewGameDisplayCards();

        ActionLog.addLog("New game started!");
    }

    private void loadSavedData(boolean humanStarting){
        currentRound = new Round(Serializer.getRoundNum(), humanStarting);
        currentRoundView = new RoundView(currentRound);
        ActionLog.addLog("Save game Loaded!");
        Serializer.clearLoadedFile();
        initSaveGameCards();

        displayPile((LinearLayout) findViewById(R.id.humanPileLayout),
                    currentRoundView.getHumanPileHandler());

        displayPile((LinearLayout) findViewById(R.id.compPileLayout),
                currentRoundView.getComputerPileHandler());

    }

    private void updateLogButton(){
        ((Button)findViewById(R.id.logButton)).setText(ActionLog.getLast());
    }

    public void openActionLog(View view){

        Intent intent = new Intent(this, ActionLogPopup.class);
        startActivityForResult(intent,RESULT_CANCELED);

    }

    public void openTurnMenu(View view){
        Intent intent = new Intent(this, TurnMenu.class);
        startActivityForResult(intent, RESULT_FIRST_USER);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT_FIRST_USER) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                currentRound.serializeRoundState();
                //This is needed to activate the permission from the manifest
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                //Request code doesn't matter, as its never being checked to see if we get it.
                requestPermissions(permissions, 1);
                Serializer.writeToSaveFile();
           //     finishAffinity();
            //    System.exit(0);
            }
        }
    }

    public void generateHumanHelp(View view){
        currentRound.generateHelpTip();
        updateLogButton();
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
        setPlayerLabel(humanButtonsAreClickable);
        Button helpButton = findViewById(R.id.roundAskForHelp);
        if(humanButtonsAreClickable){

            helpButton.setVisibility(View.VISIBLE);
            //Action radio stays hidden by default when entering player. so no entry
            findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
        } else{
            helpButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.actionRadio).setVisibility(View.INVISIBLE);
            findViewById(R.id.compSwitchScroll).setVisibility(View.VISIBLE);
        }
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

    public LinearLayout generateBuildLayout(int buttonAmount){
        LinearLayout buildLayout = new LinearLayout(this);
        buildLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        buildLayout.setOrientation(LinearLayout.HORIZONTAL);
        buildLayout.setId(View.generateViewId());

        Button valueButton = new Button(this);
        valueButton.setId(View.generateViewId());
        buildLayout.addView(valueButton);

        for(int i =0; i < buttonAmount; i++){
            ImageButton current = generateButton();
            current.setClickable(false);
            buildLayout.addView(current);

        }
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.BLUE); //white background
        border.setStroke(15, 0xFF000000); //black border with full opacity
        buildLayout.setBackground(border);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableCardClick(view);
            }
        };

        buildLayout.setOnClickListener(clickListener);
        return buildLayout;


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

            View current = findViewById(tableButtonIds.get(targets.get(i)));
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
            pile = currentRoundView.getComputerPileHandler();
        }

        displayPile(view, pile);

    }

    private void displayPile(LinearLayout layout, HandView pile){
        pile.createViewsFromModel();
        // ref.setClickable(false);
        int startingIndex = layout.getChildCount() - 1;
        for(int i = startingIndex; i < pile.size(); i++){
            ImageButton newCard = generateButton();
            newCard.setClickable(false);
            pile.displayCard(newCard, i);
            layout.addView(newCard);
        }
    }

    private void displayPile (boolean humanPile){

        if(humanPile){
            displayPile((LinearLayout) findViewById(R.id.humanPileLayout),
                    currentRoundView.getHumanPileHandler());
        } else{
            displayPile((LinearLayout) findViewById(R.id.compPileLayout),
                    currentRoundView.getComputerPileHandler());
        }


    }


    public void onRadioButtonChange(int id){

        switch (id){
            case R.id.captureRadio:
                selectRequiredCards();
                currentRound.setMoveActionForCurrentPlayer(Capture);
                break;
            case R.id.buildRadio:
                clearTableSelection();
                currentRound.setMoveActionForCurrentPlayer(Build);
                break;
            case R.id.trailRadio:
                clearTableSelection();
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


    private void initDisplayCardVariables(){

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

        tableButtonIds = new Vector<Integer>(4,4);


    }

    private void initNewGameDisplayCards(){
        HandView handler = currentRoundView.getHumanHandHandler();

        handler.displayCard((ImageButton) findViewById(R.id.hcard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.hcard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.hcard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.hcard4), 3);

        handler = currentRoundView.getComputerHandHandler();

        handler.displayCard((ImageButton) findViewById(R.id.ccard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.ccard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.ccard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.ccard4), 3);
        setClickableForVector(compHandIds, false);

        handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < 4; i++){
            handler.displayCard(addButtonToTable(), i);
        }

        setSubmitButton(false, false);


    }

    private void initSaveGameCards(){

        HandView handler = currentRoundView.getHumanHandHandler();

        for(int i =0; i < handler.size(); i++){
            handler.displayCard(humanHandButtons.get(i), i);
        }
        for(int i = handler.size(); i < humanHandButtons.size(); i++){
            humanHandButtons.get(i).setVisibility(View.INVISIBLE);
        }

        handler = currentRoundView.getComputerHandHandler();
        for(int i =0; i < handler.size();i++){
            handler.displayCard(compHandButtons.get(i), i);
        }

        for(int i = handler.size(); i < compHandButtons.size(); i++){
            compHandButtons.get(i).setVisibility(View.INVISIBLE);
        }


        handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < handler.size();i++){
            int requireButtons = handler.getNeededButtonForIndex(i);

            if(requireButtons == 1){
                handler.displayCard(addButtonToTable(), i);
            } else{
               // handler.displayBuild(generateBuildLayout(requireButtons), i);
                LinearLayout newBuild = generateBuildLayout(requireButtons);

                handler.displayBuild(newBuild, i);
                ((LinearLayout) findViewById(R.id.tableScroll)).addView(newBuild);
                tableButtonIds.add(newBuild.getId());
            }
        }

        setClickableForVector(compHandIds, false);

    }


    private void clearTableSelection(){
        currentRoundView.getTableHandHandler().unSelectAllCards();
      //  LinearLayout table = findViewById(R.id.tableScroll);
        for(int i =0; i < tableButtonIds.size(); i++){
            //TODO, add better handling than parent view
            View current = findViewById(tableButtonIds.get(i));
            current.setBackgroundColor(normalColor);
            current.setClickable(true);
        }
        ((RadioButton) findViewById(R.id.trailRadio)).setClickable(true);
    }

    private void selectRequiredCards(){
        Vector<Integer> targetCards = currentRound.findMatchingIndexOnTable();
        HandView handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < targetCards.size(); i++){
            //TODO: Add better handling than parent view
            View current = findViewById(tableButtonIds.get(targetCards.get(i)));
            current.setBackgroundColor(selectedColor);
            current.setClickable(false);
            handler.selectCard(targetCards.get(i));

        }
        if(currentRound.getSelectedTableSize() > 0){
          //  ((RadioButton) findViewById(R.id.captureRadio)).setChecked(true);
            RadioGroup rgroup = findViewById(R.id.actionRadio);
            if(rgroup.getCheckedRadioButtonId() != R.id.captureRadio){
                rgroup.check(R.id.captureRadio);
            }
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
        View chosen = findViewById(view.getId());

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

    private boolean isSelected (View ref){
        if(ref == null){
            return false;
        }

        return getBackgroundColorID(ref) == selectedColor;
    }
    private void toggleButtonColor(View ref){
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
        //TODO: Add better handling for Builds than treating as view
        for(int i =0; i < buttonIds.size(); i++){
            View current = findViewById(buttonIds.get(i));
            current.setClickable(value);
        }
    }

    private void setSubmitButton(boolean asConfirm, boolean humanMove){

        //NOTE: Some functions that call asConfirm as false, always pass false for humanMove
        Button submit = findViewById(R.id.submitButton);
        RadioGroup actionGroup = findViewById(R.id.actionRadio);
        CheckBox compCheck = findViewById(R.id.compMovecheckBox);


        if((asConfirm && humanMove) || (asConfirm && compCheck.isChecked())){
            submit.setBackgroundColor(Color.GREEN);
            submit.setText(R.string.confirmButtonText);
            if(humanMove){
                actionGroup.setVisibility(View.VISIBLE);
            }else{

            }
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSubmitClick(view);
                }
            };
            submit.setOnClickListener(clickListener);

        } else{
            submit.setBackgroundColor(Color.LTGRAY);
            submit.setText(R.string.menuButtonText);
            actionGroup.setVisibility(View.INVISIBLE);
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openTurnMenu(view);
                }
            };
            submit.setOnClickListener(clickListener);
        }
    }

    private void setPlayerLabel (boolean humanMove){
        TextView humanLabel = findViewById(R.id.hHand);
        TextView compLabel = findViewById(R.id.cHand);

        if(humanMove){
            humanLabel.setText(R.string.bottomPlayerNameCurrent);
            compLabel.setText(R.string.topPlayer);
            humanLabel.setBackgroundColor(Color.YELLOW);
            compLabel.setBackgroundColor(Color.TRANSPARENT);
        }else{
            humanLabel.setText(R.string.bottomPlayerName);
            compLabel.setText(R.string.topPlayerNameCurrent);
            humanLabel.setBackgroundColor(Color.TRANSPARENT);
            compLabel.setBackgroundColor(Color.YELLOW);
        }



    }

    public void handleSubmitClick(View view){
        if(view.getId() != R.id.submitButton){
            return;
        }


        Button submit = (Button) view;
        if(getBackgroundColorID(submit) == Color.GREEN){
            //Do move

            boolean moveResultState = currentRound.doNextPlayerMove();
            updateLogButton();
            if(moveResultState){
                //Valid move


                if(currentRound.getLastAction() == PlayerActions.Trail){
                    currentRoundView.getTableHandHandler().displaySelected(addButtonToTable());
                } else if(currentRound.getLastAction() == Build){
                    //Handle builds here
                    removeButtonsFromTable(currentRound.getLastPlayerMove().getTableCardIndices());

                    HandView table = currentRoundView.getTableHandHandler();
                    int index = table.size()-1;
                    int cardsNeeded = table.getNeededButtonForIndex(index);
                    LinearLayout newBuild = generateBuildLayout(cardsNeeded);

                    table.displayBuild(newBuild, index);
                    ((LinearLayout) findViewById(R.id.tableScroll)).addView(newBuild);
                    tableButtonIds.add(newBuild.getId());

                } else{
                    //Capture
                    removeButtonsFromTable(currentRound.getLastPlayerMove().getTableCardIndices());
                }

                int playedCardIndex = currentRound.getLastPlayerMove().getHandCardIndex();
                if(humanButtonsAreClickable){
                    findViewById(humanHandIds.get(playedCardIndex)).setVisibility(View.INVISIBLE);
                    updateHandButtons(true, false);
                    //TODO: RADIOGROUP AND COMP SWITCH TOGGLE HERE?
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

                if(currentRound.isRoundOver()){
                    endRound();
                    return;
                }

                setUpButtonsForNextPlayer();
                clearTableSelection();
                updateDeckScroll();
               // setSubmitButton(false. false);


            } else{
                // invalid move
                updateHandButtons(true, false);
                setSubmitButton(false, false);
                clearTableSelection();


            }

        }else{
            //Show menu
            return;
        }
    }


    private void endRound(){
       /* updateHandButtons(false, false);
        updateHandButtons(true, false);
        displayPile(true);
        displayPile(false);*/

       LinearLayout tableLayout = findViewById(R.id.tableScroll);
       tableLayout.removeAllViewsInLayout();

       findViewById(R.id.hHand).setVisibility(View.INVISIBLE);
       findViewById(R.id.cHand).setVisibility(View.INVISIBLE);
       findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
       findViewById(R.id.submitButton).setVisibility(View.INVISIBLE);
       //new LayoutPa
        //ConstraintLayout.LayoutParams lp = new LayoutParams(intAsDP(800), intAsDP(90));

        //findViewById(R.id.bottomPileScroll).setLayoutParams(lp);
        //findViewById(R.id.topPileScroll).setLayoutParams(lp);

        Button scoreButton = new Button(this);
        scoreButton.setId(View.generateViewId());
        scoreButton.setText("Score Game!");
        tableLayout.addView(scoreButton);
        tableLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);

        View.OnClickListener scoreGame = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScoreScreen();
            }
        };
        scoreButton.setOnClickListener(scoreGame);



    }

    private  void startScoreScreen(){
        ScoreScreen.setPlayers(currentRound.getPlayers());
        ScoreScreen.setLastCap(currentRound.getLastCapturer());
        Intent intent = new Intent(this, ScoreScreen.class);
        startActivity(intent);
        finish();
    }
}