#ifndef TABLE_H
#define TABLE_H
#include"Card.h"
#include <vector>
#include "Deck.h"
#include <iostream>
#include "Player.h"
#include "Human.h"
#include "Computer.h"
#include <iomanip>
#include "Hand.h"

using namespace std;
class Table {
public:
	Table();
	~Table();
	const void printBoard();
	bool runCycle();
	
private:


	static const int HAND_SIZE = 4;
	//vector<Card> hands[2]; 
	//vector<Card> looseCards;
	//vector<Card> playerPile;
	//vector<Card> computerPile;
	Hand looseCards;
	Deck* deck; 
	Player** players;


	/*void fillHand(vector<Card>* cardVec) {
		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			cardVec->push_back(deck->drawCard());
		}
	}*/

	void fillLooseCards() {
		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			looseCards.addCard(deck->drawCard());
		}
	}

	void fillHand(int playerIndex) {
		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			players[playerIndex]->addToHand(deck->drawCard());
		}
	}

	void printHand(vector<Card>* cardVec) {
		for (unsigned int i = 0; i < cardVec->size(); i++) {
			cout << "" + (*cardVec)[i].toString() << " ";
		}
	}

	void doPlayerMove(int player);
	
};

#endif // !TABLE_H