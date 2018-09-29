#ifndef TOURNAMENT_H
#define TOURNAMENT_H
#include <vector>
#include"Round.h"
#include "Serializer.h"


using namespace std;
class Tournament {
public:
	Tournament();
	~Tournament();
	void RunTournament();


private:
	vector<Round> allRounds;
	int scores[2];
	int checkForWinner();
	int roundNumber;

	bool checkForSaveFileLoad();

	const int POINTS_TO_WIN = 21;
	const int TIE = 2;
};


#endif // !TOURNAMENT_H





