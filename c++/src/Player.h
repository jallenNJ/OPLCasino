#ifndef PLAYER_H
#define PLAYER_H
#include <string>
#include <vector>
#include "Card.h"
#include "Hand.h"
#include <algorithm>
#include "Client.h"
#include "Serializer.h"

using namespace std;
 class Player {


public:
	enum Actions { Capture, Build, Trail };
	struct PlayerMove {
		Player::Actions actionTaken;
		Card playedCard;
		vector<int> targetIndex;
		PlayerMove(Actions aT, Card pC, vector<int> tI) {
			actionTaken = aT;
			playedCard = pC;

			for (unsigned int i = 0; i < tI.size(); i++) {
				targetIndex.push_back(tI[i]);
			}
		}
	};
	Player();
	virtual PlayerMove doTurn(Hand) = 0 ;
	const string getName() const{
		return name;
	}

	bool addToHand(Card toAdd) {
		playerHand.addCard(toAdd);
		return true;
	}

	bool addToPile(Card toAdd) {
		playerPile.addCard(toAdd);
		return true;
	}

	string toFormattedString() const{
		return playerHand.toFormattedString();
	}

	Serializer::PlayerInfo saveSelf() {
		return Serializer::PlayerInfo(getName(), 0, playerHand.toString(), playerPile.toString());
	}

	const int getHandSize() const{
		return playerHand.handSize();
	}

	bool reserveCardValue(int value) {
		if (value < 1 || value > 14) {
			return false;
		}
		buildValues.push_back(value);
		return true;
	}

	bool releaseBuildValue(int value) {
		int location = findReservedValue(value);
		if (location < 0) {
			return false;
		}
		buildValues.erase(buildValues.begin() + location);
		return true;
	}

	vector<int> findRequiredCaptures(PlayingCard played, Hand table);
	//vector<int> findOptionialCaptures(PlayingCard played, Hand table);

protected:


	int findReservedValue(int value) {
		for (unsigned int i = 0; i < buildValues.size(); i++) {
			if (buildValues[i] == value) {
				return i;
			}
		}
		return -1;
	}

	string name;
	virtual bool setName() = 0;
	Hand playerHand;
	Hand playerPile;
	vector<int> buildValues;


	bool captureCard(Card, Card);
	bool captureCard(Card, vector<Card>);
	bool createBuild(Card, vector<Card>);
	bool checkTrail(Card);
	bool checkReserved(Card);
	int amountOfSymbolInHand(char symbol) {
		return playerHand.countSymbol(symbol);
	}
	vector<vector<int>> findSelectableSets(Card, Hand);

};

#endif // !PLAYER_H
