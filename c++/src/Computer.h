#ifndef COMPUTER_H
#define COMPUTER_H
#include "Player.h"
class Computer : public Player {

public:
	Computer();
protected:
	virtual void setName() override;
	virtual PlayerMove doTurn(vector<Card>, vector<Card>) override { return PlayerMove(Trail,1,1); }
private:

};


#endif // !COMPUTER_H
