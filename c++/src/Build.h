#ifndef BUILD_H
#define BUILD_H
#include "Card.h"
#include <vector>
#include "PlayingCard.h"
#include "Client.h"

using namespace std;
class Build : public Card {
public:
	Build() {
		symbol = '0';
	}

	~Build() {
		//for (unsigned int i = 0; i < cardsInBuild.size(); i++) {
		//	delete cardsInBuild[i];
		//}
	}

/* *********************************************************************
Function Name: Build()
Purpose: Constructor that takes in information to create a build
Parameters:
			Card First, A card passed by value which will be copied as the first card in the build
			player, a string, which is the name of the player who owns the build
Return Value: Constructor[Object itself]
Local Variables:
			n/a
Algorithm:
			1) Assigned all variables to the appropriate intialized value
			2) Copy first into the main vector
Assistance Received: none
********************************************************************* */
	Build(Card& first, string player) {
		ownerName = player;

		cardsInBuild.push_back(instanitateCopy(first));
		suit = 'B';
		symbol = first.getSymbol();
		

	}

	Build(Build& copy, string owner) {
		this->cardsInBuild = copy.cardsInBuild;
		ownerName = owner;
		suit = 'B';
		symbol = copy.getSymbol();
	}


/* *********************************************************************
Function Name: toString
Purpose: To format the object to be printed out when being displayed on the table
Parameters:
			void
Return Value: The formatted object. Will be
	[ Card0 Card1 Card2 ] where Card0 is the card in the 0th index of the vector calling its own toString() function
Local Variables:
			string convertted, the formatted string as its being built
Algorithm:
			1) Places the outside brackers []
			2) Loop through all cards in the build and call their toString function()
Assistance Received: none
********************************************************************* */

	virtual const string toString() const override {
		string convertted = "[ ";
		for (unsigned int i = 0; i < cardsInBuild.size(); i++) {
			convertted += cardsInBuild[i]->toString() + " ";
		}
		convertted += "]";
		return convertted;
	}

	/* *********************************************************************
Function Name: getSymbol
Purpose: To get the symbol that represents the value of the build
Parameters:
			void
Return Value: a const char that is 2-9 or A,X,J,Q,K
Local Variables:
			nValue, an int, which reprents the sum of all cards in the build
Algorithm:
			1) Get the numeric value of the build
			2) Return the symbol based on the value
Assistance Received: none
********************************************************************* */

	const virtual char getSymbol() const override {
		int nValue = getNumericValue();
		switch (nValue) {
			//Aces are both 1 and 14, therefore the same symbol
			case 1:
			case 14:
				return 'A';
			case 10:
				return 'X';
			case 11:
				return 'J';
			case 12:
				return 'Q';
			case 13:
				return 'K';
		default://2-9
			return (char)('0' + nValue);
		}
	}

	/* *********************************************************************
Function Name: getNumericValue
Purpose: To get the sum of all Cards in the build
Parameters:
			void
Return Value: A const int, of the value of the build. [2-14] are valid values
Local Variables:
			sum, an int, the summation of all the cards
Algorithm:
			1) Sum the value of all the cards
			2) Return the value
Assistance Received: none
********************************************************************* */

	/*const virtual int getNumericValue()  override {
		/*int sum = 0;
		for (unsigned int i = 0; i < cardsInBuild.size(); i++) {
			sum += cardsInBuild[i]->getNumericValue();
		}

		return sum;*/
		//
	/*	if (numericValue < 1) {
			symbolToNumericValue();
		}
		return numericValue;
	}*/

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
	inline bool addCardToBuild(Card& newCard) {
		//if (newCard.getNumericValue() + getNumericValue() > 14) {
			
		//}

		Card* add = instanitateCopy(newCard);

		cardsInBuild.push_back(add);
		if (add->getSuit() == 'B' && cardsInBuild.size() > 1) {
			if (add->getNumericValue() != getNumericValue()) {
				Client::outputError("Multibuild created with differing values");
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

private:
	vector<Card*> cardsInBuild;

	Card* instanitateCopy(Card& toCopy) {
		if (toCopy.getSuit() == 'B') {
			return new Build((*dynamic_cast<Build*>(&toCopy)), toCopy.getOwner());
		}
		else {
			return new PlayingCard(toCopy);
		}
	}


};


#endif // !BUILD_H
