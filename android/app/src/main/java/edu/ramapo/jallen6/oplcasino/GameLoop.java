package edu.ramapo.jallen6.oplcasino;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
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
import android.widget.Space;
import android.widget.TextView;
import java.util.Collections;
import java.util.Vector;

import static edu.ramapo.jallen6.oplcasino.PlayerActions.Build;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Capture;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Invalid;
import static edu.ramapo.jallen6.oplcasino.PlayerActions.Trail;

public class GameLoop extends AppCompatActivity {

    //The strings used to pass data in intents
    public static final String humanFirstExtra = "humanFirst";
    public static final String fromSaveGameExtra = "fromSaveGame";
    public static final String humanPlayerStartScore = "humanScoreStart";
    public static final String compPlayerStartScore = "compScoreStart";


    RoundView currentRoundView;
    Round   currentRound;
    final int selectedColor = Color.CYAN;
    final int normalColor = Color.WHITE;
    private Vector<Integer> humanHandIds;
    private Vector<Integer> compHandIds;
   // private Vector<Integer> tableButtonIds;

    private Vector<ImageButton> humanHandButtons;
    private Vector<ImageButton> compHandButtons;
    private Vector<View>        tableButtons;

    private boolean humanButtonsAreClickable;

    @Override
    /**
     * Create either a new or save game
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_loop);


        //Get the information which was passed in
        Intent intent = getIntent();
        boolean humanStarting = intent.getBooleanExtra(humanFirstExtra, true);
        boolean loadedSaveFile = intent.getBooleanExtra(fromSaveGameExtra, false);
        int[] startScores = new int[2];
        startScores[0] = intent.getIntExtra(humanPlayerStartScore, 0);
        startScores[1] = intent.getIntExtra(compPlayerStartScore, 0);


        //Init the static Log and read the scene views into dynamic structures
        ActionLog.init();
        initDisplayCardVariables();

        //Load either a save game or a new game
        if(loadedSaveFile){
            loadSavedData(humanStarting, startScores);
        } else{
            startFreshGame(humanStarting, startScores);
        }



        //If human is first set the human UI visible and AI invisible
        if(humanStarting){
            findViewById(R.id.roundAskForHelp).setVisibility(View.VISIBLE);
            findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
            setClickableForVector(humanHandButtons, true);
            setClickableForVector(tableButtons, true);
            humanButtonsAreClickable = true;
        }else{
            //Set all human buttons not clickable
            setClickableForVector(humanHandButtons, false);
            setClickableForVector(tableButtons, false);
            humanButtonsAreClickable = false;
        }

        //Update the UI elements to the starting position
        setPlayerLabel(humanStarting);
        updateLogButton();


        //Set up the check change listener for the radio buttons to call the correct function
        RadioGroup radio = findViewById(R.id.actionRadio);

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int id) {
                onRadioButtonChange(id);
            }
        });


        //Display the deck and set cards clickable
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

    /**
     * Starts a brand new round
     * @param humanStarting If the human player is starting
     * @param scores The scores of the round starting (should be zero)
     */
    private void startFreshGame(boolean humanStarting, int[] scores){
        currentRound = new Round(0, humanStarting, scores);
        currentRoundView = new RoundView(currentRound);
        initNewGameDisplayCards();

        ActionLog.addLog("New game started!");
    }

    /**
     *  Load a game from a save state
     * @param humanStarting If the human is starting
     * @param scores The current scores
     */
    private void loadSavedData(boolean humanStarting, int[] scores){
        //Start the round with save data
        currentRound = new Round(Serializer.getRoundNum(), humanStarting, scores);
        currentRoundView = new RoundView(currentRound);

        ActionLog.addLog("Save game Loaded!");
        //Clear the loaded file from memory
        Serializer.clearLoadedFile();

        //Display the current model
        initSaveGameCards();

        displayPile((LinearLayout) findViewById(R.id.humanPileLayout),
                    currentRoundView.getHumanPileHandler());

        displayPile((LinearLayout) findViewById(R.id.compPileLayout),
                currentRoundView.getComputerPileHandler());

    }

    /**
     * Updates the log button to the most reason action
     */
    private void updateLogButton(){
        ((Button)findViewById(R.id.logButton)).setText(ActionLog.getLast());
    }

    /**
     * Make a pop up window of the Action Log to show all past moves
     * @param view The button which was clicked
     */
    public void openActionLog(View view){

        Intent intent = new Intent(this, ActionLogPopup.class);
        //Never read response code so it doesn't matter
        startActivityForResult(intent,RESULT_CANCELED);

    }

    /**
     * Open the turn menu to save or quit
     * @param view The view which was clicked
     */
    public void openTurnMenu(View view){
        Intent intent = new Intent(this, TurnMenu.class);
        //This code matters as this response gets checked
        startActivityForResult(intent, RESULT_FIRST_USER);

    }

    /**
     * Get the result of an activity that closed
     * @param requestCode The code which it was spawned with
     * @param resultCode The resulting answer
     * @param data The intent which was sent back
     */
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


                //Add a delay to closing the app to give time for the save
                // file to write
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        finishAffinity();
                        System.exit(0);
                    }
                }, 1000);


            }
        }
    }

    /**
     * Get and display help for the human
     * @param view The view being clicked
     */
    public void generateHumanHelp(View view){
        currentRound.generateHelpTip();
        updateLogButton();
    }

    /**
     * Update the view for the Deck scroll
     */
    public void updateDeckScroll(){
        //Get the views
        DeckView deckHandler = currentRoundView.getDeckViewHandler();
        LinearLayout view = findViewById(R.id.deckLayout);
        TextView label = findViewById(R.id.deckLabel);

        //Set the current size
        label.setText("Deck:\n"+ deckHandler.size() + "Cards");

        //TODO: Find a way to clear or update the view
        //Delete all the views and add the label
        deckHandler.createViewsFromModel();
        view.removeAllViewsInLayout();
        view.addView(label);

        //Add each card in the deck
        for(int i =0; i < deckHandler.size(); i++){
            ImageButton current = generateButton();
            current.setClickable(false);
            deckHandler.displayCard(current,i);
            view.addView(current);
        }
    }

    /**
     * Set all cards in a vector of buttons to be clickable based on the turn
     * @param humanTurn If its the humans turn or not
     */
    private void setClickabilityForMove(boolean humanTurn) {
        if (humanTurn) {
            //Make hand and table clickable
            setClickableForVector(humanHandButtons, true);
            setClickableForVector(tableButtons, true);
            humanButtonsAreClickable = true;
            setSubmitButton(false, true);
        } else {
            //For computer
            setClickableForVector(humanHandButtons, false);
            setClickableForVector(tableButtons, false);
            humanButtonsAreClickable = false;
            setSubmitButton(true, false);
        }

    }

    /**
     * Set up the graphic buttons and supporting UI based on the player going
     */
    private void setUpButtonsForNextPlayer() {
        //Toggle the current state of toggles
        setClickabilityForMove(!humanButtonsAreClickable);
        setPlayerLabel(humanButtonsAreClickable);
        Button helpButton = findViewById(R.id.roundAskForHelp);
        if(humanButtonsAreClickable){

            //Display human ui
            helpButton.setVisibility(View.VISIBLE);
            //Action radio stays hidden by default when entering player. so no entry
            findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
           // setClickableForVector(tableButtonIds, true);
        } else{
            //Dispaly comp ai
            helpButton.setVisibility(View.INVISIBLE);
            findViewById(R.id.actionRadio).setVisibility(View.INVISIBLE);
            findViewById(R.id.compSwitchScroll).setVisibility(View.VISIBLE);
            //setClickableForVector(tableButtonIds, false);
        }
    }

    /**
     * Create a new ImageButton with standard layout parameters
     * @return The new button which was created
     */
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

    /**
     * Create a linear layout, with one button and N buttons
     * @param buttonAmount Amount of image buttons required
     * @return The specifed layout
     */
    public LinearLayout generateBuildLayout(int buttonAmount){
        //Make a new layout and set default parms
        LinearLayout buildLayout = new LinearLayout(this);
        buildLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        buildLayout.setOrientation(LinearLayout.HORIZONTAL);
        buildLayout.setId(View.generateViewId());

        //Create the button which contains the data
        Button valueButton = new Button(this);
        valueButton.setId(View.generateViewId());
        buildLayout.addView(valueButton);

        //Make all the image buttons
        for(int i =0; i < buttonAmount; i++){
            ImageButton current = generateButton();
            current.setClickable(false);
            buildLayout.addView(current);

        }
        //Set the background and clickListener
        buildLayout.setBackgroundColor(normalColor);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableCardClick(view);
            }
        };

        buildLayout.setOnClickListener(clickListener);
        return buildLayout;


    }

    /**
     * Creates and adds an ImageButton to the table
     * @return A reference to the added ImageButton
     */
    private ImageButton addButtonToTable() {

        //Make a new button and bind the click listener
        ImageButton newButton = generateButton();
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableCardClick(view);
            }
        };

        newButton.setOnClickListener(clickListener);
        //tableButtonIds.add(newButton.getId());
        //Add to data structure
        tableButtons.add(newButton);

        //and to table
        LinearLayout view = findViewById(R.id.tableScroll);
        view.addView(newButton);
        //Return a reference to the button
        return newButton;
    }

    /**
     * Remove the specifed buttons from the table
     * @param targets Indices of values to be removed
     * @param addToPile If they should be added to a player pile
     */
    private void removeButtonsFromTable(Vector<Integer> targets, boolean addToPile){
        //If no targets, exit
        if(targets == null){
            return;
        }
        //Ensure data is sorted correctly
        Collections.sort(targets, Collections.<Integer>reverseOrder());

        //Get the view
        LinearLayout view = findViewById(R.id.tableScroll);
        //For every target
        for(int i =0; i < targets.size(); i++){

            int currentIndex = targets.get(i);
            //+1 is to skip over the label
            view.removeViewAt(currentIndex+1);
            tableButtons.removeElementAt(currentIndex);
            //If add to pile, add to who captured last
            if(addToPile){
                if(currentRound.getLastCapturer() == PlayerID.humanPlayer){
                    addCardToPile(true);
                } else{
                    addCardToPile(false);
                }
            }

        }
    }

    /**
     * Does an update call to the correct pile (Wrapper)
     * @param forHuman If for the human pile
     */
    private void addCardToPile(boolean forHuman){
        LinearLayout view;
        HandView pile;
        //Get the correct layouts
        if(forHuman){
            view = (LinearLayout)findViewById(R.id.humanPileLayout);
            pile = currentRoundView.getHumanPileHandler();
        } else{
            //computer here
            view = (LinearLayout)findViewById(R.id.compPileLayout);
            pile = currentRoundView.getComputerPileHandler();
        }

        //Call the update function
        displayPile(view, pile);

    }

    /**
     *  Have a pile update graphically
     * @param layout The layout to update
     * @param pile The pile to reference
     */
    private void displayPile(LinearLayout layout, HandView pile){
        pile.createViewsFromModel();

        //Get the label and update the text
        TextView label = (TextView) layout.getChildAt(0);
        String currentLabel = label.getText().toString();
        //Get where the '(' is located, and update the set of cards
        int parenPoint = currentLabel.indexOf('(');
        if(parenPoint < 0){
            label.setText(currentLabel + "\n("+Integer.toString(pile.size())+" cards)");
        }else{
            label.setText(currentLabel.substring(0,parenPoint -1) +
                    "\n("+Integer.toString(pile.size())+" cards)");
        }


        //Get all cards that aren't the index
        int startingIndex = layout.getChildCount() - 1;
        for(int i = startingIndex; i < pile.size(); i++){
            ImageButton newCard = generateButton();
            newCard.setClickable(false);
            pile.displayCard(newCard, i);
            layout.addView(newCard);
        }
    }

    /**
     * Overloaded wrapper
     * @param humanPile If the human goes first
     */
    private void displayPile (boolean humanPile){


        //Calls with full parameters
        if(humanPile){
            displayPile((LinearLayout) findViewById(R.id.humanPileLayout),
                    currentRoundView.getHumanPileHandler());
        } else{
            displayPile((LinearLayout) findViewById(R.id.compPileLayout),
                    currentRoundView.getComputerPileHandler());
        }


    }


    /**
     * Event handler for when a different radio button is clicked
     * @param id The id of the clicked button
     */
    public void onRadioButtonChange(int id){

        switch (id){
            case R.id.captureRadio:
                //Select the required cards and inform the player object
                selectRequiredCards();
                currentRound.setMoveActionForCurrentPlayer(Capture);
                break;
            case R.id.buildRadio:
                //Clear selected cards and inform player
                clearTableSelection();
                currentRound.setMoveActionForCurrentPlayer(Build);
                break;
            case R.id.trailRadio:
                //Clear selected cards and inform player
                clearTableSelection();
                currentRound.setMoveActionForCurrentPlayer(Trail);
                break;
            default:
                currentRound.setMoveActionForCurrentPlayer(Invalid);
                break;
        }


    }

    /**
     * Updated the hand buttons by either moving down or setting all four visible
     * @param forHuman For the human hand
     * @param resetHand If to reset the hands
     */
    private void updateHandButtons(boolean forHuman, boolean resetHand) {
        if (currentRound == null) {
            return;
        }

        //Get the correct HandView and buttons
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

        //Deselect all and set visible
        for (int i = 0; i < buttons.size(); i++) {
            ImageButton current = buttons.get(i);
            current.setBackgroundColor(normalColor);
            if (current.getVisibility() == View.INVISIBLE) {
                invisibleButtons++;
                current.setVisibility(View.VISIBLE);
            }
        }

        int validButtons;
        //If reseting, mark all as valid regardless of previous state
        if (resetHand) {
            validButtons = buttons.size();
        }else{
            validButtons = buttons.size() - invisibleButtons;
        }

        //Make valid buttons visible
        for (int i = 0; i < validButtons; i++) {
            ImageButton current = buttons.get(i);
            handler.displayCard(current, i);
        }

        //Make all others invisible
        for (int i = validButtons; i < buttons.size(); i++) {
            ImageButton current = buttons.get(i);
            current.setVisibility(View.INVISIBLE);
        }

    }


    /**
     * Load the button ids for the ones created in XML into vectors
     */
    private void initDisplayCardVariables(){

        //Get the human Ids and Buttons
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


        //Do the same for the computer ids and buttons
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

        //Create the table buttons, and this gets filled as time goes on
        tableButtons = new Vector<View>(4,4);

    }

    /**
     * Gives buttons their display value when a new game loads
     */
    private void initNewGameDisplayCards(){
        HandView handler = currentRoundView.getHumanHandHandler();

        //Set the human buttons
        handler.displayCard((ImageButton) findViewById(R.id.hcard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.hcard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.hcard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.hcard4), 3);

        handler = currentRoundView.getComputerHandHandler();

        //Then the computer
        handler.displayCard((ImageButton) findViewById(R.id.ccard1), 0);
        handler.displayCard((ImageButton) findViewById(R.id.ccard2), 1);
        handler.displayCard((ImageButton) findViewById(R.id.ccard3), 2);
        handler.displayCard((ImageButton) findViewById(R.id.ccard4), 3);
        setClickableForVector(compHandButtons, false);

        //Then draw the table
        handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < 4; i++){
            handler.displayCard(addButtonToTable(), i);
        }

        setSubmitButton(false, false);


    }

    /**
     * Display the current state of the hands and table from a save file
     */
    private void initSaveGameCards(){

        HandView handler = currentRoundView.getHumanHandHandler();

        //Set the human buttons
        for(int i =0; i < handler.size(); i++){
            handler.displayCard(humanHandButtons.get(i), i);
        }
        //If not in the hand, then no card is there
        for(int i = handler.size(); i < humanHandButtons.size(); i++){
            humanHandButtons.get(i).setVisibility(View.INVISIBLE);
        }

        //Then the computer
        handler = currentRoundView.getComputerHandHandler();
        for(int i =0; i < handler.size();i++){
            handler.displayCard(compHandButtons.get(i), i);
        }

        for(int i = handler.size(); i < compHandButtons.size(); i++){
            compHandButtons.get(i).setVisibility(View.INVISIBLE);
        }


        //Then all the tables
        handler = currentRoundView.getTableHandHandler();
        for(int i =0; i < handler.size();i++){
            int requireButtons = handler.getNeededButtonForIndex(i);

            //If requireButtons is one, then it must be a card and not a build
            if(requireButtons == 1){
                handler.displayCard(addButtonToTable(), i);
            } else{
               // handler.displayBuild(generateBuildLayout(requireButtons), i);
                LinearLayout newBuild = generateBuildLayout(requireButtons);

                handler.displayBuild(newBuild, i);
                ((LinearLayout) findViewById(R.id.tableScroll)).addView(newBuild);
                tableButtons.add(newBuild);
            }
        }

        //Make computer not clickable
        setClickableForVector(compHandButtons, false);

    }

    /**
     * Clears all selected cards from the table
     */
    private void clearTableSelection(){
        //Tell the view they are no longer selected
        currentRoundView.getTableHandHandler().unSelectAllCards();


        for(int i =0; i < tableButtons.size(); i++){

            //For every view on table, make color normal, and set clickable
            View current = tableButtons.get(i);
            current.setBackgroundColor(normalColor);
            current.setClickable(true);
        }
        //Allow trails at the controller level (Model can still reject it)
        ((RadioButton) findViewById(R.id.trailRadio)).setClickable(true);
    }

    /**
     * Select all cards a player is required to capture on the table
     */
    private void selectRequiredCards(){
        //Ask the model for the required cards
        Vector<Integer> targetCards = currentRound.findMatchingIndexOnTable();
        HandView handler = currentRoundView.getTableHandHandler();

        for(int i =0; i < targetCards.size(); i++){
            //For each of them, mark them selected and unclickable so they cannot be deselected
            View current = tableButtons.get(targetCards.get(i));
            current.setBackgroundColor(selectedColor);
            current.setClickable(false);
            handler.selectCard(targetCards.get(i));

        }

        //If any cards were selected, disallow trailing and select capture
        if(currentRound.getSelectedTableSize() > 0){

            RadioGroup rgroup = findViewById(R.id.actionRadio);
            if(rgroup.getCheckedRadioButtonId() != R.id.captureRadio){
                rgroup.check(R.id.captureRadio);
            }
            ((RadioButton) findViewById(R.id.trailRadio)).setClickable(false);
        }
    }

    /**
     * Display the specified card or build
     * @param view The view to be displayed
     */
    public void displayCard(View view) {
        HandView viewHandler = null;

        boolean isHuman = false;
        //Get the correct handler
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
        //Find the selected button
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
        //Get the last selected button
        ImageButton previous;
        if(isHuman){
           previous =  indexToButton(humanHandIds, viewHandler.selectCard(index));
        } else{
            previous =  indexToButton(compHandIds, viewHandler.selectCard(index));
        }

        // If no previous or different mark it as selected
        if(previous != null && previous != chosen){

            toggleButtonColor(previous);
            setSubmitButton(true, isHuman);
        }

        //Clear any selected cards
        clearTableSelection();

        //If selected a new card
        if(isSelected(chosen)){
            //Mark it and get required cards
            selectRequiredCards();
            setSubmitButton(true, isHuman);
        } else {
            //Otherwise unselected
            setSubmitButton(false, isHuman);
        }

    }

    /**
     * Handles a click on any of the table cards and builds
     * @param view The card which was clicked
     */
    public void tableCardClick(View view){
        View chosen = findViewById(view.getId());

        //Toggle the color, and select
        toggleButtonColor(chosen);
        int index = tableButtons.indexOf(view);
        if(isSelected(chosen)){
            currentRoundView.getTableHandHandler().selectCard(index);
        }else{
            //Not sure what this was suppose to be, possibly an deselect?
            //currentRound.getTableHandHandler().
        }
        //
    }

    /**
     * Get the color of the background of the passed background
     * @param v The view to check
     * @return The id of the color of the background
     */
    private int getBackgroundColorID(View v){
        if(v == null){
            return -1;
        }
        //Get the background as a color drawable, then return its color
        ColorDrawable background = (ColorDrawable)v.getBackground();
        return background.getColor();
    }

    /**
     * See if a view is selected based on the background color
     * @param ref The view being checked
     * @return True if selected, false if not
     */
    private boolean isSelected (View ref){
        if(ref == null){
            return false;
        }

        return getBackgroundColorID(ref) == selectedColor;
    }


    /**
     * Toggle the button color from normal to selected
     * @param ref The view being selected
     */
    private void toggleButtonColor(View ref){
        if(ref == null){
            return;
        }

        //Toggle the color of the button
        if(isSelected(ref)){
            ref.setBackgroundColor(normalColor);
        } else{
            ref.setBackgroundColor(selectedColor);
        }
        return;

    }

    /**
     * Converts a number to dp representation of it
     * @param target The inputted value
     * @return The converted value
     */
    private int intAsDP(int target){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, target,
                getResources().getDisplayMetrics());
    }

    /**
     * Get the button at the index of the vector
     * @param listOfIds All the ids of the buttons
     * @param index The index to select
     * @return The Imagebutton or null
     */
    private ImageButton indexToButton(Vector<Integer> listOfIds, int index){
        if(index < 0 || index >= listOfIds.size()){
            return null;
        }
        return findViewById(listOfIds.get(index));
    }


    /**
     * Sets all buttons in a vector as clickable or not
     * @param buttons The vector to change
     * @param value The value to set
     */
    private void setClickableForVector(Vector<? extends View> buttons, boolean value){

        for(int i =0; i < buttons.size(); i++){
            View current = buttons.get(i);
            current.setClickable(value);
        }
    }

    /**
     * Set the submit button to confirm or menu based on settings
     * @param asConfirm If confirm or menu
     * @param humanMove If its the human move or not (For ui)
     */
    private void setSubmitButton(boolean asConfirm, boolean humanMove){

        //NOTE: Some functions that call asConfirm as false, always pass false for humanMove
        Button submit = findViewById(R.id.submitButton);
        RadioGroup actionGroup = findViewById(R.id.actionRadio);
        CheckBox compCheck = findViewById(R.id.compMovecheckBox);


        //If as confirm and a move is allowed
        if((asConfirm && humanMove) || (asConfirm && compCheck.isChecked())){
            //Set green and update the text
            submit.setBackgroundColor(Color.GREEN);
            submit.setText(R.string.confirmButtonText);
            //Update the visibility
            if(humanMove){
                actionGroup.setVisibility(View.VISIBLE);
            }else{

            }

            //Change the onclick
            View.OnClickListener clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleSubmitClick(view);
                }
            };
            submit.setOnClickListener(clickListener);

        } else{
            //Otherwise set as menu button
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

    /**
     * Set the label for the player based on the current player
     * @param humanMove If the move is for the human or not
     */
    private void setPlayerLabel (boolean humanMove){
        //Get the labels
        TextView humanLabel = findViewById(R.id.hHand);
        TextView compLabel = findViewById(R.id.cHand);


        if(humanMove){
            //Set the text for the current player
            humanLabel.setText(R.string.bottomPlayerNameCurrent);
            compLabel.setText(R.string.topPlayer);
            //Updates the color
            humanLabel.setBackgroundColor(Color.YELLOW);
            humanLabel.setTextColor(Color.BLACK);
            compLabel.setBackgroundColor(Color.TRANSPARENT);
            compLabel.setTextColor(getResources().getColor(R.color.Gold, null));
        }else{
            //Set the text for the current player
            humanLabel.setText(R.string.bottomPlayerName);
            compLabel.setText(R.string.topPlayerNameCurrent);
            //Update the colors
            humanLabel.setBackgroundColor(Color.TRANSPARENT);
            humanLabel.setTextColor(getResources().getColor(R.color.Gold, null));
            compLabel.setBackgroundColor(Color.YELLOW);
            compLabel.setTextColor(Color.BLACK);
        }



    }

    /**
     *  Handles the click of the submit button for both players
     * @param view The button which was clicked
     *             Yes this should be multiple different files
     */
    public void handleSubmitClick(View view){
        if(view.getId() != R.id.submitButton){
            return;
        }


        Button submit = (Button) view;
        //If as submit
        if(getBackgroundColorID(submit) == Color.GREEN){
            //Do move

            boolean moveResultState = currentRound.doNextPlayerMove();
            //Update the log with the result
            updateLogButton();
            if(moveResultState){
                //Valid move


                if(currentRound.getLastAction() == PlayerActions.Trail){
                    //If trail, only add cards to the table
                    currentRoundView.getTableHandHandler().displaySelected(addButtonToTable());
                } else if(currentRound.getLastAction() == Build){
                    //Handle builds here
                    removeButtonsFromTable(currentRound.getLastPlayerMove().getTableCardIndices(), false);

                    //Get all the buttons needed for the layout
                    HandView table = currentRoundView.getTableHandHandler();
                    int cardsNeeded = table.getNeededButtonForIndex(table.getLastIndex());
                    LinearLayout newBuild = generateBuildLayout(cardsNeeded);

                    //Display the new build
                    table.displayBuild(newBuild, table.getLastIndex());
                    ((LinearLayout) findViewById(R.id.tableScroll)).addView(newBuild);
                    tableButtons.add(newBuild);

                    //Condense table and check
                    Vector<Integer> removedInCondense = currentRound.condenseBuilds(
                            currentRound.getTable().peekCard(
                                    currentRound.getTable().size()-1).getValue());

                    //If condensed, removed old builds and add new one
                    if(removedInCondense.size() > 0){
                        removeButtonsFromTable(removedInCondense, false);
                        cardsNeeded = table.getNeededButtonForIndex(table.getLastIndex());
                        newBuild = generateBuildLayout(cardsNeeded);

                        table.displayBuild(newBuild, table.getLastIndex());
                        ((LinearLayout) findViewById(R.id.tableScroll)).addView(newBuild);
                        tableButtons.add(newBuild);
                    }

                } else{
                    //Capture
                    //Remove the selected cards
                    removeButtonsFromTable(currentRound.getLastPlayerMove().getTableCardIndices(),true);
                }

                int playedCardIndex = currentRound.getLastPlayerMove().getHandCardIndex();
                //Removed played card from correct hand
                if(humanButtonsAreClickable){
                    findViewById(humanHandIds.get(playedCardIndex)).setVisibility(View.INVISIBLE);
                    updateHandButtons(true, false);

                } else{
                    findViewById(compHandIds.get(playedCardIndex)).setVisibility(View.INVISIBLE);
                    updateHandButtons(false, false);

                }

                //If both players have a full hand
                if(currentRoundView.getHumanHandHandler().size() == 4 && currentRoundView.getComputerHandHandler().size() ==4){
                    //Reset the hands graphically
                    currentRoundView.updateViews();
                    updateHandButtons(true, true);
                    updateHandButtons(false, true);

                    //Updated both hand views at the same time
                    HandView human = currentRoundView.getHumanHandHandler();
                    HandView comp = currentRoundView.getComputerHandHandler();
                    for(int i=0; i < 4; i++){
                        human.displayCard(humanHandButtons.get(i), i);
                        comp.displayCard(compHandButtons.get(i), i);
                    }


                }

                //If round is over, end the round
                if(currentRound.isRoundOver()){
                    endRound();
                    return;
                }

                //Update the rest of the graphics
                setUpButtonsForNextPlayer();
                clearTableSelection();
                updateDeckScroll();

            } else{
                // invalid move
                //Reset buttons
                updateHandButtons(true, false);
                setSubmitButton(false, false);
                clearTableSelection();


            }

        }else{
            //Show menu
            // Therefore do nothing
            return;
        }
    }


    /**
     * This function is called to end the round once the game is over
     */
    private void endRound(){

        //Remove everything from the table layout
       LinearLayout tableLayout = findViewById(R.id.tableScroll);
       tableLayout.removeAllViewsInLayout();

       //Hide elements which are no longer needed
       findViewById(R.id.hHand).setVisibility(View.INVISIBLE);
       findViewById(R.id.cHand).setVisibility(View.INVISIBLE);
       findViewById(R.id.compSwitchScroll).setVisibility(View.INVISIBLE);
       findViewById(R.id.submitButton).setVisibility(View.INVISIBLE);


       //Add a space in the linear layour
        Space whiteSpace = new Space(this);
        whiteSpace.setLayoutParams(new
                LinearLayout.LayoutParams(intAsDP(125),
                ViewGroup.LayoutParams.MATCH_PARENT));
        tableLayout.addView(whiteSpace);

        //Create the button to go to the score screen
        Button scoreButton = new Button(this);
        scoreButton.setId(View.generateViewId());
        scoreButton.setText("Score Game!");
        scoreButton.setBackgroundResource(R.drawable.rounded_rectangle);
        scoreButton.setLayoutParams(new
                LinearLayout.LayoutParams(intAsDP(125),
                ViewGroup.LayoutParams.WRAP_CONTENT));


        tableLayout.addView(scoreButton);
        tableLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);

        //Create its onclick listener
        View.OnClickListener scoreGame = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScoreScreen();
            }
        };
        scoreButton.setOnClickListener(scoreGame);



    }

    /**
     * Creates the intents, and moves to the score screen after passing data
     */
    private  void startScoreScreen(){
        //Pass required scoring information
        ScoreScreen.setPlayers(currentRound.getPlayers());
        ScoreScreen.setLastCap(currentRound.getLastCapturer());
        Intent intent = new Intent(this, ScoreScreen.class);
        //End and move
        startActivity(intent);
        finish();
    }
}