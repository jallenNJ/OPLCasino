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

Player::PlayerMove Computer::doTurn(Hand table) {

	int location = table.containsCard('D', 'X');
	if (location > 0) {
		if (playerHand.containsCardValue(10)) {
			Card play;
			for (unsigned int i = 0; i < playerHand.handSize(); i++) {
				play = playerHand.getCardCopy(i);
				if (play.getSymbol() == 'X') {
					break;
				}
			}
			vector<int> targetCard;
			for (unsigned int i = 0; i < table.handSize(); i++) {

				if (table.getCardCopy(i).getSymbol() == 'X') {
					targetCard.push_back(i);
					break;
				}

			}
			if (targetCard.size() != 0) {
				Client::outputString("AI is capturing "+ table.getCardCopy(targetCard[0]).toString() + " due to it having a score value");
				return PlayerMove(Player::Capture, play, targetCard);
			}

			
			
		}
		
	}
	location = table.containsCard('S', '2');
	if (location > 0) {
		if (playerHand.containsCardValue(2)) {
			Client::outputString("AI SHOULD CAPTURE 2 of spades");
		}
		
	}

	for (unsigned int i = 0; i < playerHand.handSize(); i++) {
		Card currentCard = playerHand.getCardCopy(i);
		if (checkReserved(currentCard)) {
			continue;
		}
		vector<vector<int>> possibleBuilds = findSelectableSets(currentCard, table);
		if (possibleBuilds.size() > 0) {
			//Client::outputString("AI should build with the card in the " + to_string(i) + " postion");
			vector<int> buildIndices = decideBestBuild(possibleBuilds);
			vector<Card> cardsToCheck;
			for (unsigned int i = 0; i < buildIndices.size(); i++) {
				cardsToCheck.push_back(table.getCardCopy(buildIndices[i]));
			}
			bool result = createBuild(currentCard, cardsToCheck);
			if (!result) {
				Client::outputError ("Ai tried to create an invalid build after deeming it valid, trying next result if it exists");
				continue;
			}
			sort(buildIndices.begin(), buildIndices.end());

			if (name != "Advisor") {
				string buildCards = cardIndiciesToString(table, buildIndices);
				Client::outputString("AI played " + currentCard.toString() + " as it was an oppertunity to make a build using cards: " + buildCards );
			}
			reverse(buildIndices.begin(), buildIndices.end());
			playerHand.removeCard(i);
			return PlayerMove(Player::Build, currentCard, buildIndices);
		}
	}

	if (buildValues.size() > 1) {
		Client::outputString("AI HAS A BUILD THEY SHOULD CAPTURE");
		vector<int> captureIndices;
		int cardIndex = 0;
		Card played;
		for (unsigned int i = 0; i < playerHand.handSize(); i++) {
			played = playerHand.getCardCopy(i);
			if (played.getNumericValue() == buildValues[0]) {
				cardIndex = i;
				
			}
		}

		for (unsigned int i = 0; i < table.handSize(); i++) {
			if (table.getCardCopy(i).getNumericValue() == played.getNumericValue()) {
				captureIndices.push_back(i);
			}
		}

		releaseBuildValue(played.getNumericValue());
		playerHand.removeCard(cardIndex);
		return PlayerMove(Player::Capture, played, captureIndices);

	}


	for (unsigned int i = 0; i < playerHand.handSize(); i++) {
		vector<int> possibleCaptures = findRequiredCaptures(playerHand.getCardCopy(i), table);
		if (possibleCaptures.size() > 0) {
			Card played = playerHand.getCardCopy(i);
			if (name != "Advisor") {
				string captureString = cardIndiciesToString(table, possibleCaptures);
				Client::outputString("AI is playing " + played.toString() + " with the intention of capturing: " + captureString);
			}
			playerHand.removeCard(i);
			return PlayerMove(Player::Capture, played, possibleCaptures);
			//Client::outputString("AI should caputre with the card in the " + to_string(i) + " postion");
			break;
		}
	}

	if (name != "Advisor") {
		Client::outputString("AI has no better options, trailing");
		Client::outputString("Ai played " + playerHand.cardToString(0));
	}
	return PlayerMove(Trail, playerHand.removeCard(0), vector<int>(1));
}


vector<int> Computer::decideBestBuild(vector<vector<int>> options) {
	if (options.size() == 0) {
		return vector<int>();
	}
	if (options.size() == 1) {
		return options[0];
	}

	unsigned int largestSize = 0;
	unsigned int indexOfLargest = 0;
	for (unsigned int i = 0; i < options.size(); i++) {
		if (options[i].size() > largestSize) {
			largestSize = options[i].size();
			indexOfLargest = i;
		}
	}

	return options[indexOfLargest];


}


string Computer::cardIndiciesToString(Hand table, vector<int> indicies) {

	string cardString = "";
	for (unsigned int i = 0; i < indicies.size(); i++) {
		cardString += table.getCardCopy(indicies[i]).toString();
	}
	return cardString;

}