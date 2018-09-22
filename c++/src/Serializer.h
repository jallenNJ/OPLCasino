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

	static void createSaveFile();

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

	static string getTableCards() {
		return table;
	}


	static vector<string> parseLine(string);

	static PlayerInfo getHumanPlayerInfo() {
		return humanPlayer;
	}

	static bool nextPlayerIsHuman() {
		return nextPlayer == "Human";
	}


	static bool setCompterPlayerSaveState(string name, int currentScore, string currentHand, string currentPile) {
		compterPlayerToSave = PlayerInfo(name, currentScore, currentHand, currentPile);

	}

	static bool setHumanPlayerSaveState(string name, int currentScore, string currentHand, string currentPile) {
		humanPlayerToSave = PlayerInfo(name, currentScore, currentHand, currentPile);

	}

	static bool setTableSaveState(string t) {
		tableToSave = t;
		return true;
	}
	static bool setDeckSaveState(string d) {
		deckToSave = d;
		return true;
	}
	static bool setNextPlayerSaveState(string n) {
		nextPlayer = n;
		return true;
	}

private:
	static PlayerInfo computerPlayer;
	static PlayerInfo humanPlayer;
	static PlayerInfo compterPlayerToSave;
	static PlayerInfo humanPlayerToSave;
	static string table;
	static string tableToSave;
	static string deck;
	static string deckToSave;
	static string nextPlayer;
	static string nextPlayerToSave;
	static int round;
	static int roundToSave;
	static vector<string>buildOwners;

	static vector<string> readNNonBlankLines(ifstream&, int);
	
	static PlayerInfo readPlayerInfo(vector<string>);
	static inline string removeHeader(string);


	
};




#endif // !SERIALIZER_H
