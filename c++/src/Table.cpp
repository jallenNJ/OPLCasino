#include "Table.h"

Table::Table() {

	hands[0].reserve(4);
	hands[1].reserve(4);
	looseCards.reserve(4);
	piles[0].reserve(35);
	piles[1].reserve(35);

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


const void Table::printBoard() {
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




	return false;
}

void Table::doPlayerMove(int playerIndex) {
	Player::PlayerMove resultTuple =  players[playerIndex]->doTurn(hands[playerIndex], looseCards);
	switch (resultTuple.actionTaken) {
	case Player::Actions::Capture:
		piles[playerIndex].push_back(hands[playerIndex][resultTuple.handIndex]);
		// Add table cards
		hands[playerIndex].erase(hands[playerIndex].begin() + resultTuple.handIndex);
		break;
	case Player::Actions::Build:
		break;
	case Player::Actions::Trail:
		looseCards.push_back(hands[playerIndex][resultTuple.handIndex]);
		hands[playerIndex].erase(hands[playerIndex].begin() + resultTuple.handIndex);
		break;
	default:
		abort();
		break;
	}
}