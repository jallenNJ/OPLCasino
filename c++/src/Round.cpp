#include "Round.h"


Round::Round() {
	char input = 0;
	do {
		cout << "(H)eads or (T)ails?";
		
		cin >> input;
	} while (input != 'H' && input!='h' && input != 'T' && input !='t');

	bool guessedHeads = false;
	if (input == 'H' || input == 'h') {
		guessedHeads = true;
	}

	srand(time(NULL));
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

	Deck* deck = new Deck();
	playerScores[HUMAN_PLAYER] = 10;
	//playerScores[COMPUTER_PLAYER] = 10;
	playerThatWonRound = HUMAN_PLAYER;

}