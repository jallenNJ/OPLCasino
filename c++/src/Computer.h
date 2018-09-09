#ifndef COMPUTER_H
#define COMPUTER_H
#include "Player.h"
class Computer : public Player {

public:
	Computer();
protected:
	virtual void setName() override;
	virtual int doTurn(vector<Card>, vector<Card>) override { return -10; }
private:

};


#endif // !COMPUTER_H
