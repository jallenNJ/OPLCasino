#include "Player.h"

Player::Player() {


}



/* *********************************************************************
Function Name: findRequiredCaptures
Purpose: To Find all cards which are required to be captured by played card
Parameters:
			played, the card being played from the hand
			Hand Table, the cards on the table
Return Value: vector<int> of all indicies which need to be returned
Local Variables:
			vector<int> requireds, the return value of indicies
			Card Current, cache of current card for efficiency
Algorithm:
			1) Check if any cards match the face value
			2) Or if any builds match and are owned by the player
			3) If either match, add the indice of the build
Assistance Received: none
********************************************************************* */

 vector<int> Player::findRequiredCaptures(Card played, Hand table) {
	 vector<int> requireds;

	 //For every card
	 for (unsigned int i = 0; i < table.handSize(); i++) {
		 //Cache the card
		Card current =  table.getCardCopy(i);

		//If they are the same value
		 if (current.getNumericValue() == played.getNumericValue()) {
			 //If not a build, can immediately be pushed
			 if (current.getSuit() != 'B') {
				 requireds.push_back(i);
			 }
			 else { //Is a build

				 //If current player owns the build and the card is reserved
				 if (current.getOwner() == getName() && checkReserved(played)) {
					//Release the value
					 releaseBuildValue(played.getNumericValue());
				 }
				 //Add the build
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
	 //Sum all the card values
	 int sum = played.getNumericValue();
	 for (unsigned int i = 0; i < selectedCards.size(); i++) {
		 sum += selectedCards[i].getNumericValue();
	 }

	 //If exceeds max value, its not valud
	 if (sum > 14) {
		 return false;
	 }
	 return playerHand.containsCardValue(sum);
 }



 bool Player::checkReserved(Card played) {
	 if (findReservedValue(played.getNumericValue()) >= 0) {
		 if (amountOfSymbolInHand(played.getSymbol()) == 1) {
			 return true;
		 }
	 }
	 return false;
 }


 /* *********************************************************************
Function Name: findSelectableSets
Purpose: To find all selectable subsets for target card
Parameters:
			played, the card being played from the hand
			Hand table, the cards on the table
Return Value: vector<vector<int>> of all indicies
Local Variables:
			int playedCardValue, for readabilty, the value of the played card
			vector<vector<int>> allsets, the return value
			 vector<int> stackVector, all indicies for this set
Algorithm:
			1) For every card, check if there is a set of cards that sum to its value
			2) Start with the card next to the played card
			3) If the current potentials + new card <= target, add to list (Greedy algorithm)
			4) If value matches, it is a set
			5) If value exceeds, clear and start with the same played card, and the starting card next to the previous
Assistance Received: none
********************************************************************* */
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
			 if (cardInHand.getNumericValue() < sum + firstTableCard.getNumericValue()) {
				 continue;

			 }
			 if (cardInHand.getNumericValue() == sum + firstTableCard.getNumericValue()) {
				 stackVector.push_back(firstCardOnTable);
				 allSets.push_back(stackVector);
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