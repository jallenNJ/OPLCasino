#ifndef DECK_H
#define DECK_H
#include "Card.h"
#include <vector>

using namespace std;
class Deck {
public:
	Deck();

private:
	char suitLetters[4];
	char specialChar[5];
	
	vector<Card> allCards;

	void intializeCards();
};



#endif // !DECK_H


