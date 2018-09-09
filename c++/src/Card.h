#ifndef CARD_H
#define CARD_H
#include <string>
using namespace std;
class Card {
	
public:
	Card(char, char);

	string toString() {
		return string(1, suit)+ string(1, symbol);
	}

	bool checkCapture(Card);

	char getSymbol() {
		return symbol;
	}
private:
	char suit;
	char symbol;



};


#endif // !CARD_H


