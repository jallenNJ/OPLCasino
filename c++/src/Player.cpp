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