#ifndef PLAYER_H
#define PLAYER_H
#include <string>
#include <vector>
#include "Card.h"
#include "Hand.h"
#include <algorithm>
#include "Client.h"

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
	string getName() {
		return name;
	}

	void addToHand(Card toAdd) {
		playerHand.addCard(toAdd);
	}

	void addToPile(Card toAdd) {
		playerPile.addCard(toAdd);
	}

	string toFormattedString() {
		return playerHand.toFormattedString();
	}

	const int getHandSize() {
		return playerHand.handSize();
	}

protected:

	string name;
	virtual void setName() = 0;
	Hand playerHand;
	Hand playerPile;


	bool captureCard(Card, Card);
	bool captureCard(Card, vector<Card>);
	bool createBuild(Card, vector<Card>);
	


};

#endif // !PLAYER_H
