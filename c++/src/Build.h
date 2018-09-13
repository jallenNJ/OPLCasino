#ifndef BUILD_H
#define BUILD_H
#include "Card.h"
#include <vector>

using namespace std;
class Build : public Card {
public:
	Build() {
		buildOwner = 0;
	}
	Build(Card first, int player) {
		buildOwner = player;
		cardsInBuild.push_back(first);
		suit = 'B';
		symbol = getSymbol();

	}

	virtual const string toString() override{
		string convertted = "[ ";
		for (unsigned int i = 0; i < cardsInBuild.size(); i++) {
			convertted += cardsInBuild[i].toString() + " ";
		}
		convertted += "]";
		return convertted;
	}

	const virtual char getSymbol() override {
		int nValue = getNumericValue();
		switch (nValue) {
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

	const virtual int getNumericValue() override {
		int sum = 0;
		for (int i = 0; i < cardsInBuild.size(); i++) {
			sum += cardsInBuild[i].getNumericValue();
		}
		return sum;
	}

	inline bool addCardToBuild(Card newCard) {
		//TODO: Add check to make sure doesn't exceed 14 etc
		if (newCard.getNumericValue() + getNumericValue() > 14) {
			return false;
		}
		cardsInBuild.push_back(newCard);
		symbol = getSymbol();
		return true;
	}

private:
	vector<Card> cardsInBuild;
	int buildOwner;


};


#endif // !BUILD_H
