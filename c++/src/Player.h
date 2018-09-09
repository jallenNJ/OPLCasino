#ifndef PLAYER_H
#define PLAYER_H
#include <string>
#include <vector>
#include "Card.h"
using namespace std;

 class Player {
public:
	enum Actions { Capture, Build, Trail };
	Player();
	virtual int doTurn(vector<Card>, vector<Card>) = 0 ;
	string getName() {
		return name;
	}

protected:

	string name;
	virtual void setName();
	


};

#endif // !PLAYER_H
