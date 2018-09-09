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

	humanPlayer = new Human();
}


void Table::printBoard() {
	cout <<left<< setw(10)<< humanPlayer->getName()<<"'s Hand: ";
	printHand(&playerHand);
//	for (int i = 0; i < playerHand.size(); i++) {
	//	cout << ""+playerHand[i].toString() << " ";
//	}
	cout << "\n";
	cout << setw(10)<<"Table:\t";
	printHand(&looseCards);
	cout << "\n";
	cout << "COMPUTER GOES HERE";
	printHand(&computerHand);
	cout << endl;
}

Table::~Table() {
	delete deck;
	delete humanPlayer;
}