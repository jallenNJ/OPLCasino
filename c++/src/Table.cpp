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

}


void Table::printBoard() {
	cout << "Player Hand: ";
	for (int i = 0; i < playerHand.size(); i++) {
		//cout << ""+playerHand[i].toString() << " ";
	}

}

Table::~Table() {
	delete deck;
}