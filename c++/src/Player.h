#ifndef PLAYER_H
#define PLAYER_H
#include <string>
#include <vector>
#include "Card.h"
#include "Hand.h"

using namespace std;
 class Player {


public:
	enum Actions { Capture, Build, Trail };
	struct PlayerMove {
		Player::Actions actionTaken;
		int handIndex;
		vector<int> targetIndex;
		PlayerMove(Actions aT, int hI, vector<int> tI) {
			actionTaken = aT;
			handIndex = hI;
			//targetIndex = tI;
			for (unsigned int i = 0; i < tI.size(); i++) {
				targetIndex.push_back(tI[i]);
			}
		}
	};
	Player();
	virtual PlayerMove doTurn(vector<Card>, vector<Card>) = 0 ;
	string getName() {
		return name;
	}

	void addToHand(Card toAdd) {
		playerHand.addCard(toAdd);
	}

	string toFormattedString() {
		return playerHand.toFormattedString();
	}

protected:

	string name;
	virtual void setName() = 0;
	Hand playerHand;
	Hand playerPile;


	bool captureCard(Card, Card);
	bool captureCard(Card, vector<Card>);
	bool createBuild(Card, vector<Card>, vector<Card>);
	


};

#endif // !PLAYER_H
