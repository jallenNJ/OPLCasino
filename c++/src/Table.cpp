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


/* *********************************************************************
Function Name: printBoard
Purpose: To format a string to print board, and then print it
Parameters:
			void
Return Value: void (calls Client to output)
Local Variables:
			names, an array of all the names
			padding, an int of how many spaces to pad
			formattedTable, the formatted table being built
Algorithm:
			1) Print the human
			2) Print the board
			3) Print the Computer
Assistance Received: none
********************************************************************* */
const void Table::printBoard() {
	//Get the names of all players
	string names[3];
	names[0] = players[0]->getName();
	names[1] = players[1]->getName();
	names[2] = "Table";

	//Find the amount of padding needed (longestName + 1)
	int padding = (int)max(names[0].length(), names[1].length());
	padding = max(padding, (int)names[2].length());

	//Apply padding
	for (int i = 0; i < 3; i++) {
		for (unsigned int j = padding; j > padding - names[i].size(); j--) {
			names[i] += " ";
		}
		names[i] += ": ";
	}

	//Create the string to be output
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

/* *********************************************************************
Function Name: runCycle()
Purpose: Have both players play one card, and deal more cards if needed
Parameters:
			void
Return Value: bool, true if round is over, false if round can continue
Local Variables:
			none, member variables only
Algorithm:
			1) Print board
			2) First player does move
			3) Print board
			4) Second player does move
			5) If hands are empty, deal more cards
Assistance Received: none
********************************************************************* */
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


/* *********************************************************************
Function Name: doPlayerMove
Purpose: To have specifed player do their move
Parameters:
			playerIndex, the index to the player object in the array
Return Value: void (saves in member variables)
Local Variables:
			resultTuple, the results from the Player clase
			newBuild, if a new build is being made, it is in this object
Algorithm:
			1) Get each card seperately
			2) Create that into its own object
			3) Add to the member data object
Assistance Received: none
********************************************************************* */
void Table::doPlayerMove(int playerIndex) {
	
	//Have the player choose their move
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