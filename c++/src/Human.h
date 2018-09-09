#ifndef HUMAN_H
#define HUMAN_H
#include "Player.h"
#include <iostream>


using namespace std;
class Human : public Player {
public:
	Human();

protected:
	virtual void setName() override;
private:
	

};



#endif // !HUMAN_H
