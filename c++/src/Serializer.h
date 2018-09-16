#ifndef SERIALIZER_H
#define SERIALIER_H
#include <string>
#include <fstream>
#include "Client.h"
#include <sstream>


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


	static int round;
	//string 


};




#endif // !SERIALIZER_H
