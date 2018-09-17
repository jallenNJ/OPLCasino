#ifndef SERIALIZER_H
#define SERIALIER_H
#include <string>
#include <fstream>
#include "Client.h"
#include <sstream>


using namespace std;
class Serializer {

	struct PlayerInfo {
		string name;
		int score;
		string hand;
		string pile;
		bool isValid;
		
		PlayerInfo() {
			name = "";
			score = 0;
			hand = "";
			pile = "";
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
	static string table;
	static string deck;
	static string nextPlayer;
	static int round;


	static vector<string> readNNonBlankLines(ifstream&, int);
	static vector<string> parseLine(string);
	static PlayerInfo readPlayerInfo(vector<string>);
	static inline string removeHeader(string);

	//string 

	
};




#endif // !SERIALIZER_H
