#include "Hand.h"


/* *********************************************************************
Function Name: Hand
Purpose: To intialize the object with exsiting cards (Loading Save)
Parameters:
			startingCards, a string for all the cards to be put into the deck
Return Value: Constructor
Local Variables:
			cards, a vector<string> to handled the tokenized parameter
Algorithm:
			1) Get each card seperately
			2) Create that into its own object
			3) Add to the member data object
Assistance Received: none
********************************************************************* */
Hand::Hand(string startingCards) {
	//Call the tokenizer and store the result
	vector<string> cards = Serializer::parseLine(startingCards);
	//For every card
	for (unsigned int i = 0; i < cards.size(); i++) {
		//If its a build, output error for now and add individually
		if (cards[i][0] == '[' || cards[i][0] == ']') {
			Client::outputError("Hand class needs to be taught how to parse builds");
			continue;
		}
		if (cards[i].length() < 2) {
			continue;
		}

		//Add the card
		addCard(PlayingCard(cards[i][0], cards[i][1]));

	}
}

/* *********************************************************************
Function Name: toFormattedString
Purpose: Output all objects in hand with their associated index+1
Parameters:
			none
Return Value: The formatted string in the form
1) Card0 2)Card1
Where each Card calls its own toString function to output data
Local Variables:
			formatted, the formatted string in progress
Algorithm:
			For each card
				1) Add the index + 1 and a ')' to the string
				2) Add the Card to the string

Assistance Received: none
********************************************************************* */
string Hand::toFormattedString() const {
	string formatted = "";
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		formatted += to_string(i + 1) + ")" + cardsInHand[i]->toString() + " ";
	}
	return formatted;


}

string Hand::toString() const {
	string formatted = "";
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		formatted += cardsInHand[i]->toString() + " ";
	}
	return formatted;

}

/* *********************************************************************
Function Name: countSymbol
Purpose: To calculate the amount of time a specified symbol appears in the hand
Parameters:
			symbol, the char to count the occurances of
Return Value: The amount of time the value appears, where 0 is no occurances
Local Variables:
			Sum, the amount of times it is found
Algorithm:
			For each card:
				1) If the symbol matches the target, increment counter
			End For
			2)Return the counter
Assistance Received: none
********************************************************************* */
int Hand::countSymbol(char symbol) const{
	int sum = 0;
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		if (cardsInHand[i]->getSymbol() == symbol) {
			sum++;
		}
	}
	return sum;
}
/* *********************************************************************
Function Name: addCard
Purpose: To add a build into the hand
Parameters:
			newBuild, the build passed by value to be added to the deck
Return Value: A bool for if it succeeded. True means its valid, false means it failed
Local Variables:
			Build*:
				Originial: A pointer to the build in the vector
				newMutli: The new multi build to add to the object
Algorithm:
			1) Find A build
			2) See if it matches the value, if not, jump to 4
			3) Add the multi build and return true
			4) If not, add as a new build
Assistance Received: none
********************************************************************* */

 bool Hand::addCard(Build newBuild) {
	 if (newBuild.getSuit() == 'N') {
		 return false;
	 }
	for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		//TODO: Test if this works when serializing is working
		if (cardsInHand[i]->getSuit() == 'B') {
			if (cardsInHand[i]->getNumericValue() == newBuild.getNumericValue()) {
				Build* original = dynamic_cast<Build*>(cardsInHand[i]);
				Build* newMulti = new Build(*original);
				newMulti->addCardToBuild(newBuild);
				delete cardsInHand[i];
				cardsInHand[i] = newMulti;
				return true;
			}
		}
	}

	//Memory is free in the above delete
	Card* toAdd = new Build(newBuild);
	cardsInHand.push_back(toAdd);
	return true;

}

 /* *********************************************************************
Function Name: removeCard
Purpose: To remove and return a copy of the Card at the specified index
Parameters:
			index, an unsigned int index to the location in the array to remove from
Return Value: A copy of the card removed
Local Variables:
			Chosen: The copy of the Card to return
Algorithm:
			1) Find the card in the hand
			2) Copy it
			3) Deallocate the orignial
			4) Return the copy
Assistance Received: none
********************************************************************* */

 Card Hand::removeCard(unsigned int index) {
	 if (index >= cardsInHand.size()) {
		 return Card();
	 }
	 Card chosen = *cardsInHand[index];
	 delete cardsInHand[index];
	 cardsInHand.erase(cardsInHand.begin() + index);
	 return chosen;

 }


 int Hand::getAmountOfSuit(char targetSuit) const {
	 int hits = 0;

	 for (unsigned int i = 0; i < cardsInHand.size(); i++) {
		 if (cardsInHand[i]->getSuit() == targetSuit) {
			 hits++;
		 }
	 }
	 return hits;
 }