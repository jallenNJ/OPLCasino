#ifndef CARD_H
#define CARD_H
#include <string>
#include <vector>
using namespace std;
class Card {
	
public:
	Card(char, char);

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
private:
	char suit;
	char symbol;
	int numericValue;
	void symbolToNumericValue();


	
};


#endif // !CARD_H


