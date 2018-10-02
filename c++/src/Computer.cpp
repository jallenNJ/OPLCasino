#include "Computer.h"


Computer::Computer() {
	setName();
}

Computer::Computer(bool loadSaveFile) {
	Serializer::PlayerInfo saveInfo = Serializer::getComputerPlayerInfo();
	if (!loadSaveFile || saveInfo.isValid == false) {
		setName();
		return;
	}

	setName();
	playerHand = Hand(saveInfo.hand);
	playerPile = Hand(saveInfo.pile);
}

bool Computer::setName() {
	name = "Bleep Bloop"; //Maybe Clever Mongoose?
	return true;
}




/* *********************************************************************
Function Name: doTurn()
Purpose: For the AI to decide its turn, by calling helper functions
Parameters:
			Table, the Hand object that contains the table cards
Return Value: A Playermove object containing the move the AI will make
Local Variables:
			pendingAction, a PlayerMove that contains the return value of the helper function
			
Algorithm:
	1) Check for priority cards to be capture
	2) Check to create new builds
	3) Check to capture existing builds
	4) Check to Capture loose cards
	5) Trail
			
Assistance Received: none
********************************************************************* */
Player::PlayerMove Computer::doTurn(Hand table) {

	//Check if the ten of the diamonds exist on the table, and capture if able
	PlayerMove pendingAction = checkForPrioCard(table, 'D', 'X', 10);
	if (pendingAction.actionTaken == Player::Actions::Capture) {
		return pendingAction;
	}

	//Check if the two of Spades exist on the table, and capture if able
	pendingAction = checkForPrioCard(table, 'S', '2', 2);
	if (pendingAction.actionTaken == Player::Actions::Capture) {
		return pendingAction;
	}

	//Check if any builds are possible, and create them
	pendingAction = checkForBuilds(table);
	if (pendingAction.actionTaken == Player::Actions::Build) {
		return pendingAction;
	}
	
	//Check if able to capture any existing builds on the table
	pendingAction = checkForBuildCapture(table);
	if (pendingAction.actionTaken == Player::Actions::Capture) {
		return pendingAction;
	}

	//Check if able to capture any cards
	pendingAction = checkForNormalCapture(table);
	if (pendingAction.actionTaken == Player::Actions::Capture) {
		return pendingAction;
	}


	
	//If not the advising AI, print out move
	if (name != "Advisor") {
		Client::outputString("AI has no better options, trailing");
		Client::outputString("Ai played " + playerHand.cardToString(0));
	}

	//No valid action, trail.
	return PlayerMove(Trail, playerHand.removeCard(0), vector<int>(1));
}


/* *********************************************************************
Function Name: checkForPrioCard
Purpose: For the AI to check for priority cards and capture if possible
Parameters:
			Table, the Hand object that contains the table cards
			targetSuit, the suit of the desired card
			targetSymbol, the symbol of the desired card
			targetVal, the numeric value of the desired card
Return Value: A Playermove object containing the move the AI will make. Capture if valid, trail if not 
Local Variables:
			location, an int for if the table contains the target card.
			play, A Card object for the card the AI will play
			vector<int> targetCard, the card(s) which will be captured.

Algorithm:
	1) Check if card is on table
	2) If it does, check if player holds card to capture it
	3) If they do, capture it
Assistance Received: none
********************************************************************* */
Player::PlayerMove Computer::checkForPrioCard(Hand table, char targetSuit, char targetSymbol, int targetVal) {
	int location = table.containsCard(targetSuit, targetSymbol);
	//If the card is on the table
	if (location > 0) {
		//And the player has a card to capture it
		if (playerHand.containsCardValue(targetVal)) {
			Card play;
			//Get the card they can play from their hand
			for (unsigned int i = 0; i < playerHand.handSize(); i++) {
				play = playerHand.getCardCopy(i);
				if (play.getSymbol() == targetSymbol) {
					break;
				}
			}
			//Get all cards with matching symbols on the table
			//If its reserved, the build will clear it
			vector<int> targetCard;
			for (unsigned int i = 0; i < table.handSize(); i++) {

				if (table.getCardCopy(i).getSymbol() == targetSymbol) {
					targetCard.push_back(i);
				}
				

			}
			//If its reserved, clear it as its capturing its build by being played
			if (targetCard.size() != 0) {
				if (checkReserved(play)) {
					releaseBuildValue(play.getNumericValue());
				}
				//Output move and return
				Client::outputString("AI is playing " + play.toString() + " to capture " + table.getCardCopy(targetCard[0]).toString() + " due to it having a score value. ");
				return PlayerMove(Player::Capture, play, targetCard);
			}



		}
	}
	//Return an invalid value due to the card not existing
	return PlayerMove(Player::Trail, playerHand.getCardCopy(0), vector<int>());

}

/* *********************************************************************
Function Name: checkForBuilds
Purpose: For the AI to see if it can create any builds
Parameters:
			Table, the Hand object that contains the table cards
Return Value: A Playermove object containing the move the AI will make. Build if valid, Trail if not
Local Variables:
			currentCard, a card object for readabilty and efficency
			vector<vector<int>> possibleBuilds, all possible build combinations it can make
			result, a bool to contain if the move is valid
Algorithm:
	1) Check for all possible builds
	2) Choose the best build (Most cards)

Assistance Received: none
********************************************************************* */

Player::PlayerMove Computer::checkForBuilds(Hand table) {
	//For every card in the hand
	for (unsigned int i = 0; i < playerHand.handSize(); i++) {

		Card currentCard = playerHand.getCardCopy(i);
		//If its reserved it can't be used for the build
		if (checkReserved(currentCard)) {
			continue;
		}
		//Get all builds it can make
		vector<vector<int>> possibleBuilds = findSelectableSets(currentCard, table);
		if (possibleBuilds.size() > 0) {

			vector<int> buildIndices = decideBestBuild(possibleBuilds);
			vector<Card> cardsToCheck;
			//Get all the cards it is trying to build with
			for (unsigned int i = 0; i < buildIndices.size(); i++) {
				cardsToCheck.push_back(table.getCardCopy(buildIndices[i]));
			}
			bool result = createBuild(currentCard, cardsToCheck);
			if (!result) {
			//	Client::outputError("Ai tried to create an invalid build after deeming it valid, trying next result if it exists");
				continue;
			}

			//Sort, output, and then reverse the cards
			sort(buildIndices.begin(), buildIndices.end());

			if (name != "Advisor") {
				string buildCards = cardIndiciesToString(table, buildIndices);
				Client::outputString("AI played " + currentCard.toString() + " as it was an oppertunity to make a build using cards: " + buildCards);
			}
			std::reverse(buildIndices.begin(), buildIndices.end());
			//Remove from hand and return
			playerHand.removeCard(i);
			return PlayerMove(Player::Build, currentCard, buildIndices);
		}
	}
	return PlayerMove(Player::Trail, playerHand.getCardCopy(0), vector<int>());
}

/* *********************************************************************
Function Name: checkForBuildCaptures
Purpose: For the AI to see if it can capture any of its existing builds
Parameters:
			Table, the Hand object that contains the table cards
Return Value: A Playermove object containing the move the AI will make. Capture if valid, Trail if not
Local Variables:
			vector<int> captureIndices, all indicies which can be captured
			int cardIndex, the index in the hand of the card being played
			Card Played, the card object being played
Algorithm:
	1) Check for all possible builds
	2) Choose the best build (In a helper function)
	3) Format return object based on that choice

Assistance Received: none
********************************************************************* */
Player::PlayerMove Computer::checkForBuildCapture(Hand table) {
	//If it has a build to play
	if (buildValues.size() > 1) {
		vector<int> captureIndices;
		int cardIndex = 0;
		Card played;

		//Find the card that matches the first reserved value
		for (unsigned int i = 0; i < playerHand.handSize(); i++) {
			played = playerHand.getCardCopy(i);
			if (played.getNumericValue() == buildValues[0]) {
				cardIndex = i;

			}
		}

		//Find the table card that matches it
		for (unsigned int i = 0; i < table.handSize(); i++) {
			if (table.getCardCopy(i).getNumericValue() == played.getNumericValue()) {
				captureIndices.push_back(i);
			}
		}

		//Play the card with the build
		if (captureIndices.size() > 0) {
			releaseBuildValue(played.getNumericValue());
			Client::outputString("Ai is playing " + played.toString() + " to capture its build");
			playerHand.removeCard(cardIndex);
			return PlayerMove(Player::Capture, played, captureIndices);

		}


	}

	//Return an invalid value
	return PlayerMove(Player::Trail, playerHand.getCardCopy(0), vector<int>());


}

/* *********************************************************************
Function Name: checkForNormalCapture
Purpose: For the AI to see if it can capture any loose cards
Parameters:
			Table, the Hand object that contains the table cards
Return Value: A Playermove object containing the move the AI will make. Capture if valid, Trail if not
Local Variables:
			vector<int> possibleCaptures all indicies which can be captured
			Card Played, the card object being played
Algorithm:
	1) Check for all possible builds
	2) Choose the best build (Most cards)

Assistance Received: none
********************************************************************* */

Player::PlayerMove Computer::checkForNormalCapture(Hand table) {
	//For all Cards in hand
	for (unsigned int i = 0; i < playerHand.handSize(); i++) {
		//Check for all captures with this card
		vector<int> possibleCaptures = findRequiredCaptures(playerHand.getCardCopy(i), table);
		if (possibleCaptures.size() > 0) {
			//If so, get a copy of the card
			Card played = playerHand.getCardCopy(i);
			if (name != "Advisor") {
				string captureString = cardIndiciesToString(table, possibleCaptures);
				Client::outputString("AI is playing " + played.toString() + " with the intention of capturing: " + captureString);
			}
			//Remove, and choose all cards which were returned
			playerHand.removeCard(i);
			return PlayerMove(Player::Capture, played, possibleCaptures);
			break;
		}
	}

	//Return invalid value
	return PlayerMove(Player::Trail, playerHand.getCardCopy(0), vector<int>());
}


/* *********************************************************************
Function Name: decideBestBuild
Purpose: For the AI to choose which of the potential builds are the best option
Parameters:
			vector<vector<int>> options, all the potential options
Return Value: A Playermove object containing the move the AI will make. Capture if valid, Trail if not
Local Variables:
			unsigned int largestSize, the size of the largest vector
			unsigned int indexOfLargest, the index of the largest vector
Algorithm:
	1) Check for all possible builds
	2) Choose the best build (Most cards)

Assistance Received: none
********************************************************************* */
vector<int> Computer::decideBestBuild(vector<vector<int>> options) {

	//Edge case checking
	if (options.size() == 0) {
		return vector<int>();
	}
	if (options.size() == 1) {
		return options[0];
	}

	unsigned int largestSize = 0;
	unsigned int indexOfLargest = 0;
	//For every vector
	for (unsigned int i = 0; i < options.size(); i++) {
		//If its larger, track it as the largest
		if (options[i].size() > largestSize) {
			largestSize = options[i].size();
			indexOfLargest = i;
		}
	}

	//Return the largest
	return options[indexOfLargest];


}


string Computer::cardIndiciesToString(Hand table, vector<int> indicies) {

	string cardString = "";
	for (unsigned int i = 0; i < indicies.size(); i++) {
		cardString += table.getCardCopy(indicies[i]).toString();
	}
	return cardString;

}