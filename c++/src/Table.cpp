#include "Table.h"

Table::Table() {
	initTable();


}

Table::Table(bool humanStart) {
	initTable(humanStart);
}

Table::Table(bool humanStart, bool loadFromSave) {
	if (loadFromSave == false) {
		Table(humanStart);
		return;
	}
	initTable(humanStart, true);

}

void Table::initTable(bool humanStart) {
	Human* human = new Human();
	Computer* computer = new Computer();
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

	deck = new Deck();
	fillHand(0);
	fillHand(1);
	fillLooseCards();
	humanFirst = humanStart;
}

void Table::initTable(bool humanStart, bool loadFromSave) {
	if (loadFromSave == false) {
		initTable(humanStart);
		return;
	}

	Human* human = new Human(true);
	//Below this needs to be updated for saves
	Computer* computer = new Computer();
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

	deck = new Deck();
	fillHand(0);
	fillHand(1);
	fillLooseCards();
	humanFirst = humanStart;



}

const void Table::printBoard() {

	string names[3];
	names[0] = players[0]->getName();
	names[1] = players[1]->getName();
	names[2] = "Table";

	int padding = (int)max(names[0].length(), names[1].length());
	padding = max(padding, (int)names[2].length());
	for (int i = 0; i < 3; i++) {
		for (unsigned int j = padding; j > padding - names[i].size(); j--) {
			names[i] += " ";
		}
		names[i] += ": ";
	}
	string formattedTable = "\n" + names[0] + players[0]->toFormattedString() + 
							"\n" + names[2] + looseCards.toFormattedString() + 
							"\n" + names[1] + players[1]->toFormattedString() +
							"\n";
	Client::outputString(formattedTable);

}

Table::~Table() {
	delete deck;
	delete[] players;
}

bool Table::runCycle() {
	printBoard();
	if (humanFirst) {
		doPlayerMove(0);
		printBoard();
		doPlayerMove(1);
	}
	else {
		doPlayerMove(1);
		printBoard();
		doPlayerMove(0);
	}

	if (players[0]->getHandSize() == 0 && players[1]->getHandSize() == 0) {
		if (deck->isEmpty()) {
			return true;
		}
		else {
			fillHand(0);
			fillHand(1);
		}
		
	}



	return false;
}

void Table::doPlayerMove(int playerIndex) {
	
	Player::PlayerMove resultTuple =  players[playerIndex]->doTurn(looseCards);
	Build newBuild(resultTuple.playedCard, players[playerIndex]->getName());
	switch (resultTuple.actionTaken) {
	case Player::Actions::Capture:
		players[playerIndex]->addToPile(resultTuple.playedCard);
		for (unsigned int i = 0; i < resultTuple.targetIndex.size(); i++) {
			players[playerIndex]->addToPile(looseCards.removeCard(resultTuple.targetIndex[i]));
		}
		break;
	case Player::Actions::Build:
		
		for (unsigned int i = 0; i < resultTuple.targetIndex.size(); i++) {
			newBuild.addCardToBuild(looseCards.removeCard(resultTuple.targetIndex[i]));
		}
		looseCards.addCard(newBuild);
		players[playerIndex]->reserveCardValue(newBuild.getNumericValue());


		break;
	case Player::Actions::Trail:
		looseCards.addCard(resultTuple.playedCard);
		break;
	default:
		abort();
		break;
	}
	
}