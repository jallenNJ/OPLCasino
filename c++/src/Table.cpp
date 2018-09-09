#include "Table.h"

Table::Table() {

	playerHand.reserve(4);
	computerHand.reserve(4);
	looseCards.reserve(4);
	playerPile.reserve(35);
	computerPile.reserve(35);

	deck = new Deck();
	fillHand(&playerHand);
	fillHand(&computerHand);
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
	printHand(&playerHand);
	cout << "\n";
	cout << setw(15)<<"Table"<<":";
	printHand(&looseCards);
	cout << "\n";
	cout << left << setw(15) << players[1]->getName() << ": ";
	printHand(&computerHand);
	cout << "\n";
	cout << endl;
}

Table::~Table() {
	delete deck;
	delete[] players;
}