#include "Build.h"

/* *********************************************************************
Function Name: addCardToBuild
Purpose: To add another card into the build
Parameters:
			newCards, a Card passed by value. If this card doesn't exceed 14, it will be added
Return Value: bool. True if added, false if rejected
Local Variables:
			n/a
Algorithm:
			1) Check to make sure the Card is valid
			2) Add to data structure
Assistance Received: none
********************************************************************* */
bool Build::addCardToBuild(Card& newCard) {
	//if (newCard.getNumericValue() + getNumericValue() > 14) {

	//}

	Card* add = instanitateCopy(newCard);

	cardsInBuild.push_back(add);
	if (add->getSuit() == 'B' && cardsInBuild.size() > 1) {
		if (add->getNumericValue() != getNumericValue()) {
			Client::outputError("Multibuild potentially created with differing values");
			char outputC = getSymbol();
			Client::outputError("Acting value for " + toString() + " is " + outputC);
		}
		return true;
	}

	if (cardsInBuild.size() == 1) {
		symbol = newCard.getSymbol();

	}
	else {
		symbol = numericValueToSymbol(getNumericValue() + newCard.getNumericValue());
	}

	symbolToNumericValue();

	suit = 'B';
	return true;
}