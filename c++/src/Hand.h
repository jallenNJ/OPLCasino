#ifndef HAND_H
#define HAND_H
#include <vector>
#include "Card.h"
#include <string>


using namespace std;
class Hand {
public:
	Hand() {
		cardsInHand.reserve(4);
	}

	inline bool addCard(Card newCard) {
		cardsInHand.push_back(newCard);
		return true;
	}


	inline Card getCardCopy(unsigned int index) {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = cardsInHand[index];
		return chosen;
	}

	inline Card removeCard(unsigned int index) {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = cardsInHand[index];
		cardsInHand.erase(cardsInHand.begin() + index);
		return chosen;

	}

	inline bool containsCardValue(int value) {
		for (unsigned int i = 0; i < cardsInHand.size(); i++) {
			if (cardsInHand[i].getNumericValue() == value) {
				return true;
			}
		}
		return false;
	}

	inline string cardToString(int index) {
		if (index > cardsInHand.size()) {
			return "";
		}
		return cardsInHand[index].toString();
	}

	const unsigned int handSize() {
		return cardsInHand.size();
	}

	string toFormattedString();
private:
	vector<Card> cardsInHand;

};
#endif // !HAND_H