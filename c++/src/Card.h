#ifndef CARD_H
#define CARD_H

class Card {
public:
	Card(char, char);

	string toString() {
		return ""+suit + symbol;
	}
private:
	char suit;
	char symbol;



};


#endif // !CARD_H


