#include "Tournament.h"


Tournament::Tournament() {
	scores[Round::HUMAN_PLAYER] = 0;
	scores[Round::COMPUTER_PLAYER] = 0;
	roundNumber = 0;
	Serializer::init();
}

Tournament::~Tournament() {
	for (unsigned int i = 0; i < allRounds.size();i++) {
		//delete allRounds[i];
	}
}

/* *********************************************************************
Function Name: RunTournament()
Purpose: To run the tournament to target points or user exits
Parameters:
			startingCards, a string for all the cards to be put into the deck
Return Value: void
Local Variables:
			loaded, a flag for if a save file was loaded
			save/first Where the first round is stored
			

Algorithm:
			1) See if the user wants to load a save
			2) Create the first round / restore the save
			3) Create new rounds until score is reached
Assistance Received: none
********************************************************************* */
void Tournament::RunTournament() {
	bool loaded = false;
	//Check if user wants to load save
	Round* active = NULL;
	Round* previous = NULL;
	while (true){
		if (checkForSaveFileLoad()) {
			 loaded = Serializer::loadInSaveFile(Serializer::getSaveFilePath());
		}
		else {
			break;
		}
		//If failed to load
		if (loaded == false) {
			if (Client::getYesNoInput("Would you like to try another file (y/n)?") == 'n') {
				break;
			}
		} else{
			//Load in the data
			scores[Round::HUMAN_PLAYER] = Serializer::getHumanScore();
			scores[Round::COMPUTER_PLAYER] = Serializer::getComputerScore();
			roundNumber = Serializer::getRound();
			Serializer::setSaveScore(scores[Round::HUMAN_PLAYER], scores[Round::COMPUTER_PLAYER]);
			Serializer::setRoundToSave(roundNumber);

			active = new Round(Serializer::nextPlayerIsHuman(), true);
	
			break;
		}

	}

	if (!loaded) {
		active = new Round();
	
		
	}
	active->playRound();
	
	
	int winner = -1;
	while (winner < 0) {
		if (previous != NULL) {
			delete previous;
		}
		previous = active;
		//Sum Results here
		scores[Round::HUMAN_PLAYER] += previous->getPlayerScore(Round::HUMAN_PLAYER);
		scores[Round::COMPUTER_PLAYER] += previous->getPlayerScore(Round::COMPUTER_PLAYER);
		Serializer::setSaveScore(scores[Round::HUMAN_PLAYER], scores[Round::COMPUTER_PLAYER]);
		Serializer::setRoundToSave(roundNumber);
		winner = checkForWinner();

		if (winner >= 0) {
			break;
		}


		Client::outputString("Tournament Scores: Human: " + to_string(scores[Round::HUMAN_PLAYER]) + "   Computer: " + to_string(scores[Round::COMPUTER_PLAYER]));

		//No winner, another round
		active = new Round(previous->getPlayerThatCapturedLast());

	}

	switch (winner)
	{
	case Round::HUMAN_PLAYER:
		Client::outputString("The human has won the tournament!");
		break;
	case Round::COMPUTER_PLAYER:
		Client::outputString("The computer has wonthe tournament!");
		break;
	case 2:
		Client::outputString("The result of the tournament is a tie!");
		break;
	default:
		Client::outputError("Invalid enum in winner, view scores below to check");
		break;
	}

	Client::outputString("Tournament Scores: Human: " + to_string( scores[Round::HUMAN_PLAYER]) + "   Computer: " + to_string(scores[Round::COMPUTER_PLAYER]));

}

bool Tournament::checkForSaveFileLoad() {
	char result = Client::getYesNoInput("Would you like to load in a save file (y/n)?");
	if (result == 'y') {
		return true;
	}
	return false;

}


/* *********************************************************************
Function Name: checkForWinner()
Purpose: Check if someone won and who
Parameters:
			void
Return Value: int the winner
Local Variables:
			humanScore and compScore, caches of the member variable for code readability
Algorithm:
			1) Check to see if one has more than the threshold
			2) See if they are tied
			3) If not, return the greater
Assistance Received: none
********************************************************************* */
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