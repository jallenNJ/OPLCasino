#include "Human.h"



Human::Human() {
	setName();
}
void Human::setName() {
	string input = "";

	while (input.length() == 0) {
		cout << "Enter your name:  " << endl;
		cin >> input;
	}
	
	name = input;

	cout << "Welcome " << name << "!" << endl;
	
}