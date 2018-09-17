#include "Serializer.h"

int Serializer::round;
Serializer::PlayerInfo Serializer::computerPlayer;
Serializer::PlayerInfo Serializer::humanPlayer;
string Serializer::table;
string Serializer::deck;
string Serializer::nextPlayer;

void Serializer::init() {
	round = 0;
	table = "";
	deck = "";
	nextPlayer = "";

	/*fileHeadersToInt["Round"] = Round;
	fileHeadersToInt["Computer"] = Computer;
	fileHeadersToInt["Human"] = Human;
	fileHeadersToInt["Table"] = Table;
	fileHeadersToInt["BuildOwner"] = BuildOwner;
	fileHeadersToInt["Deck"] = Deck;
	fileHeadersToInt["Next"] = Next;*/


}


bool Serializer::loadInSaveFile(string filePath) {
	ifstream saveFile;
	saveFile.open(filePath);
	if (!saveFile) {
		Client::outputError("Failed to open file: " + filePath);
		return false;
	}

	vector<vector<string>> dataByType;
	dataByType.push_back(readNNonBlankLines(saveFile, 1));
	dataByType.push_back(readNNonBlankLines(saveFile, 4));
	dataByType.push_back(readNNonBlankLines(saveFile, 4));
	while (!saveFile.eof()) {
		dataByType.push_back(readNNonBlankLines(saveFile, 1));
	}

	round = stoi(parseLine(dataByType[0][0])[1]);
	computerPlayer = readPlayerInfo(dataByType[1]);
	humanPlayer = readPlayerInfo(dataByType[2]);
	table = removeHeader(dataByType[3][0]);
	deck = removeHeader(dataByType[dataByType.size() - 2][0]);
	nextPlayer = removeHeader(dataByType.back()[0]);




/*
	switch (labelToSaveFileHeaders(parsedLine[0]))
	{
	case Serializer::Round:
		round = stoi(parsedLine[1]);
		break;
	case Serializer::Computer:
		break;
	case Serializer::Human:
		break;
	case Serializer::Table:
		break;
	case Serializer::BuildOwner:
		break;
	case Serializer::Deck:
		break;
	case Serializer::Next:
		break;
	case Serializer::HangingData:
	default:
		break;
	}*/

	return true;
}

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

 string Serializer::removeHeader(string cards) {
	 int colonIndex = cards.find(':');
	 return cards.substr(colonIndex + 1);
 }

/*

Serializer::SaveFileHeaders Serializer::labelToSaveFileHeaders(string label) {
	map<string, Serializer::SaveFileHeaders>::iterator mapIterator;
	mapIterator = fileHeadersToInt.find(label);
	if (mapIterator ==fileHeadersToInt.end()) {
		return mapIterator->second;
	}
	return HangingData;
}*/