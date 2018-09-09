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

Player::PlayerMove Human::doTurn(vector<Card> hand, vector<Card>tableCards) {
	
	
	
	while (true) {
		Actions actionToTake = promptForAction();
		int cardInHand = promptForCardToUse (hand.size(), false);
		int cardOnTable = promptForCardToUse(tableCards.size(), true);
		bool successfulResult = false;
		switch (actionToTake) {
			case Player::Capture:
				successfulResult = captureCard(hand[cardInHand], tableCards[cardOnTable]);
				break;
			case Player::Build:
				break;
			case Player::Trail:
				break;
			default:
				break;
		}

		if (successfulResult) {
			return PlayerMove(actionToTake, cardInHand, cardOnTable);
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
		if (input == 'T') {
			return Trail;
		}
	}
}


int Human::promptForCardToUse(int size, bool selectingTable) {

	if (size == 1) {
		cout << "Only available card automatically choosen" << endl;
		return 0;
	}


	string playOrSelect = "";
	if (selectingTable) {
		playOrSelect = "select";
	} else {
		playOrSelect = "play";
	}

	int input = 0;
	while (input < 1 || input > size) {
		cout << "Which card would you like to "<< playOrSelect << " (1-" << size << ")" << endl;
		cin >> input;
		cin.clear();
		cin.ignore(cin.rdbuf()->in_avail(), '\n');
	}
	input--;
	return input;
}