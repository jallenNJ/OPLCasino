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

	inline void addCard(Card newCard) {
		cardsInHand.push_back(newCard);
	}

	inline Card removeCard(unsigned int index) {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = cardsInHand[index];
		cardsInHand.erase(cardsInHand.begin() + index);
		return chosen;

	}

	string toFormattedString();
private:
	vector<Card> cardsInHand;

};
#endif // !HAND_H
