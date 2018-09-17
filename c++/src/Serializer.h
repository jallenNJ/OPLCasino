#ifndef SERIALIZER_H
#define SERIALIER_H
#include <string>
#include <fstream>
#include "Client.h"
#include <sstream>
#include <map>


using namespace std;
class Serializer {

	struct PlayerInfo {
		string name;
		int score;
		string hand;
		string pile;
		bool isValid;
		
		PlayerInfo() {
			name = string();
			score = int();
			hand = string();
			pile = string();
			isValid = false;
		}

		PlayerInfo(string n, int s, string h, string p) {
			name = n;
			score = s;
			hand = h;
			pile = p;
			isValid = true;
		}


	};
	
public:

	static bool loadInSaveFile(string);
	static string inline getSaveFilePath() {
		return Client::getStringInput("Please input save file path.");
	}

	static void init();

protected:




private:
	static PlayerInfo computerPlayer;
	static PlayerInfo humanPlayer;
	enum SaveFileHeaders { Round, Computer, Human, Table, BuildOwner, Deck, Next, HangingData };
	//static map<string, SaveFileHeaders> fileHeadersToInt;

	//static SaveFileHeaders labelToSaveFileHeaders(string);
	static int round;
	static vector<string> readNNonBlankLines(ifstream&, int);
	static vector<string> parseLine(string);
	static PlayerInfo readPlayerInfo(vector<string>);
	static inline string removeHeader(string);

	//string 

	
};




#endif // !SERIALIZER_H
