#include "Card.h"


const bool Card::checkCapture(Card other) {
	return getSymbol() == other.getSymbol();
}


/* *********************************************************************
Function Name: checkCapture
Purpose: To see if a card can capture multiple cards
Parameters:
			cardsToCheck, a vector<Card> passed by value of all the cards being captured
Return Value: A bool for if it can be caputred. True if it can be, false if it can't be
Local Variables:
			int sum, the sum of all the cards being captured
Algorithm:
			1) Find the sum of the cards
			2) Check to see if the value matches the card
Assistance Received: none
********************************************************************* */
const bool Card::checkCapture(vector<Card> cardsToCheck) {
	//Calculate the sum of all cards
	int sum = 0;
	for (unsigned int i = 0; i < cardsToCheck.size(); i++) {
		sum += cardsToCheck[i].getNumericValue();
	}
	//If they exceed max of any played card, return false
	if (sum > 14) {
		return false; 
	}
	//If value matches, return true
	if (sum == getNumericValue()) {
		return true;
	}
	//If this card is an ace acting as Aces high
	if (sum == 14 && getSymbol() == 'A') {
		return true;
	}

	//Return false if fell through all above cases
	return false;

}

/* *********************************************************************
Function Name: symbolToNumericValue()
Purpose: To convert the face symbol to its numeric value
Parameters:
			none (uses memeber varible symbol)
Return Value: Void (sets member numericValue) [1-13]
Local Variables:
			none
Algorithm:
			1) Map the symbol to its numeric value
Assistance Received: none
********************************************************************* */

void Card::symbolToNumericValue() {
	//If its a digit, convert it to the ascii form of itself
	if (isdigit(symbol)) {
		numericValue = (int)(symbol - '0');
		return;
	}
	//Else, map the letter to its associated value per the game rules
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
		default: //Catch for invalid values
			numericValue = 1;
			break;

	}
	return;

}