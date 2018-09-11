#include "Player.h"

Player::Player() {


}



 bool Player::captureCard(Card played, Card target) {
	 return played.checkCapture(target);
 }
 bool Player::captureCard(Card played, vector<Card> targets) {
	 return played.checkCapture(targets);
 }

 bool Player::createBuild(Card played, vector<Card> hand, vector<Card> selectedCards) {
	 int sum = played.getNumericValue();
	 for (unsigned int i = 0; i < selectedCards.size(); i++) {
		 sum += selectedCards[i].getNumericValue();
	 }
	 for (unsigned int i = 0; i < hand.size(); i++) {
		 if (sum == hand[i].getNumericValue()) {
			 return true;
		 }
	 }
	 return false;
 }