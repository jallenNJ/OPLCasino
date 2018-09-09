#ifndef PLAYER_H
#define PLAYER_H
#include <string>

using namespace std;

 class Player {
public:
	Player();
	virtual int doTurn(int) = 0 ;
	string getName() {
		return name;
	}

protected:

	string name;
	virtual void setName();
	


};

#endif // !PLAYER_H
