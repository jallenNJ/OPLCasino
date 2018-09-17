#ifndef HAND_H
#define HAND_H
#include <vector>
#include "Card.h"
#include "Build.h"
#include <string>


using namespace std;
class Hand {
public:
	Hand() {
		cardsInHand.reserve(4);
	}

	inline bool addCard(Card newCard) {
		Card* toAdd = new Card(newCard);
		cardsInHand.push_back(toAdd);
		return true;
	}

	inline bool addCard(Build newBuild) {
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

		Card* toAdd = new Build(newBuild);
		cardsInHand.push_back(toAdd);
		return true;

	}


	inline Card getCardCopy(unsigned int index) const {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = *cardsInHand[index];
		return chosen;
	}

	inline Card removeCard(unsigned int index) {
		if (index >= cardsInHand.size()) {
			return Card();
		}
		Card chosen = *cardsInHand[index];
		delete cardsInHand[index];
		cardsInHand.erase(cardsInHand.begin() + index);
		return chosen;

	}

	inline bool containsCardValue(int value) const {
		for (unsigned int i = 0; i < cardsInHand.size(); i++) {
			if (cardsInHand[i]->getNumericValue() == value) {
				return true;
			}
		}
		return false;
	}

	inline string cardToString(int index) const{
		if (index > cardsInHand.size()) {
			return "";
		}
		return cardsInHand[index]->toString();
	}

	const unsigned int handSize() const{
		return (unsigned int)cardsInHand.size();
	}

	string toFormattedString() const;
	int countSymbol(char) const;
private:
	vector<Card*> cardsInHand;

};
#endif // !HAND_H
