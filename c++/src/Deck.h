#ifndef DECK_H
#define DECK_H
#include "Card.h"
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

typedef	std::_Vector_iterator<std::_Vector_val<std::_Simple_types<Card>>> cardIterator;

class Deck {
public:
	Deck();

	Card drawCard() {
		if (isEmpty) {
			abort();
		}

		Card toReturn = *topOfDeck;
		topOfDeck++;
		if (topOfDeck == allCards.end()) {
			isEmpty = true;
		}
		return toReturn;

	}

private:
	char suitLetters[4];
	char specialChar[5]; 
	vector<Card> allCards;

	cardIterator topOfDeck;
	bool isEmpty;

	void intializeCards();
	inline void shuffleCards() {
		random_shuffle(allCards.begin(), allCards.end());
	}
	

};



#endif // !DECK_H


