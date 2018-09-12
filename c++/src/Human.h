#ifndef HUMAN_H
#define HUMAN_H
#include "Player.h"



using namespace std;
class Human : public Player {
	
public:
	Human();

	virtual PlayerMove doTurn(Hand) override;

protected:
	virtual bool setName() override;
private:
	Actions promptForAction();
	vector<int> promptForCardToUse(int, bool = false);

};



#endif // !HUMAN_H
