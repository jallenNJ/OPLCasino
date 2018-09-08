#include "Deck.h"


Deck::Deck(){
	suitLetters[0] = 'C';
	suitLetters[1] = 'D';
	suitLetters[2] = 'H';
	suitLetters[3] = 'S';

	specialChar[0] = 'A';
	specialChar[1] = 'X';
	specialChar[2] = 'J';
	specialChar[3] = 'Q';
	specialChar[4] = 'K';


	intializeCards();
	shuffleCards();
	topOfDeck = allCards.begin();
	empty = false;

}

void Deck::intializeCards() {
	allCards.reserve(52);
	for (int i = 0; i < 4; i++) {
		allCards.push_back(Card(suitLetters[i], specialChar[0]));
		for (char j = '2'; j <= '9'; j++) {
			allCards.push_back(Card(suitLetters[i], j));
		}
		for (int j = 1; j < 5; j++) {
			allCards.push_back(Card(suitLetters[i], specialChar[j]));
		}
	}
	
}
