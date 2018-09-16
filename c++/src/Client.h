#ifndef  CLIENT_H
#define CLIENT_H
#include <iostream>
#include <string>
#include <vector>


using namespace std;
class Client {
public:

	static string getStringInput(string);
	static char getCharInput(string, vector<char>, bool = true);
	static inline void outputString(string message) {
		cout << message << endl;
	}

	static inline void outputError(string errorMsg) {
		cerr << errorMsg << endl;
	}

	static int getIntInputRange(string, int, int);

	static char getYesNoInput(string prompt, bool forceLower = true) {
		vector<char> yesNo;
		yesNo.push_back('y');
		yesNo.push_back('n');
		return getCharInput(prompt, yesNo, forceLower);
	}

private:

	static inline void clearSTDIn() {
		cin.clear();
		cin.ignore(cin.rdbuf()->in_avail(), '\n');
	}


};




#endif // ! CLIENT_H
