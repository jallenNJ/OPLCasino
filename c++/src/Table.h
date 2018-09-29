#ifndef TABLE_H
#define TABLE_H
#include"Card.h"
#include <vector>
#include "Deck.h"
#include <iostream>
#include "Player.h"
#include "Human.h"
#include "Computer.h"
#include "Hand.h"
#include "Build.h"
#include "Serializer.h"
#include <queue>

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
	void serilizeAllObjects();


	Hand looseCards;
	Deck* deck; 
	Player** players;
	vector<Build> buildsInProgress;
	void fillLooseCards(); 

	void processPoppedBuild(vector<Build>&);

	void captureRemaingCards();

	void fillHand(int playerIndex) const{
		if (players[playerIndex]->getHandSize() > 0) {
			return;
		}
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

	void createSuggestedMove();

	void actionMenu();

	queue<int> nextPlayerIndex;
	int lastCapture;
	
};

#endif // !TABLE_H