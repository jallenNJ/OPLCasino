#include "Card.h"


Card::Card(char su, char sy) {
	suit = su;
	symbol = sy;
	symbolToNumericValue();
}	

const bool Card::checkCapture(Card other) {
	return getSymbol() == other.getSymbol();
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