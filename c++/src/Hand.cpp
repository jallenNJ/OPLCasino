#include "Hand.h"

string Hand::toFormattedString() {
	string formatted = "";
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		formatted += to_string(i + 1) + ")" + cardsInHand[i]->toString() + " ";
	}
	return formatted;



}