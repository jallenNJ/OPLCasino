#include "Client.h"

string Client::getStringInput(string prompt) {
	cout << prompt;
	string userInput = "";
	getline(cin, userInput);
	clearSTDIn();
	return userInput;

}


char Client::getCharInput(string prompt, vector<char> validAnswers, bool forceLowerCase) {
	char input = '0';
	if (forceLowerCase) {
		for (unsigned int i = 0; i < validAnswers.size(); i++) {
			validAnswers[i] = tolower(validAnswers[i]);
		}
	}
	while (true) {
		cout << prompt;
		cin >> input;
		clearSTDIn();
		input = tolower(input);
		for (unsigned int i = 0; i < validAnswers.size(); i++) {
			if (input == validAnswers[i]) {
				return input;
			}
		}
	}
}

int Client::getIntInputRange(string prompt, int lowerBound, int upperBound) {
	if (upperBound < lowerBound) {
		int swap = lowerBound;
		lowerBound = upperBound;
		upperBound = swap;
	}
	string userInput = "";
	int input = lowerBound - 1;
	while (true) {
		userInput = "";
		cout << prompt;
		getline(cin, userInput);
		clearSTDIn();
		try {
			input = stoi(userInput);
		}
		catch(exception e){
			cout << "NaN. Please enter a number between " << lowerBound << " and " << upperBound << endl;
			continue;
		}
		if (input >= lowerBound && input <= upperBound) {
			return input;
		}

	}
}