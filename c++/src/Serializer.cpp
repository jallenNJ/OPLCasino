#include "Serializer.h"

int Serializer::round;

void Serializer::init() {
	round = 0;

	fileHeadersToInt["Round"] = Round;
	fileHeadersToInt["Computer"] = Computer;
	fileHeadersToInt["Human"] = Human;
	fileHeadersToInt["Table"] = Table;
	fileHeadersToInt["BuildOwner"] = BuildOwner;
	fileHeadersToInt["Deck"] = Deck;
	fileHeadersToInt["Next"] = Next;


}


bool Serializer::loadInSaveFile(string filePath) {
	ifstream saveFile;
	saveFile.open(filePath);
	if (!saveFile) {
		Client::outputError("Failed to open file: " + filePath);
		return false;
	}
	
	string line = "";
	while (!saveFile.eof()) {
		getline(saveFile, line);
		if (line == "") {
			continue;
		}
	}
	vector<string>parsedLine = parseLine(line);
	SaveFileHeaders temp;
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
	}
	/*while (!saveFile.eof()) {
		getline(saveFile, line);
		if (line == "") {
			continue;
		}
		istringstream inputStream(line);
		string label = "";
		inputStream >> label;
		vector<string> data;
		while (true) {
			string buffer = "";
			inputStream >> buffer;
			if (buffer.size() == 0) {
				break;
			}
			data.push_back(buffer);
		}

		line = "";
	}*/

	return true;
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
	}
	return data;
}

Serializer::SaveFileHeaders Serializer::labelToSaveFileHeaders(string label) {
	map<string, Serializer::SaveFileHeaders>::iterator mapIterator;
	mapIterator = fileHeadersToInt.find(label);
	if (mapIterator ==fileHeadersToInt.end()) {
		return mapIterator->second;
	}
	return HangingData;
}