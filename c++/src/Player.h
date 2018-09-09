#ifndef PLAYER_H
#define PLAYER_H
#include <string>
#include <vector>
#include "Card.h"
using namespace std;

 class Player {


public:
	enum Actions { Capture, Build, Trail };
	struct PlayerMove {
		Player::Actions actionTaken;
		int handIndex;
		int targetIndex;
		PlayerMove(Actions aT, int hI, int tI) {
			actionTaken = aT;
			handIndex = hI;
			targetIndex = tI;
		}
	};
	Player();
	virtual PlayerMove doTurn(vector<Card>, vector<Card>) = 0 ;
	string getName() {
		return name;
	}

protected:

	string name;
	virtual void setName();
	bool captureCard(Card, Card);
	


};

#endif // !PLAYER_H
