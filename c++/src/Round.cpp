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
	playerThatWonRound = -1;
	loadSave = false;
}


void Round::playRound() {
	Table* table = NULL;
	if (loadSave) {
		//Maybe pass in the next player from Serializer?
		table = new Table((startingPlayer == HUMAN_PLAYER), true);
	}
	else {
		table = new Table((startingPlayer == HUMAN_PLAYER));
	}
	 
	while (table->runCycle() == false);

	playerScores[HUMAN_PLAYER] = table->getHumanScore();
	playerScores[COMPUTER_PLAYER] = table->getComputerScore();

	Client::outputString("Round Scores: Human: " + to_string(playerScores[HUMAN_PLAYER]) + "  Computer: " + to_string(playerScores[COMPUTER_PLAYER]) + "\n");
	delete table;

}