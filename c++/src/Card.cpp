#include "Card.h"


const bool Card::checkCapture(Card other) {
	return getSymbol() == other.getSymbol();
}

const bool Card::checkCapture(vector<Card> cardsToCheck) {
	int sum = 0;
	for (unsigned int i = 0; i < cardsToCheck.size(); i++) {
		sum += cardsToCheck[i].getNumericValue();
	}
	if (sum > 14) {
		return false; 
	}
	if (sum == getNumericValue()) {
		return true;
	}
	if (sum == 14 && getSymbol() == 'A') {
		return true;
	}
	return false;


}

void Card::symbolToNumericValue() {
	if (isdigit(symbol)) {
		numericValue = (int)(symbol - '0');
		return;
	}
	switch (symbol) {
		case 'A':
			numericValue = 1;
			break;
		case 'X':
			numericValue = 10;
			break;
		case 'J':
			numericValue = 11;
			break;
		case 'Q':
			numericValue = 12;
			break;
		case 'K':
			numericValue = 13;
			break;
		default:
			numericValue = 1;
			break;

	}
	return;

}