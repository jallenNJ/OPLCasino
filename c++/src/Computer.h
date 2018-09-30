#ifndef COMPUTER_H
#define COMPUTER_H
#include "Player.h"
#include "Human.h"
class Computer : public Player {
	
public:
	Computer();
	Computer(bool);

	Computer(Human& copy) {
		name = "Advisor";
		playerHand = Hand(copy.getHandString());
		buildValues = copy.getReservedValues();
	}
protected:
	virtual bool setName() override;
	virtual PlayerMove doTurn(Hand) override;// { Client::outputString("Ai played " + playerHand.cardToString(0)); return PlayerMove(Trail, playerHand.removeCard(0), vector<int>(1)); }
private:
	vector<int> decideBestBuild(vector<vector<int>> options);
	string cardIndiciesToString(Hand, vector<int>);
};


#endif // !COMPUTER_H
