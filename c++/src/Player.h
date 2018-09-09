#ifndef PLAYER_H
#define PLAYER_H
#include <string>

using namespace std;

class Player {
public:
	Player();
	void doTurn();
	string getName() {
		return name;
	}

protected:

	string name;
	virtual void setName();
	


};

#endif // !PLAYER_H
