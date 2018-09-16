#include "Round.h"


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

void Round::intializeRound() {
	playerScores[HUMAN_PLAYER] = 0;
	playerScores[COMPUTER_PLAYER] = 0;
	roundOver = false;
	playerThatWonRound = -1;
}


void Round::playRound() {

	Table* table = new Table((startingPlayer == HUMAN_PLAYER));
	while (table->runCycle() == false);

	delete table;

}