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




	return false;
}

void Table::doPlayerMove(int playerIndex) {
	
	Player::PlayerMove resultTuple =  players[playerIndex]->doTurn(looseCards);
	switch (resultTuple.actionTaken) {
	case Player::Actions::Capture:
		players[playerIndex]->addToPile(resultTuple.playedCard);
		for (unsigned int i = 0; i < resultTuple.targetIndex.size(); i++) {
			players[playerIndex]->addToPile(looseCards.removeCard(resultTuple.targetIndex[i]));
		}
		break;
	case Player::Actions::Build:
		break;
	case Player::Actions::Trail:
		looseCards.addCard(resultTuple.playedCard);
		break;
	default:
		abort();
		break;
	}
	
}