#ifndef HUMAN_H
#define HUMAN_H
#include "Player.h"



using namespace std;
class Human : public Player {
	
public:
	Human();
	Human(bool);

	virtual PlayerMove doTurn(Hand) override;

protected:
	virtual bool setName() override;
private:
	Actions promptForAction();
	vector<int> promptForCardToUse(int, bool = false);
	vector<vector<int>> getOptionialInput(vector<int> required, Hand tableCards, int targetValue);
	vector<int> getSelectionOfCards(vector<int> required, Hand tableCards, int targetValue);

};



#endif // !HUMAN_H
