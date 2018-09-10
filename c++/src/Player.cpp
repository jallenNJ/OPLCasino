#include "Player.h"

Player::Player() {

	setName();

}

 void Player::setName() {
	 name = "Generic Player";
}


 bool Player::captureCard(Card played, Card target) {
	 return played.checkCapture(target);
 }
 bool Player::captureCard(Card played, vector<Card> targets) {
	 return played.checkCapture(targets);
 }