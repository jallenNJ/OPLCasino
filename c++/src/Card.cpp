#include "Card.h"


Card::Card(char su, char sy) {
	suit = su;
	symbol = sy;
}

bool Card::checkCapture(Card other) {
	return getSymbol() == other.getSymbol();
}