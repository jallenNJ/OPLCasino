#include "Client.h"


/* *********************************************************************
Function Name: getStringInput
Purpose: To prompt the user for string input and return its value
Parameters:
			prompt, A string which will be the output to the console
Return Value: The raw user input up to the first '\n'
Local Variables:
			userInput, the buffer to put the input into
Algorithm:
			1) Output the prompt
			2) Retrieve the user input
			3) Clean out the input stream
Assistance Received: none
********************************************************************* */
string Client::getStringInput(string prompt) {
	cout << prompt;
	string userInput = "";
	getline(cin, userInput);
	clearSTDIn();
	return userInput;

}

/* *********************************************************************
Function Name: getCharInput
Purpose: Get character input from the user and check to ensure its already valid
Parameters:
			prompt, a string which will be outputed to the user
			validAnswers, a vector<char> which are all the accepted answers that can be returned
			forceLowerCase, a bool that defaults true that forces all input to lower case
Return Value: User input that matches one of the valid answers
Local Variables:
			char input. The buffer to store the user input
Algorithm:
			1) Prompt user for input
			2) Get the input
			3) Check if its valid, if its not go to Step 1
			4) Force to lowercase if set in parameters
			5) Return input
Assistance Received: none
********************************************************************* */
char Client::getCharInput(string prompt, vector<char> validAnswers, bool forceLowerCase) {
	char input = '0';
	//If forcing to lowercase, ensuring the valid answers are lower case
	if (forceLowerCase) {
		for (unsigned int i = 0; i < validAnswers.size(); i++) {
			validAnswers[i] = tolower(validAnswers[i]);
		}
	}
	//Loop until appropriate answer
	while (true) {
		//Get the input and clear the rest of the buffer
		cout << prompt;
		cin >> input;
		clearSTDIn();
		//Force to lowercase if set
		if (forceLowerCase) {
			input = tolower(input);
		}
		//Check if input matches valid answers, if it does, then return
		for (unsigned int i = 0; i < validAnswers.size(); i++) {
			if (input == validAnswers[i]) {
				return input;
			}
		}
	}
}


/* *********************************************************************
Function Name: getIntInputRange
Purpose: To get user input of an int within a continous range
Parameters:
			promp, the string to be presented to the user
			lowerBound, an int which represents the lowerst value to accept, inclusive
			upperBound, an int which represents the highest value to accept, inclusive
Return Value: and int on the range [lowerBound, upperBound] 
Local Variables:
			swap. Used to swap the bounds if needed
			userInput, the buffer for the userInput
			input, the integer buffer to store the data in
Algorithm:
			1) Make sure bounds are logical
			2) Prompt user for input
			3) Recieve user input
			4) If its a number, check if its within the range. If either condition is false, go to step 2
			5) Return number
Assistance Received: none
********************************************************************* */
int Client::getIntInputRange(string prompt, int lowerBound, int upperBound) {
	//If bounds provided in the wrong order, swap them
	if (upperBound < lowerBound) {
		int swap = lowerBound;
		lowerBound = upperBound;
		upperBound = swap;
	}
	//Intialzie loop vars
	string userInput = "";
	int input = lowerBound - 1;

	//Forever loop, returns the value once it is found
	while (true) {
		//Get user input and clear the buffer
		userInput = "";
		cout << prompt;
		getline(cin, userInput);
		clearSTDIn();

		//Attempt to parse it into an int
		try {
			input = stoi(userInput);
		}
		catch(exception e){
			//If NaN, show message to user and prompt again
			cout << "NaN. Please enter a number between " << lowerBound << " and " << upperBound << endl;
			continue;
		}
		//Now a number, check to see if it can be returned
		if (input >= lowerBound && input <= upperBound) {
			return input;
		}

	}
}