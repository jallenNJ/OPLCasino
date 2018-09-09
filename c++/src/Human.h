#ifndef HUMAN_H
#define HUMAN_H
#include "Player.h"
#include <iostream>


using namespace std;
class Human : public Player {
	
public:
	Human();

	virtual PlayerMove doTurn(vector<Card>, vector<Card>) override;

protected:
	virtual void setName() override;
private:
	Actions promptForAction();
	int promptForCardToUse(int, bool = false);

};



#endif // !HUMAN_H
