#include "Deck.h"


/* *********************************************************************
Function Name: Deck
Purpose: Intialize the object
Parameters:
			none
Return Value: Constructor
Local Variables:
			None
Algorithm:
			1) Intialize all the Special Suit letters and symbols
			2) Create all the cards
			3) Shuffle them
			4) Intialize remaining variables
Assistance Received: none
********************************************************************* */
Deck::Deck(){

	//Define the symbols per the game rules
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

/* *********************************************************************
Function Name: intializeCards
Purpose: To ensure all cards have been intialized
Parameters:
			None
Return Value: None, intializes member variables
Local Variables:
			None
Algorithm:
		For each card:
			1) Create A Card
			2) Add to Deck
Assistance Received: none
********************************************************************* */
void Deck::intializeCards() {
	//Allocate all the memory in one go
	allCards.reserve(52);
	//For every Suit
	for (int i = 0; i < 4; i++) {
		//Create the ace
		allCards.push_back(PlayingCard(suitLetters[i], specialChar[0]));
		//Create the numberic cards
		for (char j = '2'; j <= '9'; j++) {
			allCards.push_back(PlayingCard(suitLetters[i], j));
		}
		//Create all other special symbols except for the Ace
		for (int j = 1; j < 5; j++) {
			allCards.push_back(PlayingCard(suitLetters[i], specialChar[j]));
		}
	}
	
}
