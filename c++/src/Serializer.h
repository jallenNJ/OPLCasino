#ifndef SERIALIZER_H
#define SERIALIER_H
#include <string>
#include <fstream>
#include "Client.h"
#include <sstream>
#include <map>


using namespace std;
class Serializer {
	
public:

	static bool loadInSaveFile(string);
	static string inline getSaveFilePath() {
		return Client::getStringInput("Please input save file path.");
	}

	static void init();

protected:




private:
	enum SaveFileHeaders { Round, Computer, Human, Table, BuildOwner, Deck, Next, HangingData };
	//static map<string, SaveFileHeaders> fileHeadersToInt;

	//static SaveFileHeaders labelToSaveFileHeaders(string);
	static int round;
	static vector<string> readNNonBlankLines(ifstream&, int);
	static vector<string> parseLine(string);
	//string 

	
};




#endif // !SERIALIZER_H
