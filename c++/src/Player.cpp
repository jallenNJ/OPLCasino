#include "Player.h"

Player::Player() {


}



 bool Player::captureCard(Card played, Card target) {
	 return played.checkCapture(target);

	 //FIX THE BELOW CODE BY OVERLOADING BUILD AS A SEPERATE FUNCTION
	/*bool sameValue = played.checkCapture(target);
	if (sameValue == false) {
		return false;
	}
	if (checkReserved(played)) {
		if (target.getSuit == 'B') {
			if(target.)
		}

	}*/
 }
 bool Player::captureCard(Card played, vector<Card> targets) {
	 return played.checkCapture(targets);
 }

 vector<int> Player::findRequiredCaptures(Card played, Hand table) {
	 vector<int> requireds;

	 for (unsigned int i = 0; i < table.handSize(); i++) {
		Card current =  table.getCardCopy(i);
		 if (current.getNumericValue() == played.getNumericValue()) {
			 if (current.getSuit() != 'B') {
				 requireds.push_back(i);
			 }
			 else {
				 if (current.getOwner() == getName() && checkReserved(played)) {
					
					 releaseBuildValue(played.getNumericValue());
				 }

				 requireds.push_back(i);
			 }
		}
	 }
	 return requireds;
 }

 /* *********************************************************************
Function Name: createBuild
Purpose: To check if creating a build is a valid move
Parameters:
			played, the card being played from the hand
			selectedCards, copies of the cards on the table being played
Return Value: bool, true if valid, false if not
Local Variables:
			sum, the sum of selected cards
Algorithm:
			1) Sum the values of new cards
			2) Check if it matches the selected Cards
Assistance Received: none
********************************************************************* */
 bool Player::createBuild(Card played, vector<Card> selectedCards) {
	 int sum = played.getNumericValue();
	 for (unsigned int i = 0; i < selectedCards.size(); i++) {
		 sum += selectedCards[i].getNumericValue();
	 }
	 if (sum > 14) {
		 return false;
	 }
	 return playerHand.containsCardValue(sum);
 }

 bool Player::checkTrail(Card played) {
	 return !checkReserved(played);
 }

 bool Player::checkReserved(Card played) {
	 if (findReservedValue(played.getNumericValue()) >= 0) {
		 if (amountOfSymbolInHand(played.getSymbol()) == 1) {
			 return true;
		 }
	 }
	 return false;
 }


 vector<vector<int>> Player::findSelectableSets(Card playedCard, Hand table) {

	 //Cache the value for nicer syntax
	 int playedCardValue = playedCard.getNumericValue();
	 vector<vector<int>> allSets;
	 vector<int> stackVector;
	 int sum = 0;
	
	 //Reserve up to ten for better runtime
	 stackVector.reserve(10);

	 
	 //This loop is responsible for checking every card in the players hand
	 for (unsigned int cardInHandIndex = 0; cardInHandIndex < playerHand.handSize(); cardInHandIndex++) {
		 //Save the card for readabilty
		 Card cardInHand = playerHand.getCardCopy(cardInHandIndex);

		 //If the played card is this card, skip
		 if (cardInHand.getSuit() == playedCard.getSuit() && cardInHand.getSymbol() == cardInHand.getSymbol()) {
			 continue;
		 }

		 //For every card on the table
		 for (unsigned int firstCardOnTable = 0; firstCardOnTable < table.handSize(); firstCardOnTable++) {

			 //Remove the old data and load in the constant data (the played card)
			 sum = playedCardValue;
			 stackVector.clear();

			 //If the value is greater, cannot be used in a build
			 if (sum > cardInHand.getNumericValue()) {
				 continue;
			 }

			 //Store the first card we're checking for readability
			 Card firstTableCard = table.getCardCopy(firstCardOnTable);
			 if (cardInHand.getNumericValue() <= sum + firstTableCard.getNumericValue()) {
				 continue;

			 }
			 //The offset to check every card after it
			 for (unsigned int offset = 1; offset < table.handSize(); offset++) {
				//For every card after the first card
				 for (unsigned int i = firstCardOnTable + offset; i < table.handSize(); i++) {
					 //Reset previous attempts, and cache the current card for readabilty
					 stackVector.clear();
					 Card current = table.getCardCopy(i);

					 //If the value with the new card matches or is less then,
					 if (cardInHand.getNumericValue() >= sum + current.getNumericValue()) {
						 //Add it to the propestive build
						 sum += current.getNumericValue();
						 stackVector.push_back(i);

						 //If it matches
						 if (sum == cardInHand.getNumericValue()) {

							 //Check if its a duplicate
							 bool dupe = false;
							 for (unsigned int k = 0; k < allSets.size(); k++) {
								 if (stackVector == allSets[k]) { 
									 //It is a duplicate
									 dupe = true;
									 break;
								 }
							 }
							 //If not a duplicate, record it
							 if (!dupe) {
								 allSets.push_back(stackVector);
								 break;
							 }
						 }

					 }
				 }
			 }
		 }
	 }



	 return allSets;
 }