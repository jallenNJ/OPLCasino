#include "Table.h"

Table::Table() {



	Human* human = new Human();
	Computer* computer = new Computer();
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

	deck = new Deck();
	fillHand(0);
	fillHand(1);
	fillLooseCards();

}


const void Table::printBoard() {
	cout << "\n";
	cout <<left<< setw(15)<< players[0]->getName()<<": ";
	//printHand(&hands[0]);
	cout << players[0]->toFormattedString();
	cout << "\n";
	cout << setw(15)<<"Table"<<":";
	cout << " " << looseCards.toFormattedString();
	cout << "\n";
	cout << left << setw(15) << players[1]->getName() << ": ";
	cout << players[1]->toFormattedString();
	cout << "\n";
	cout << endl;
}

Table::~Table() {
	delete deck;
	delete[] players;
}

bool Table::runCycle() {
	printBoard();
	doPlayerMove(0);




	return true;
}

void Table::doPlayerMove(int playerIndex) {
	//Player::PlayerMove resultTuple =  players[playerIndex]->doTurn(hands[playerIndex], looseCards);
}