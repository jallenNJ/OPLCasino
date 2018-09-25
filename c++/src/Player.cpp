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

 vector<int> Player::findRequiredCaptures(PlayingCard played, Hand table) {
	 vector<int> requireds;

	 for (unsigned int i = 0; i < table.handSize(); i++) {
		Card current =  table.getCardCopy(i);
		 if (current.getSymbol() == played.getSymbol()) {
			 if (current.getSuit() != 'B') {
				 requireds.push_back(i);
			 }
			 else {
				 if (current.getOwner() == getName() && checkReserved(played)) {
					 requireds.push_back(i);
				 }
			 }
		}
	 }
	 return requireds;
 }

 /*vector<int> Player::findOptionialCaptures(PlayingCard played, Hand table){

	 for (unsigned int i = 0; i < table.handSize(); i++) {
		 Card current = table.getCardCopy(i);
	 }
 }*/

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
		 if (amountOfSymbolInHand(played.getSymbol()) < 2) {
			 return true;
		 }
	 }
	 return false;
 }


 vector<vector<int>> Player::findSelectableSets(int target, Hand table) {
	 vector<vector<int>> allSets;
	 if (target == 1) {
		 target = 14;
	 }
	 vector<int> stackVector;
	 int sum = 0;
	
	 stackVector.reserve(10);
	 for (unsigned int i = 0; i < table.handSize(); i++) {
		
		 stackVector.push_back(i);
		 if (stackVector[0] > target) {
			 continue;
		 }
		 sum = stackVector[0];

		 for (unsigned int offset = 1; offset < table.handSize() - 1; offset++) {
			 stackVector.clear();
			 stackVector.push_back(i);
			 sum = stackVector[0];
			 for (unsigned int j = i + offset; j < table.handSize(); j++) {
				 Card current = table.getCardCopy(j);
				 if (target >= sum + current.getNumericValue()) {
					 sum += current.getNumericValue();
					 stackVector.push_back(j);
					 if (sum == target) {
						 bool dupe = false;
						 for (unsigned int k = 0; k < allSets.size(); k++) {
							 if (stackVector == allSets[k]) {
								 dupe = true;
								 break;
							 }
						 }
						 if (!dupe) {
							 allSets.push_back(stackVector);
						 }
						
						 stackVector.clear();
						 break;
					 }

				 }
			 }

		 }


		


	 }
	 return allSets;
 }