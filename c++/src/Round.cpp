#include "Round.h"

/* *********************************************************************
Function Name: Round
Purpose: To intialize a new round object with a coin toss
Parameters:
			None
Return Value: Constructor
Local Variables:
			input, what the user called in the coin toss
			guessedHeads a bool to store if they guessed heads, for cleaner code

Algorithm:
			1) Flip coin
			2) See if the result matches
			3) Intialize rest of member fields
Assistance Received: none
********************************************************************* */
Round::Round() {
	vector<char> answers;
	answers.push_back('h');
	answers.push_back('t');
	char input = Client::getCharInput("(H)eads or (T)ails?", answers);


	bool guessedHeads = false;
	if (input == 'h') {
		guessedHeads = true;
	}

	srand((unsigned int)time(NULL)); //Explict conversation to unsigned int to remove warning
	int randNum = rand() % 100;
	if (randNum % 2 == 0) { //Coin landed on heads
		if (guessedHeads) {
			Client::outputString("Heads! Player goes first");
			startingPlayer = HUMAN_PLAYER;
		}
		else {
			Client::outputString("Heads! Computer goes first");
			startingPlayer = COMPUTER_PLAYER;
		}	
	}
	else { //Coin landed on tails
		if (guessedHeads) {
			Client::outputString("Tails! Computer goes first");
			startingPlayer = COMPUTER_PLAYER;
		}
		else {
			Client::outputString("Tails! Player goes first");
			startingPlayer = HUMAN_PLAYER;
		}

	}
	intializeRound();
}

Round::Round(bool humanFirst) {
	if (humanFirst) {
		startingPlayer = HUMAN_PLAYER;
	}
	else {
		startingPlayer =  COMPUTER_PLAYER;
	}

	intializeRound();
}


Round::Round(bool humanFirst, bool loadFromSave) {
	//TODO fix dupe code
	if (humanFirst) {
		startingPlayer = HUMAN_PLAYER;
	}
	else {
		startingPlayer = COMPUTER_PLAYER;
	}
	loadSave = loadFromSave;

}

void Round::intializeRound() {
	playerScores[HUMAN_PLAYER] = 0;
	playerScores[COMPUTER_PLAYER] = 0;
	roundOver = false;
	playerLastCapture = -1;
	loadSave = false;
}


/* *********************************************************************
Function Name: playRound()
Purpose: To play the round to completion
Parameters:
			None
Return Value: Void
Local Variables:
			Table* table, the table object running the game

Algorithm:
			1) Create the table, and load from save if specified
			2) Run cycles (each player plays) in table until the round ends
			3) Retrieve the scores from the table object
			4) Output scores
			5) Clean up local vars
Assistance Received: none
********************************************************************* */
void Round::playRound() {
	Table* table = NULL;
	//Intialize the object
	if (loadSave) {
		//If save, load from save
		table = new Table((startingPlayer == HUMAN_PLAYER), true);
	}
	else {
		//Create a table from no save file
		table = new Table((startingPlayer == HUMAN_PLAYER));
	}
	 
	//Run cycles until the round completes in the table
	while (table->runCycle() == false);

	//Get the scores from the table
	playerScores[HUMAN_PLAYER] = table->getHumanScore();
	playerScores[COMPUTER_PLAYER] = table->getComputerScore();
	

	//Output the scores, and get the player who captured last
	Client::outputString("Round Scores: Human: " + to_string(playerScores[HUMAN_PLAYER]) + "  Computer: " + to_string(playerScores[COMPUTER_PLAYER]) + "\n");
	playerLastCapture = table->getPlayerWhoCapturedLast();

	//Delete the object
	delete table;

}