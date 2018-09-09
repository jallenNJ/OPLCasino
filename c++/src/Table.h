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

using namespace std;
class Table {
public:
	Table();
	~Table();
	void printBoard();
	
private:


	static const int HAND_SIZE = 4;
	vector<Card> playerHand;
	vector<Card> computerHand;
	vector<Card> looseCards;
	vector<Card> playerPile;
	vector<Card> computerPile;
	Deck* deck; 
	Player** players;


	void fillHand(vector<Card>* cardVec) {
		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			cardVec->push_back(deck->drawCard());
		}
	}

	void printHand(vector<Card>* cardVec) {
		for (int i = 0; i < cardVec->size(); i++) {
			cout << "" + (*cardVec)[i].toString() << " ";
		}
	}

	
};

#endif // !TABLE_H