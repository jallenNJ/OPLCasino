#ifndef SERIALIZER_H
#define SERIALIZER_H
#include <string>
#include <fstream>
#include "Client.h"
#include <sstream>


using namespace std;
class Serializer {

public:
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
	


	static bool loadInSaveFile(string);
	static string inline getSaveFilePath() {
		return Client::getStringInput("Please input save file path.");
	}

	static void init();

	static int getHumanScore() {

		return humanPlayer.score;
	}
	static int getComputerScore() {

		return computerPlayer.score;
	}

	static string getDeck() {
		return deck;
	}


	static vector<string> parseLine(string);

	static PlayerInfo getHumanPlayerInfo() {
		return humanPlayer;
	}

	static bool nextPlayerIsHuman() {
		return nextPlayer == "Human";
	}

private:
	static PlayerInfo computerPlayer;
	static PlayerInfo humanPlayer;
	static string table;
	static string deck;
	static string nextPlayer;
	static int round;
	static vector<string>buildOwners;

	static vector<string> readNNonBlankLines(ifstream&, int);
	
	static PlayerInfo readPlayerInfo(vector<string>);
	static inline string removeHeader(string);


	
};




#endif // !SERIALIZER_H
