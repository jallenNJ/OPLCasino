#include "Tournament.h"


Tournament::Tournament() {
	scores[Round::HUMAN_PLAYER] = 0;
	scores[Round::COMPUTER_PLAYER] = 0;
}

Tournament::~Tournament() {
	for (int i = 0; i < allRounds.size();i++) {
		//delete allRounds[i];
	}
}

void Tournament::RunTournament() {

	allRounds.push_back(new Round());
	allRounds[0].playRound();
	
	int winner = -1;
	while (winner < 0) {
		//Sum Results here
		scores[Round::HUMAN_PLAYER] += allRounds.back().getPlayerScore(Round::HUMAN_PLAYER);
		scores[Round::COMPUTER_PLAYER] += allRounds.back().getPlayerScore(Round::COMPUTER_PLAYER);
		int humanScore = scores[Round::HUMAN_PLAYER];
		int compScore = scores[Round::COMPUTER_PLAYER];
		if (humanScore > POINTS_TO_WIN) {
			if (humanScore > compScore) {
				winner = Round::HUMAN_PLAYER;
				break;
			}
			else if (humanScore == compScore) {
				winner = TIE; //Make sure it isn't either of the enums
				break;
			}
			else {
				winner = Round::COMPUTER_PLAYER;
				break;
			}
		}
		else if (compScore > POINTS_TO_WIN) {
			winner = Round::COMPUTER_PLAYER;
			break;
		}


		//No winner, another round
		allRounds.push_back(new Round(allRounds.back().getWinner()));
		allRounds.back().playRound();

	}
}