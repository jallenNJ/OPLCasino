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

	/* *********************************************************************
Function Name: getYesNoInput
Purpose: Warpper function to getCharInfo which provides (y/n) as valid answers
Parameters:
			prompt, the string to which to prompt the user
			forceLower, a flag to force to lowercase
Return Value: Char that is either (y/n) 
Local Variables:
			temp[], an integer array used to sort the grades
Algorithm:
			1) Add all the grades
			2) Divide the sum by the number of students in class to calculate the average
Assistance Received: none
********************************************************************* */
	static char getYesNoInput(string prompt) {
		vector<char> yesNo;
		yesNo.push_back('y');
		yesNo.push_back('n');
		return getCharInput(prompt, yesNo, true);
	}

private:

	static inline void clearSTDIn() {
		cin.clear();
		cin.ignore(cin.rdbuf()->in_avail(), '\n');
	}


};




#endif // ! CLIENT_H
