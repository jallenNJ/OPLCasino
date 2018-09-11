#include "Human.h"



Human::Human() {
	setName();
}
void Human::setName() {
	string input = "";

	while (input.length() == 0) {
		cout << "Enter your name:  " << endl;
		cin >> input;
	}
	
	name = input;

	cout << "Welcome " << name << "!" << endl;
	
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
			cardsToCheck.push_back(tableCards.removeCard(cardsOnTable[i]));
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
			cout << "Invalid action" << endl;
		}
	}
	


}

Player::Actions Human::promptForAction() {
	while (true) {
		cout << "Would you like to: (C)apture, (B)uild, (T)rail?" << endl;
		char input;
		cin >> input;

		cin.clear();                              
		cin.ignore(cin.rdbuf()->in_avail(), '\n');
		
		input = tolower(input);
		if (input == 'c') {
			return Capture;
		}
		if (input == 'b') {
			return Build;
		}
		if (input == 't') {
			return Trail;
		}
	}
}


vector<int> Human::promptForCardToUse(int size, bool selectingTable) {
	vector<int> values;
	if (size == 1) {
		cout << "Only available card automatically choosen" << endl;
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
		while (input < 1 || input > size) {
			cout << "Which card would you like to " << playOrSelect << " (1-" << size << ")" << endl;
			cin >> input;
			cin.clear();
			cin.ignore(cin.rdbuf()->in_avail(), '\n');
		}
		bool valid = true;
		for (unsigned int i = 0; i < previousInputs.size(); i++) {
			if (input == previousInputs[i]) {
				cout << "Cannot select the same card twice" << endl;
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
			char moreValues = '0';
			while (moreValues != 'y' && moreValues != 'n') {
				cout << "Would you like to enter another card? (y/n)";
				cin >> moreValues;
				cin.clear();
				cin.ignore(cin.rdbuf()->in_avail(), '\n');
				moreValues = tolower(moreValues);
			}
			if (moreValues == 'n') {
				break;
			}

		} else {
			break;
		}

	}

	
	return values;
}