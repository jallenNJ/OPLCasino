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
	vector<Build> buildsInProgress;
	void fillLooseCards() {
		string savedTable = Serializer::getTableCards();
		if (savedTable.length() >= 0) {
			vector<string> tokens = Serializer::parseLine(savedTable);
			for (unsigned int i = 0; i < tokens.size(); i++) {
				string token = tokens[i];
				if (token[0] == '[') {
					buildsInProgress.push_back(Build());
					if (token.length() == 1) {
						continue;
					}
					token = token.substr(1);
					
				}
				else if (token[0] == ']') {
					looseCards.addCard(buildsInProgress.back());
					buildsInProgress.pop_back();
					continue;

				}

				char cardSuit = token[0];
				char cardSymbol = token[1];
				if (buildsInProgress.size() > 0) {
					buildsInProgress.back().addCardToBuild(PlayingCard(cardSuit, cardSymbol));
					if (token.length() > 2) {
						if (token[2] == ']') {
							//DUPE CODE, MOVE TO INLINE
							looseCards.addCard(buildsInProgress.back());
							buildsInProgress.pop_back();

						}
					}
				}
				else {

					looseCards.addCard(PlayingCard(cardSuit, cardSymbol));
				}
				
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