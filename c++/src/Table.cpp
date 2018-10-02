#include "Table.h"

Table::Table() {
	initTable();


}

Table::Table(bool humanStart) {
	initTable(humanStart);
}

Table::Table(bool humanStart, bool loadFromSave) {
	if (loadFromSave == false) {
		Table(humanStart);
		return;
	}
	initTable(humanStart, true);

}


/* *********************************************************************
Function Name: initTable 
Purpose: To init the table without a save file, helper function to the constructor
Parameters:
			bool humanStart, if the human player is starting
Return Value: void
Local Variables:
			Human * human, the human player being created
			Computer * computer, the computer player being created
Algorithm:
			1) If loading from a save:
			2) Create all players and tell them to intialize without a savefile
			3) Generate the deck
			4) Deal cards (Human, AI, Table)
			5) Intialize vars
Assistance Received: none
********************************************************************* */
void Table::initTable(bool humanStart) {
	Human* human = new Human();
	//Computer* human = new Computer();  //This lineis for debugging with computer
	Computer* computer = new Computer();
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

	deck = new Deck();
	fillHand(0);
	fillHand(1);
	fillLooseCards();
	humanFirst = humanStart;
	lastCapture = 0;
	playerScores[0] = 0;
	playerScores[1] = 0;
}


/* *********************************************************************
Function Name: initTable (Overload)
Purpose: To init the table from a save file, helper function to the constructor
Parameters:
			bool humanStart, if the human player is starting
			bool, loadFrom save, if to load from a save file
Return Value: void 
Local Variables:
			Human * human, the human player being created
			Computer * computer, the computer player being created
Algorithm:
			1) If loading from a save:
			2) Create all players and tell them to load from a save
			3) Generate the deck
			4) Deal cards (Human, AI, Table)
			5) Intialize vars
Assistance Received: none
********************************************************************* */
void Table::initTable(bool humanStart, bool loadFromSave) {
	if (loadFromSave == false) {
		initTable(humanStart);
		return;
	}

	Human* human = new Human(true);
	Computer* computer = new Computer(true);
	players = new Player*[2];
	players[0] = human;
	players[1] = computer;

	deck = new Deck();
	fillHand(0);
	fillHand(1);
	fillLooseCards();
	humanFirst = humanStart;
	lastCapture = 0;
	playerScores[0] = 0;
	playerScores[1] = 0;

}


/* *********************************************************************
Function Name: printBoard
Purpose: To format a string to print board, and then print it
Parameters:
			void
Return Value: void (calls Client to output)
Local Variables:
			names, an array of all the names
			padding, an int of how many spaces to pad
			formattedTable, the formatted table being built
Algorithm:
			1) Print the human
			2) Print the board
			3) Print the Computer
Assistance Received: none
********************************************************************* */
const void Table::printBoard() {
	//Get the names of all players
	string names[3];
	names[0] = players[0]->getName();
	names[1] = players[1]->getName();
	names[2] = "Table";

	//Find the amount of padding needed (longestName + 1)
	int padding = (int)max(names[0].length(), names[1].length());
	padding = max(padding, (int)names[2].length());

	//Apply padding
	for (int i = 0; i < 3; i++) {
		for (unsigned int j = padding; j > padding - names[i].size(); j--) {
			names[i] += " ";
		}
		names[i] += ": ";
	}

	//Create the string to be output
	string formattedTable = "\n" + names[0] + players[0]->toFormattedString() + 
							"\n" + names[2] + looseCards.toFormattedString() + 
							"\n" + names[1] + players[1]->toFormattedString() +
							"\n\n\n(Debug Data Dump:)\n"+ "Deck: " + deck->toString()+
							"\n Pile of " + names[0] + players[0]->pileToString()+
							"\n Pile of " + names[1]  + players[1]->pileToString()+
							"\n";
	Client::outputString(formattedTable);

}

Table::~Table() {
	delete deck;
	delete[] players;
}

/* *********************************************************************
Function Name: runCycle()
Purpose: Have both players play one card, and deal more cards if needed
Parameters:
			void
Return Value: bool, true if round is over, false if round can continue
Local Variables:
			none, member variables only
Algorithm:
			1) Print board
			2) First player does move
			3) Print board
			4) Second player does move
			5) If hands are empty, deal more cards
Assistance Received: none
********************************************************************* */
bool Table::runCycle() {
	//printBoard();
	if (nextPlayerIndex.size() == 0) {
		if (humanFirst) {
			nextPlayerIndex.push(0);
			nextPlayerIndex.push(1);
		}
		else {
			nextPlayerIndex.push(1);
			nextPlayerIndex.push(0);
		}
	}

	while (nextPlayerIndex.size() != 0) {
		printBoard();
		actionMenu();
		doPlayerMove(nextPlayerIndex.front());
		nextPlayerIndex.pop();
	}

	if (players[0]->getHandSize() == 0 && players[1]->getHandSize() == 0) {
		if (deck->isEmpty()) {
			captureRemaingCards();
			scoreRound();
			Client::outputString("Final state of board");
			printBoard();
			return true;
		}
		else {
			fillHand(0);
			fillHand(1);
		}
		
	}

	return false;
}


/* *********************************************************************
Function Name: doPlayerMove
Purpose: To have specifed player do their move
Parameters:
			playerIndex, the index to the player object in the array
Return Value: void (saves in member variables)
Local Variables:
			resultTuple, the results from the Player clase
			newBuild, if a new build is being made, it is in this object
Algorithm:
			1) Get each card seperately
			2) Create that into its own object
			3) Add to the member data object
Assistance Received: none
********************************************************************* */
void Table::doPlayerMove(int playerIndex) {
	
	//Have the player choose their move
	Player::PlayerMove resultTuple =  players[playerIndex]->doTurn(looseCards);
	Build newBuild(resultTuple.playedCard, players[playerIndex]->getName());

	//Make sure the information is sorted in descending order so the remove works
	sort(resultTuple.targetIndex.begin(), resultTuple.targetIndex.end());
	reverse(resultTuple.targetIndex.begin(), resultTuple.targetIndex.end());

	switch (resultTuple.actionTaken) {

		//If being caputred
	case Player::Actions::Capture:
		//Added the played card to the pile
		players[playerIndex]->addToPile(resultTuple.playedCard);
		
		//For every card that was caputed
		for (unsigned int i = 0; i < resultTuple.targetIndex.size(); i++) {
			//Get the reference to the removed card
			Card* removed = looseCards.removeCardAsReference(resultTuple.targetIndex[i]);

			//If its a build, normalize the name to remove issues with custome names
			if (removed->getSuit() == 'B') {
				string normalizedName = removed->getOwner();
				if (normalizedName != "Computer" && normalizedName != "Human") {
					if (normalizedName == players[0]->getName()) {
						normalizedName = "Human";
					}
					else {
						normalizedName = "Computer";
					}
				}

				//See if the person that is capturing matches the build owner name
				if ((normalizedName == "Computer" && playerIndex == 0) || normalizedName == "Human" && playerIndex == 1) {
					//If they don't match, release the build in the other player, as the capture took their build
					if (playerIndex == 1) {
						players[0]->releaseBuildValue(removed->getNumericValue());
					}
					else {
						players[1]->releaseBuildValue(removed->getNumericValue());
					}
				}
			}

			//Add to pile
			if (removed->getSuit() == 'B') {
				//If its a build, get all the cards individually
				players[playerIndex]->addToPile( dynamic_cast<Build *>(removed)->getCardsInBuild());
			}
			else {
				players[playerIndex]->addToPile(*removed);
			}
			
			//Delete the recieved card
			delete removed;
		}
		//Record who captured
		lastCapture = playerIndex;
		break;


		//If making a build
	case Player::Actions::Build:
		
		//For all cards being selected for a build.
		for (unsigned int i = 0; i < resultTuple.targetIndex.size(); i++) {
			Card* removed = looseCards.removeCardAsReference(resultTuple.targetIndex[i]);

			//If its a build, normalize the name to remove issues with custome names
			if (removed->getSuit() == 'B') {
				string normalizedName = removed->getOwner();
				if (normalizedName != "Computer" && normalizedName != "Human") {
					if (normalizedName == players[0]->getName()) {
						normalizedName = "Human";
					}
					else {
						normalizedName = "Computer";
					}
				}

				//See if the person that is capturing matches the build owner name
				if ((normalizedName == "Computer" && playerIndex == 0) || normalizedName == "Human" && playerIndex == 1) {
					//If they don't match, release the build in the other player, as the capture took their build
					if (playerIndex == 1) {
						players[0]->releaseBuildValue(removed->getNumericValue());
					}
					else {
						players[1]->releaseBuildValue(removed->getNumericValue());
					}
				}
			}
			//Add to build and delete the copy
			newBuild.addCardToBuild(*removed);
			delete removed;
		}

		//Add the build to the table and reserve the new value
		looseCards.addCard(newBuild);
		players[playerIndex]->reserveCardValue(newBuild.getNumericValue());


		break;
	case Player::Actions::Trail:

		//Add the card to the table
		looseCards.addCard(resultTuple.playedCard);
		break;
	default:
		Client::outputError("Invalid sanitized input");
		break;
	}
	
}


/* *********************************************************************
Function Name: fillLooseCards
Purpose: To deal cards to the table
Parameters:
			non
Return Value: void (saves in member variables)
Local Variables:
			string savedTable, the saved table string
			vector<string>tokens, the tokens of the above string
			string token, an indivual token
			char cardSuit, the suit of the card
			char cardSymbol, the symbol of the card
			PlayingCard adding, the card to add
Algorithm:
			1) Check if there is a save file to load in
			2) If there is, load in the data,
				If there are builds, construct them via tracking in a stack
			3) If no save file, deal four cards from the deck.

Assistance Received: none
********************************************************************* */
void Table::fillLooseCards(){
	//If there is any save data, retrieve it
	string savedTable = Serializer::getTableCards();
	if (savedTable.length() > 0) {
		//Split into tokens and loop for each token
		vector<string> tokens = Serializer::parseLine(savedTable);
		for (unsigned int i = 0; i < tokens.size(); i++) {
			string token = tokens[i];
			//If the start of a build, create a new build on the stack
			if (token[0] == '[') {
				buildsInProgress.push_back(Build());
				//If nothing else in the token, skip
				if (token.length() == 1) {
					continue;
				}
				//Remove the '[' and continue to card processing
				token = token.substr(1);

			}
			//If the end of a build with no card, pop the build and process it
			else if (token[0] == ']') {
				processPoppedBuild(buildsInProgress);
			
				continue;

			}
			//Get the details for the cards
			char cardSuit = token[0];
			char cardSymbol = token[1];

			//If a build in in progress
			if (buildsInProgress.size() > 0) {
				//Add the card to the build
				PlayingCard adding(cardSuit, cardSymbol);
				buildsInProgress.back().addCardToBuild(adding);

				//If there is a closing ']', pop the build
				if (token.length() > 2) {
					if (token[2] == ']') {
						processPoppedBuild(buildsInProgress);

					}
				}
			}
			else {
				//Add the card not in the build
				looseCards.addCard(PlayingCard(cardSuit, cardSymbol));
			}

		}
		return;
	}

	//If no save file, deal four cards
	for (int i = 0; i < 4; i++) {
		if (deck->isEmpty()) {
			return;
		}
		looseCards.addCard(deck->drawCard());
	}
}

/* *********************************************************************
Function Name: processPoppedBuild
Purpose: Helper function to handle builds when they need to be removed from the stack
Parameters:
			vector<Build>& buildsInProgress
Return Value: void (saves in member variables/ pass by reference)
Local Variables:
			int amountOfBuilds, a local cache for readabilty
			string buildString, the build in string form
			int playerID, the ID of the player
Algorithm:
			1) Get all the builds with owners from the file
			2) If the build is the last on the stack
				Assign it to the player which owns it
				Add to table
			3) If a sub build being removed, add it to its parent build
			4) Remove build from stack
Assistance Received: none
********************************************************************* */
void Table::processPoppedBuild(vector<Build>& buildsInProgress) {
	//Get the list of build owners from the Serializer, and record the amount of builds currently being processed
	vector<string> buildsWithOwners = Serializer::getBuildOwners();
	int amountOfBuilds = buildsInProgress.size();

	if (amountOfBuilds == 1) { //If only one

		string buildString = buildsInProgress.back().toString();
		//Remove all white space from the string, then erase what is left over. 
		buildString.erase(remove_if(buildString.begin(), buildString.end(), isspace), buildString.end());
		for (unsigned int i = 0; i < buildsWithOwners.size(); i++) {
			int buildI = buildsWithOwners[i].find_last_of(']');
			//Increment to contain the bracket as we want amount of characters, not index
			buildI++;
			string buildText = buildsWithOwners[i].substr(0, buildI);
			//Remove the white space in the same way
			buildText.erase(remove_if(buildText.begin(), buildText.end(), isspace), buildText.end());

			//If the builds match
			if (buildText == buildString) {
				//Set the owner
				buildsInProgress.back().setOwner(buildsWithOwners[i].substr(buildI + 1));

				//Match the player id to the string for names
				int playerID = 0;
				if (buildsInProgress.back().getOwner() != "Human" && buildsInProgress.back().getOwner() != players[0]->getName()) {
					playerID = 1;
				}
				players[playerID]->reserveCardValue(buildsInProgress.back().getNumericValue());
				break;
			}
		}
		//Add itto the table
		looseCards.addCard(buildsInProgress.back());

	}
	else {
		//Add the build to the build
		buildsInProgress[amountOfBuilds - 2].addCardToBuild(buildsInProgress.back());


	}
	//Remove the build from the stack
	buildsInProgress.pop_back();
}


/* *********************************************************************
Function Name: actionMenu
Purpose: To display the menu for the user to choose what to do
Parameters:
			none
Return Value: void calls other functions
Local Variables:
			int option, the choosen menu options
			string menu, the menu text to be prompted
Algorithm:
			1) Display menu
			2) Get user input
			3) If Save game: Serialize objects, write to file, and save (with helper functions)
			4) If Do move: Have the correct player execute move
			5) If ask for help
				Create another AI with a copy of the human
				Have them generate a move
				Format it to be displayed
			6) If quiet the game, exit(0)
Assistance Received: none
********************************************************************* */
void Table::actionMenu() {
	int option = 0;
	//Generate the menu
	string menu = "1) Save the Game\n";
	//If the next turn is the player
	if (nextPlayerIndex.front() == 0) {
		menu += "2) Make a Move (Human)\n";

		menu += "3) Ask for help\n";
		menu += "4) Quit the Game\n";
		option = Client::getIntInputRange(menu, 1, 4);
		

	}
	else { //If the next turn is the AI
		menu += "2) Make a Move (Computer)\n";

		menu += "3) Quit the Game\n";
		option = Client::getIntInputRange(menu, 1, 3);
		//If 3, increment to 4 for close the game
		if (option == 3) {
			option++;
		}

	}



	switch (option) {
	case 1: //Save the game
		serilizeAllObjects();
		Serializer::createSaveFile();
		exit(0);
		return;
	case 2: //Do player move
		return;
	case 3: //Ask for help (Human Only)

		if (nextPlayerIndex.front() == 1) {
			Client::outputString("The ai does not need to recommend a move to itself, instead it will make the move");
			return;
		}
		createSuggestedMove();
		return;

	case 4: //Quit
		Client::outputString("Thanks for playing!");
		exit(0);
	default:
		Client::outputError("Invalid sanatized input, assuming make a move");
		return;
	}
	
}


/* *********************************************************************
Function Name: serilizeAllObjects
Purpose: To have all objects the table manages to save themselves
Parameters:
			none
Return Value: void (saves in Serializer object)
Local Variables:
			Card current, Cache of currently processed card
Algorithm:
			1) Call the AI to save itself
			2) Call the human to save itself
			3) Call the deck to save itself
			4) Call the table to save it self
			5) Find the next player and format string to be saved
			6) Save all builds 
Assistance Received: none
********************************************************************* */
void Table::serilizeAllObjects() {
	//Call every object which can save itself
	Serializer::setCompterPlayerSaveState(players[1]->saveSelf());
	Serializer::setHumanPlayerSaveState(players[0]->saveSelf());
	Serializer::setDeckSaveState(deck->toString());
	Serializer::setTableSaveState(looseCards.toString());

	//Format the string for next player and save it
	if (nextPlayerIndex.front() == 0) {
		Serializer::setNextPlayerSaveState("Human");
	}
	else {
		Serializer::setNextPlayerSaveState("Computer");
	}
	
	//For every build, call its to string and append its owner
	for (unsigned int i = 0; i < looseCards.handSize(); i++) {
		Card current = looseCards.getCardCopy(i);
		if (current.getSuit() == 'B') {
			string saveOwnerName = "";
			//Check who the owner is
			if (current.getOwner() == players[0]->getName() || current.getOwner() == "Human") {
				saveOwnerName = "Human";
			}
			else {
				saveOwnerName = "Computer";
			}
			//Log it to be saved
			Serializer::addBuildToSave(looseCards.toStringOfIndex(i), saveOwnerName);
		}
	}

}


/* *********************************************************************
Function Name: createSuggestedMove
Purpose: To have the AI recommend the move to a palyer
Parameters:
			pnone
Return Value: void (outputs to console)
Local Variables:
			Player* advisor, the Computer giving the recommendation
			Playermove recommendation, the recommendation being made
			string outputString, the message to be printed
Algorithm:
			1) Get each card seperately
			2) Create that into its own object
			3) Add to the member data object
Assistance Received: none
********************************************************************* */
void Table::createSuggestedMove() {
	//Generate the advisor and have it fine the move
	Player* advisor = new Computer(*dynamic_cast<Human*>(players[0]));
	Player::PlayerMove recommendation = advisor->doTurn(looseCards);
	//Build the string
	string outputString = "The AI recommends that you ";
	switch (recommendation.actionTaken)
	{
		
		case Player::Actions::Capture:
			outputString += " capture with the card " + recommendation.playedCard.toString() +
				"\n to capture these cards:";
			//Add every card it suggests to capture
			for (unsigned int i = 0; i < recommendation.targetIndex.size(); i++) {
				outputString += looseCards.getCardCopy(recommendation.targetIndex[i]).toString() + " ";
			}
			outputString += "\n";
			break;
		case Player::Actions::Build:
			//Add every card it suggests to build with
			outputString += " create a build with the card " + recommendation.playedCard.toString() +
				"\n and use these cards:";
			for (unsigned int i = 0; i < recommendation.targetIndex.size(); i++) {
				outputString += looseCards.getCardCopy(recommendation.targetIndex[i]).toString() + " ";
			}
			outputString += "\n";
			break;
		case Player::Actions::Trail:
			outputString += " trail with this card " + recommendation.playedCard.toString() + " as there are no other options\n";

			break;
		default:
			Client::outputError("AI recommendation returned an invalid enum");
			break;
	}

	//Output the string and remove the advsior
	Client::outputString(outputString);

	delete advisor;
}

/* *********************************************************************
Function Name: captureRemaingCards
Purpose: To have all remaining cards on the table be captured by the last player
Parameters:
			none
Return Value: void (saves in member variables)
Local Variables:
			none
Algorithm:
			1) For every card on table
				2) Add it to the pile of the player
Assistance Received: none
********************************************************************* */
void Table::captureRemaingCards() {

	for ( int i = looseCards.handSize() -1; i >=0; i--) {
		players[lastCapture]->addToPile(looseCards.getCardCopy(i));
		looseCards.removeCard(i);

	}


}


/* *********************************************************************
Function Name: scoreRound()
Purpose: To tally the score for the round
Parameters:
			none
Return Value: void (saves in member variables)
Local Variables:
			int humanCards, the pile size of the human
			int compCards, the pile size of the computer
			int humanSpades, the amount of spades the human has
			int compSpades, the amount of spades the computer has
Algorithm:
			1) For every card on table
				2) Add it to the pile of the player
Assistance Received: none
********************************************************************* */
void Table::scoreRound() {

	//Find out which player has the most cards
	int humanCards = players[0]->getPileSize();
	int compCards = players[1]->getPileSize();
	Client::outputString(players[0]->getName() + " has " + to_string(humanCards) + " in their pile, and " + players[1]->getName() + " has " + to_string(compCards) + " in their pile");

	//Give three points to the player that does, no points in a tie
	if (humanCards > compCards) {
		playerScores[0] += 3;
	}
	else if (humanCards < compCards) {
		playerScores[1] += 3;
	}

	//Find out who has the most spades
	int humanSpades = players[0]->getAmountOfSpadesInPile();
	int compSpades = players[1]->getAmountOfSpadesInPile();

	Client::outputString(players[0]->getName() + " has " + to_string(humanSpades) + " spades in their pile, and " + players[1]->getName() + " has " + to_string(compSpades) + " spades in their pile");
	
	//Add one point to who has the most, no points if tie
	if (humanSpades > compSpades) {
		playerScores[0]++;
	}
	else if (humanSpades < compSpades) {
		playerScores[1]++;
	}
	
	//Give two points to the player that has the 10 of diamonds
	if (players[0]->containsCardInPile('D', 'X')) {
		playerScores[0] += 2;
	}
	else if (players[1]->containsCardInPile('D', 'X')) {
		playerScores[1] += 2;
	}

	//Give one point to the player that has the 2 of spades
	if (players[0]->containsCardInPile('S', '2')) {
		playerScores[0] += 1;
	}
	else if (players[1]->containsCardInPile('S', '2')) {
		playerScores[1] += 1;
	}

	//Each player scores one point per ace in their pile
	playerScores[0] += players[0]->getAmountOfSymbolInPile('A');
	playerScores[1] += players[1]->getAmountOfSymbolInPile('A');

}