#include "Human.h"



Human::Human() {
	setName();
}

/* *********************************************************************
Function Name: Human
Purpose: To intialize the object from a save state
Parameters:
			loadFromSave, a bool to indicate the Constructor should access the save data
Return Value: Constructor
Local Variables:
			saveInfo, A PlayerInfo struct containing all the information to be read in
Algorithm:
			1) Get the save data
			2) Assign the save data to each variable
Assistance Received: none
********************************************************************* */
Human::Human(bool loadFromSave) {
	Serializer::PlayerInfo saveInfo = Serializer::getHumanPlayerInfo();
	if (loadFromSave == false || saveInfo.isValid == false) {
		Human();
		return;
	}

	name = saveInfo.name;
	playerHand = Hand(saveInfo.hand);
	playerPile = Hand(saveInfo.pile);
}

bool Human::setName() {
	
	name = Client::getStringInput("Please enter your name: ");

	Client::outputString("Welcome " + name + "!");
	return true;
	
}

/* *********************************************************************
Function Name: doTurn
Purpose: To have the human enter the action they want to take and
	validate it
Parameters:
			tableCards, a copy of the Hand object on the table to read data
Return Value: A PlayerMove struct which contians the move enum, the card they played, and what they suggested
Local Variables:
			actionToTake, a cache of the move they took
			cardsInHand, to store the card they want to play(later is cast to an int)
			cardsOnTable, store all the cards they want to select
			cardInHand, an int that stores the 0th index of cardsInHand
			successfulResult, a bool to keep track if the action was valid
			played, a cache of the selected card from hand

Algorithm:
			1) Choose Action
			2) Choose Card in Hand
			3) Choose table cards (if applicable)
			4) Validate action
			5) If not valid Go to step 1
			6)Return all used information
Assistance Received: none
********************************************************************* */
Player::PlayerMove Human::doTurn(Hand tableCards) {
	
	
	while (true) {
		//Get all the vars
		Actions actionToTake = promptForAction();
		vector<int> cardsInHand = promptForCardToUse (playerHand.handSize(), false);
		vector<int> cardsOnTable;

		//If trailing, don't need to select which card on table to play
		if (actionToTake != Trail) {
			cardsOnTable = promptForCardToUse(tableCards.handSize(), true);
		}
		

		int cardInHand = cardsInHand[0];
		bool successfulResult = false;
		vector<Card> cardsToCheck;
		for (unsigned int i = 0; i < cardsOnTable.size(); i++) {
			cardsToCheck.push_back(tableCards.getCardCopy(cardsOnTable[i]));
		}
		//Call appropriate function based on each action
		switch (actionToTake) {
			//TODO: Add check to prevent reserved card from being played
			case Player::Capture:
				successfulResult = captureCard(playerHand.getCardCopy(cardInHand) , cardsToCheck);
				break;
			case Player::Build:
				//TODO: Check if creating to adding to build
				//TODO: Make sure can't played reserved card unless there is replacement
				successfulResult = createBuild(playerHand.getCardCopy(cardInHand), cardsToCheck);
				break;
			case Player::Trail:
				//TODO: Make sure cannot trail with identical symbol
				successfulResult = checkTrail(playerHand.getCardCopy(cardInHand));
				break;
			default:
				break;
		}
		//If valid, return the tuple
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


/* *********************************************************************
Function Name: promptForAction
Purpose: To prompt the user for which action they want to take
Parameters:
			void
Return Value: The Action which was selected
Local Variables:
			allowedLetters, the correct answers to be used by Client class
			input, the result of the user input
Algorithm:
			1) Get user input
			2)Return enum based on it
Assistance Received: none
********************************************************************* */
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

/* *********************************************************************
Function Name: promptForCardToUse
Purpose: Prompt the user for what Card they wish to use
Parameters:
			size, an int for how many cards are in the hand
			selectingTable,for if selecting from the table or hand
Return Value: Vector<int> of all selected cards indices
Local Variables:
			values, a vector<int> which store the values to return
			playOrSelect, contains string for output
			input, the result of userInpur
Algorithm:
			1) Ask the user what card they want to select
			2) If selecting from hand, return
			3) Ask which card on table they would like to select
			4) If the card was selected before, reprompt
			5) If the user wants to select another card, prompt for 3
Assistance Received: none
********************************************************************* */
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