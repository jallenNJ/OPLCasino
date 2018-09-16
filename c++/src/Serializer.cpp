#include "Serializer.h"

int Serializer::round;

void Serializer::init() {
	round = 0;
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
	}

	return true;
}