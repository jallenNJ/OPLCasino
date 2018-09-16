#ifndef DECK_H
#define DECK_H
#include "Card.h"
#include "PlayingCard.h"
#include <vector>
#include <algorithm>
#include <queue>
using namespace std;

typedef	_Vector_iterator<_Vector_val<_Simple_types<Card>>> cardIterator;

class Deck {
public:
	Deck();

	Card drawCard() {
		if (empty) {
			Client::outputError("Drawing from empty deck, returning invalid card");
			return Card();
		}

		Card toReturn = *topOfDeck;
		topOfDeck++;
		if (topOfDeck == allCards.end()) {
			empty = true;
		}
		return toReturn;

	}

	bool isEmpty() {
		return empty;
	}

private:
	char suitLetters[4];
	char specialChar[5]; 
	vector<Card> allCards;

	cardIterator topOfDeck;
	bool empty;

	void intializeCards();
	inline void shuffleCards() {
		random_shuffle(allCards.begin(), allCards.end());
	}
	

};



#endif // !DECK_H