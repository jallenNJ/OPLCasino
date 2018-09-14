#include "Tournament.h"


Tournament::Tournament() {
	scores[Round::HUMAN_PLAYER] = 0;
	scores[Round::COMPUTER_PLAYER] = 0;
}

Tournament::~Tournament() {
	for (unsigned int i = 0; i < allRounds.size();i++) {
		//delete allRounds[i];
	}
}

void Tournament::RunTournament() {
	Round first;
	first.playRound();
	//allRounds.push_back(new Round());
	//allRounds[0].playRound();
	
	int winner = -1;
	while (winner < 0) {
		//Sum Results here
		scores[Round::HUMAN_PLAYER] += allRounds.back().getPlayerScore(Round::HUMAN_PLAYER);
		scores[Round::COMPUTER_PLAYER] += allRounds.back().getPlayerScore(Round::COMPUTER_PLAYER);
		winner = checkForWinner();

		//No winner, another round
		allRounds.push_back(new Round(allRounds.back().getWinner()));
		allRounds.back().playRound();

	}

	cout << winner << " has won" << endl;
}

int Tournament::checkForWinner() {
	int humanScore = scores[Round::HUMAN_PLAYER];
	int compScore = scores[Round::COMPUTER_PLAYER];
	if (humanScore > POINTS_TO_WIN) {
		if (humanScore > compScore) {
			return Round::HUMAN_PLAYER;
			
		}
		else if (humanScore == compScore) {
			return TIE;
		}
		else {
			return Round::COMPUTER_PLAYER;

		}
	}
	else if (compScore > POINTS_TO_WIN) {
		return Round::COMPUTER_PLAYER;
		
	}
	else {
		return -1;
	}
}