#ifndef PLAYING_CARD_H
#define PLAYING_CARD_H
#include "Card.h"


class PlayingCard : public Card {

public:
	//Call superclass and no more
	PlayingCard() { };

	PlayingCard(Card card) {
		suit = card.getSuit();
		symbol = card.getSymbol();
		ownerName = card.getOwner();
	}
	PlayingCard(char, char);

protected:



private:





};




#endif // !PLAYING_CARD_H
