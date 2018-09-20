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
#include "Build.h"
#include "Serializer.h"

using namespace std;
class Table {
public:
	Table();
	Table(bool);
	Table(bool, bool);
	~Table();
	const void printBoard();
	bool runCycle();
	
private:
	bool humanFirst;
	void initTable(bool = true);
	void initTable(bool, bool);
	static const int HAND_SIZE = 4;
	Hand looseCards;
	Deck* deck; 
	Player** players;

	void fillLooseCards() {
		string savedTable = Serializer::getTableCards();
		if (savedTable.length() >= 0) {
			vector<string> tokens = Serializer::parseLine(savedTable);
			Build buildInProgress;
			for (unsigned int i = 0; i < tokens.size(); i++) {
				if (tokens[i][0] == '[') {
					//Client::outputError("TABLE NEEDS TO BE PROGRAMED TO HANDLE BUILDS");

					continue;
				}
				else if (tokens[i][0] == ']') {

				}
				char cardSuit = tokens[i][0];
				char cardSymbol = tokens[i][1];
				looseCards.addCard(PlayingCard(cardSuit, cardSymbol));
			}
			return;
		}

		for (int i = 0; i < 4; i++) {
			if (deck->isEmpty()) {
				return;
			}
			looseCards.addCard(deck->drawCard());
		}
	}


	void fillHand(int playerIndex) const{
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