#ifndef HAND_H
#define HAND_H
#include <vector>
#include "Card.h"
#include "PlayingCard.h"
#include "Build.h"
#include <string>
#include "Serializer.h"


using namespace std;
class Hand {
public:
	Hand() {
		cardsInHand.reserve(4);
	}
	Hand(string);


	inline bool addCard(Card newCard) {
		Card* toAdd = new Card(newCard);
		cardsInHand.push_back(toAdd);
		return true;
	}

	 bool addCard(vector<Card> newCards) {
		 for (unsigned int i = 0; i < newCards.size(); i++) {
			 addCard(newCards[i]);
		}
		 return true;
	}
	bool addCard(Build);

	inline Card getCardCopy(unsigned int index) const {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = *cardsInHand[index];
		return chosen;
	}

	Card removeCard(unsigned int);
	

	inline bool containsCardValue(int value) const {
		for (unsigned int i = 0; i < cardsInHand.size(); i++) {
			if (cardsInHand[i]->getNumericValue() == value) {
				return true;
			}
		}
		return false;
	}

	inline string cardToString(unsigned int index) const{
		if (index > cardsInHand.size()) {
			return "";
		}
		return cardsInHand[index]->toString();
	}

	const unsigned int handSize() const{
		return (unsigned int)cardsInHand.size();
	}

	string toFormattedString() const;
	string toString() const;
	int countSymbol(char) const;

	int containsCard(char cardSuit, char cardSym) {
		for (unsigned int i = 0; i < cardsInHand.size(); i++) {
			Card* current = cardsInHand[i];
			if (current->getSuit() == cardSuit && current->getSymbol() == cardSym) {
				return i;
			}
		}
		return -1;

	}

	string toStringOfIndex(unsigned int i) {
		if (i >= cardsInHand.size()) {
			return "";
		}
		return cardsInHand[i]->toString();
	}

	int getAmountOfSuit(char) const;

	Card* removeCardAsReference(unsigned int);

private:
	vector<Card*> cardsInHand;

};
#endif // !HAND_H
