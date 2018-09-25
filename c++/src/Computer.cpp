#include "Computer.h"


Computer::Computer() {
	setName();
}

bool Computer::setName() {
	name = "Bleep Bloop"; //Maybe Clever Mongoose?
	return true;
}

Player::PlayerMove Computer::doTurn(Hand table) {

	int location = table.containsCard('D', 'X');
	if (location > 0) {
		Client::outputString("AI SHOULD CAPTURE 10 of hearts");
	}
	location = table.containsCard('S', '2');
	if (location > 0) {
		Client::outputString("AI SHOULD CAPTURE 2 of spades");
	}

	for (unsigned int i = 0; i < playerHand.handSize(); i++) {
		vector<vector<int>> possibleBuilds = findSelectableSets(playerHand.getCardCopy(i).getNumericValue(), table);
		if (possibleBuilds.size() > 0) {
			Client::outputString("AI should build with the card in the " + to_string(i) + " postion");
			break;
		}
	}

	for (unsigned int i = 0; i < playerHand.handSize(); i++) {
		vector<int> possibleCaptures = findRequiredCaptures(playerHand.getCardCopy(i), table);
		if (possibleCaptures.size() > 0) {
			Client::outputString("AI should caputre with the card in the " + to_string(i) + " postion");
			break;
		}
	}

	Client::outputString("AI has no better options, trailing");
	Client::outputString("Ai played " + playerHand.cardToString(0)); 
	return PlayerMove(Trail, playerHand.removeCard(0), vector<int>(1));
}