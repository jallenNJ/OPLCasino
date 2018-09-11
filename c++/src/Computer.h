#ifndef COMPUTER_H
#define COMPUTER_H
#include "Player.h"
class Computer : public Player {

public:
	Computer();
protected:
	virtual void setName() override;
	virtual PlayerMove doTurn(Hand) override { return PlayerMove(Trail,Card(),vector<int>(1)); }
private:

};


#endif // !COMPUTER_H
