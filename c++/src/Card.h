#ifndef CARD_H
#define CARD_H
#include <string>
#include <vector>
using namespace std;
class Card {
	
public:
	Card() {
		setInvalidCard();
	}

	virtual const string toString() {
		return string(1, suit)+ string(1, symbol);
	}
	
	const bool checkCapture(Card);
	const bool checkCapture(vector<Card>);

	const virtual char getSymbol() {
		return symbol;
	}

	//Aces return 1
	const virtual int getNumericValue() {
		return numericValue;
	}

	const bool isInvalid() {
		return suit == 'N';
	}

	const char getSuit() {
		return suit;
	}
	const bool setOwner(string name) {
		ownerName = name;
	}
	const string getOwner() {
		return ownerName;
	}

protected:
	char suit;
	char symbol;
	string ownerName;

	void symbolToNumericValue();
private:
	int numericValue;
	void setInvalidCard() {
		suit = 'N';
		symbol = '0';
	}

	
};


#endif // !CARD_H


