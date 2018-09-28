#include "Serializer.h"

int Serializer::round;
Serializer::PlayerInfo Serializer::computerPlayer;
Serializer::PlayerInfo Serializer::humanPlayer;
string Serializer::table;
string Serializer::deck;
string Serializer::nextPlayer;
vector<string> Serializer::buildOwners;

Serializer::PlayerInfo Serializer::compterPlayerToSave;
Serializer::PlayerInfo Serializer::humanPlayerToSave;
int Serializer::roundToSave;
string Serializer::tableToSave;
string Serializer::deckToSave;
string Serializer::nextPlayerToSave;
vector<string> Serializer::buildOwnersToSave;

int Serializer::computerSaveScore;
int Serializer::humanSaveScore;


void Serializer::init() {
	round = 0;
	table = "";
	deck = "";
	nextPlayer = "";

	computerPlayer = PlayerInfo();
	humanPlayer = PlayerInfo();
	compterPlayerToSave = PlayerInfo();
	humanPlayerToSave = PlayerInfo();

	roundToSave = 0 ;
	tableToSave = "";
	deckToSave = "";
	nextPlayerToSave = "";
	computerSaveScore = 0;
	humanSaveScore = 0;

}



/* *********************************************************************
Function Name: loadInSaveFile
Purpose: To load in the data from a saveFile into static fields
Parameters:
			filePath, the path to the file to open
Return Value: bool, true if file loaded, false if it failed
Local Variables:
			saveFile, the IFstream to the file
			dataByType, the vector that contains all the data
Algorithm:
			1) Open the file
			2) Read in the data
			3) Parse each field with appropriate function
Assistance Received: none
********************************************************************* */
bool Serializer::loadInSaveFile(string filePath) {
	//Open the file and ensure it opened succesfully
	ifstream saveFile;
	saveFile.open(filePath);
	if (!saveFile) {
		Client::outputError("Failed to open file: " + filePath);
		return false;
	}

	//Read the first 9 lines of the file in three chunks due to file formatting
	vector<vector<string>> dataByType;
	dataByType.push_back(readNNonBlankLines(saveFile, 1));
	dataByType.push_back(readNNonBlankLines(saveFile, 4));
	dataByType.push_back(readNNonBlankLines(saveFile, 4));
	//Read the rest of the file in one line chunks
	while (!saveFile.eof()) {
		dataByType.push_back(readNNonBlankLines(saveFile, 1));
	}

	//Pull out what round it is
	round = stoi(parseLine(dataByType[0][0])[1]);

	//Parse the player info into the approraite structs
	computerPlayer = readPlayerInfo(dataByType[1]);
	humanPlayer = readPlayerInfo(dataByType[2]);
	
	//Seperate the cards into the correct reference vars and remove the header
	table = removeHeader(dataByType[3][0]);
	deck = removeHeader(dataByType[dataByType.size() - 2][0]);
	nextPlayer = removeHeader(dataByType.back()[0]);
	
	//Get all the build owners, which may be zero.
	for (unsigned int i = 4; i < dataByType.size() - 2; i++) {
		buildOwners.push_back(removeHeader(dataByType[i][0]));
	}

	//Close the file as it won't be read anymore
	saveFile.close();

	return true;
}

/* *********************************************************************
Function Name: readNNonBlankLines
Purpose: To load in a specifed amount of lines from the file, skipping over empty lines
Parameters:
			file, an ifstream passed by **REFERENCE**
			amountOfLines, an int to say how many lines to read 
Return Value: Constructor
Local Variables:
			cards, a vector<string> to handled the tokenized parameter
Algorithm:
			1) Read next line of file
			2) If only whitespace, go to Step 1
			3) Store line of file
			4) Increment counter
			5) If counter is less than target, go to Step 1
Assistance Received: Worked with Andrew Wild to get the idea of passing the ifStream
			by reference to a function to read target lines
********************************************************************* */
//Worked w/ AW
 vector<string> Serializer::readNNonBlankLines(ifstream& file, int amountOfLines) {
	vector<string> results;
	while (!file.eof() && results.size() < amountOfLines) {
		string line = "";
		getline(file, line);
		if (line == ""|| line.find_first_not_of(' ') == string::npos) {
			continue;
		}
		results.push_back(line);

	}
	return results;

}

 /* *********************************************************************
Function Name: parseLine
Purpose: To tokenize a line of text
Parameters:
			line, the string of text to tokenize
Return Value: Vector<string> of all string tokens
Local Variables:
			inputStream, which is the stringstream that splits by space
			label and buffer are string buffers to store the inputs
Algorithm:
			1) Get a token
			2) Store the token
			3) Return list of tokens
Assistance Received: none
********************************************************************* */
 vector<string> Serializer::parseLine(string line) {
	 vector<string> data;
	 while (true) {
		 istringstream inputStream(line);
		 string label = "";
		 inputStream >> label;
		 data.push_back(label);

		 while (true) {
			 string buffer = "";
			 inputStream >> buffer;
			 if (buffer.size() == 0) {
				 break;
			 }
			 data.push_back(buffer);
		 }
		 return data;
	 }
 }

 /* *********************************************************************
Function Name: readPlayerInfo
Purpose: To read all player lines related to a single player 
Parameters:
			data, all the data to parse
Return Value: PlayerInfo struct filled in with the data in
Local Variables:
			type a string containing thename
			score, the parsed score
			hand, the parsed hand
			pile, the parsed pile
Algorithm:
			1) Read each vector for each var
Assistance Received: none
********************************************************************* */
 Serializer::PlayerInfo Serializer::readPlayerInfo(vector<string> data) {

	 if (data.size() != 4) {
		 return PlayerInfo();
	 }
	 string type = data[0];
	 type = type.substr(0, type.length() - 1);
	 int score = stoi(parseLine(data[1])[1]);
	 string hand = removeHeader(data[2]);
	 string pile = removeHeader(data[3]);

	 return PlayerInfo(type, score, hand, pile);

 }

 /* *********************************************************************
Function Name: removeHeader
Purpose: To remove the header in the save file
Parameters:
			cards, the string to have the header removed from 
Return Value: string, with the header removed (1 character after the colon)
Local Variables:
			colongIndex, the indice with the first colon
Algorithm:
			1) Find the colon
			2) Remove to the character after it
Assistance Received: none
********************************************************************* */
 string Serializer::removeHeader(string cards) {
	 int colonIndex = cards.find(':');
	 //Todo, handle check with not found?(May just return the entire string)
	 return cards.substr(colonIndex + 1);
 }


 void Serializer::createSaveFile() {
	 string fileName = Client::getStringInput("Please enter save file name (No file extension):");
	 fileName += ".txt";
	 ofstream saveFile;
	 saveFile.open(fileName);
	 if (!saveFile.is_open()) {
		 Client::outputError("Failed to create file. Please check to ensure the file is not locked, and program has write access to directory");
		 return;
	 }

	 saveFile << "Round: " + to_string(roundToSave) + "\n\n";

	 saveFile << "Computer:\n   Score: " << to_string(compterPlayerToSave.score) <<
		 "\n   Hand: " << compterPlayerToSave.hand <<
		 "\n   Pile: " << compterPlayerToSave.pile <<
		 "\n\n";

	 saveFile << "Human:\n   Score: " << to_string(humanPlayerToSave.score) <<
		 "\n   Hand: " << humanPlayerToSave.hand <<
		 "\n   Pile: " << humanPlayerToSave.pile <<
		 "\n\n";

	 saveFile << "Table: " << tableToSave << "\n\n";

	 for (unsigned int i = 0; i < buildOwnersToSave.size(); i++) {
		 saveFile << buildOwnersToSave[i] << "\n\n";
	 }

	 saveFile << "Deck: "<< deckToSave << "\n\n";

	 saveFile << "Next Player: " << nextPlayerToSave;

	 saveFile.close();

 }

 string Serializer::loadPrebuiltDeck() {
	 string filePath;
	 ifstream deckFile;
	 while (true) {
		filePath = Client::getStringInput("Please enter the file path to the deck file: ");
		deckFile.open(filePath);
		if (deckFile.is_open()) {
			break;
		}
		Client::outputError("Failed to open file: " + filePath);

	 }
	 string deckToReturn = "";
	 while (!deckFile.eof()) {
		 string line = "";
		 getline(deckFile, line);
		 if (line == ""){
			 continue;
		 }
		 line.erase(remove_if(line.begin(), line.end(), isspace), line.end());
		 if (line.length() > 2) {
			 Client::outputError("Deck had token longer than a card, skipping.");
			 continue;
		 }
		 deckToReturn += line + " ";
	 }

	 deckFile.close();
	 return deckToReturn;


 }