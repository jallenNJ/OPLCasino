#include "Round.h"


Round::Round() {
	char input = 0;
	do {
		cout << "(H)eads or (T)ails?";
		
		cin >> input;
		cin.clear();
		cin.ignore(cin.rdbuf()->in_avail(), '\n');//clears any extra in buffer
		input = tolower(input);
	} while (input!='h'  && input !='t');

	bool guessedHeads = false;
	if (input == 'h') {
		guessedHeads = true;
	}

	srand((unsigned int)time(NULL)); //Explict conversation to unsigned int to remove warning
	int randNum = rand() % 100;
	if (randNum % 2 == 0) { //Coin landed on heads
		if (guessedHeads) {
			cout << "Heads! Player goes first" << endl;
			currentPlayer = HUMAN_PLAYER;
		}
		else {
			cout << "Heads! Computer goes first" << endl;
			currentPlayer = COMPUTER_PLAYER;
		}	
	}
	else { //Coin landed on tails
		if (guessedHeads) {
			cout << "Tails! Computer goes first" << endl;
			currentPlayer = COMPUTER_PLAYER;
		}
		else {
			cout << "Tails! Player goes first" << endl;
			currentPlayer = HUMAN_PLAYER;
		}

	}
	intializeRound();
}

Round::Round(bool humanFirst) {
	if (true) {
		currentPlayer = HUMAN_PLAYER;
	}
	else {
		currentPlayer =  COMPUTER_PLAYER;
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
	Table* table = new Table();
	//table->printBoard();
	while (table->runCycle() == false);

	delete table;

}