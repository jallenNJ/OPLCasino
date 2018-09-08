#ifndef ROUND_H
#define ROUND_H
#include <iostream>
#include <random>
#include <ctime>
#include "Table.h"
using namespace std;
class Round {
public:
	
	enum PlayerIndex
	{
		HUMAN_PLAYER = 0,
		COMPUTER_PLAYER = 1
	};

	Round();
	Round(bool);

	~Round() {

	}

	void playRound();

	int getPlayerScore(int player) {
		switch (player) {
		case HUMAN_PLAYER:
			return playerScores[HUMAN_PLAYER];
		case COMPUTER_PLAYER:
			return playerScores[COMPUTER_PLAYER];
		default:
			cerr << "Invalid selection to getPlayerScores, returning -1";
			return -1;
		}
	}

	int getWinner() {
		return playerThatWonRound;
	}
private:

	void intializeRound();
	int currentPlayer;
	bool roundOver;
	int playerScores[2];
	int playerThatWonRound;

	const int MAX_PLAYERS = 2;

	

};



#endif // !ROUND_H
