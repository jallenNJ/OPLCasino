#include "Hand.h"

string Hand::toFormattedString() const {
	string formatted = "";
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		formatted += to_string(i + 1) + ")" + cardsInHand[i]->toString() + " ";
	}
	return formatted;


}

int Hand::countSymbol(char symbol) const{
	int sum = 0;
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		if (cardsInHand[i]->getSymbol() == symbol) {
			sum++;
		}
	}
	return sum;
}