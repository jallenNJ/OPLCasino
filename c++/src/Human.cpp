#include "Human.h"



Human::Human() {
	setName();
}
bool Human::setName() {
	
	name = Client::getStringInput("Please enter your name: ");

	Client::outputString("Welcome " + name + "!");
	return true;
	
}

Player::PlayerMove Human::doTurn(Hand tableCards) {
	
	
	
	while (true) {
		Actions actionToTake = promptForAction();
		vector<int> cardsInHand = promptForCardToUse (playerHand.handSize(), false);
		vector<int> cardsOnTable;
		if (actionToTake != Trail) {
			cardsOnTable = promptForCardToUse(tableCards.handSize(), true);
		}
		
		int cardInHand = cardsInHand[0];
		bool successfulResult = false;
		vector<Card> cardsToCheck;
		for (unsigned int i = 0; i < cardsOnTable.size(); i++) {
			cardsToCheck.push_back(tableCards.getCardCopy(cardsOnTable[i]));
		}
		switch (actionToTake) {
			//TODO: Add check to prevent reserved card from being played
			case Player::Capture:
				successfulResult = captureCard(playerHand.getCardCopy(cardInHand) , cardsToCheck);
				break;
			case Player::Build:
				//TODO: Check if creating to adding to build
				successfulResult = createBuild(playerHand.getCardCopy(cardInHand), cardsToCheck);
				break;
			case Player::Trail:
				//CHECK IF OWN BUILD and replace later
				successfulResult = true;
				break;
			default:
				break;
		}

		if (successfulResult) {
			sort(cardsOnTable.begin(), cardsOnTable.end());
			reverse(cardsOnTable.begin(), cardsOnTable.end());
			Card played = playerHand.removeCard(cardInHand);
			return PlayerMove(actionToTake, played, cardsOnTable);
		}
		else {
			Client::outputString("Invalid Action");
		}
	}
	


}

Player::Actions Human::promptForAction() {
		vector<char> allowedLetters;
		allowedLetters.push_back('c');
		allowedLetters.push_back('b');
		allowedLetters.push_back('t');
		char input = Client::getCharInput("Would you like to: (C)apture, (B)uild, (T)rail?", allowedLetters);
		if (input == 'c') {
			return Capture;
		}
		if (input == 'b') {
			return Build;
		}
		if (input == 't') {
			return Trail;
		}

		Client::outputError("Failed to find valid move, error handling with trail");
		return Trail;
}


vector<int> Human::promptForCardToUse(int size, bool selectingTable) {
	vector<int> values;
	if (size == 1) {
		Client::outputString("Only available card automatically choosen");
		values.push_back(0);
		return values;
	}


	string playOrSelect = "";
	if (selectingTable) {
		playOrSelect = "select";
	} else {
		playOrSelect = "play";
	}

	int input = 0;
	vector<int> previousInputs;
	while (true) {
		input = Client::getIntInputRange("Which card would you like to " + playOrSelect + " (1-" + to_string(size) + ")", 1, size);
		bool valid = true;
		for (unsigned int i = 0; i < previousInputs.size(); i++) {
			if (input == previousInputs[i]) {
				Client::outputString("Cannot select the same card twice");
				valid = false;
				break;
			}
		}
		if (valid) {
			previousInputs.push_back(input);
			input--;
			values.push_back(input);
	
		}
		input = 0;
		
		if (selectingTable) {
			vector<char> yesNo;
			yesNo.push_back('y');
			yesNo.push_back('n');
			char moreValues = Client::getCharInput("Would you like to enter another card? (y/n):", yesNo);
			if (moreValues == 'n') {
				break;
			}

		} else {
			break;
		}

	}

	
	return values;
}