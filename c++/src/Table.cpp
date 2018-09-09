#include "Table.h"

Table::Table() {

	hands[0].reserve(4);
	hands[1].reserve(4);
	looseCards.reserve(4);
	playerPile.reserve(35);
	computerPile.reserve(35);

	deck = new Deck();
	fillHand(&hands[0]);
	fillHand(&hands[1]);
	fillHand(&looseCards);

	Human* human = new Human();
	Computer* computer = new Computer();
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

}


void Table::printBoard() {
	cout << "\n";
	cout <<left<< setw(15)<< players[0]->getName()<<": ";
	printHand(&hands[0]);
	cout << "\n";
	cout << setw(15)<<"Table"<<":";
	printHand(&looseCards);
	cout << "\n";
	cout << left << setw(15) << players[1]->getName() << ": ";
	printHand(&hands[1]);
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
	players[playerIndex]->doTurn(hands[playerIndex], looseCards);
}