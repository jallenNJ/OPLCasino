#ifndef TABLE_H
#define TABLE_H
#include"Card.h"
#include <vector>
#include "Deck.h"
#include <iostream>
using namespace std;
class Table {
public:
	Table();
	~Table();

	
private:


	static const int HAND_SIZE = 4;
	vector<Card> playerHand;
	vector<Card> computerHand;
	vector<Card> looseCards;
	vector<Card> playerPile;
	vector<Card> computerPile;
	Deck* deck; 

	void fillHand(vector<Card>* cardVec) {
		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			cardVec->push_back(deck->drawCard());
		}
	}

	void printBoard();
};

#endif // !TABLE_H