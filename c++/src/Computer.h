#ifndef COMPUTER_H
#define COMPUTER_H
#include "Player.h"

class Computer : public Player {

public:
	Computer();
protected:
	virtual void setName() override;
	virtual PlayerMove doTurn(Hand) override { Client::outputString("Ai played " + playerHand.cardToString(0)); return PlayerMove(Trail, playerHand.removeCard(0), vector<int>(1)); }
private:

};


#endif // !COMPUTER_H
