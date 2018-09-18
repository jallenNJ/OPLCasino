#ifndef CARD_H
#define CARD_H
#include <string>
#include <vector>
using namespace std;
class Card {
	
public:
	/* *********************************************************************
Function Name: Card()
Purpose: Intaialize Object to the invalid card
Parameters:
			void
Return Value: Constructor
Local Variables:
			none
Algorithm:
			1) Intialize the card to its invalid value
Assistance Received: none
********************************************************************* */
	Card() {
		setInvalidCard();
	}
	/* *********************************************************************
Function Name: toString
Purpose: To format the Card in a form to be output 
Parameters:
			none
Return Value: string,   The formatted string to be output
Local Variables:
			none
Algorithm:
			1) Get the suit
			2) Get the symbol
			3) Put both in a string as (suitSymbol)
Assistance Received: none
********************************************************************* */
	virtual const string toString() const{
		return string(1, suit)+ string(1, symbol);
	}
	
	const bool checkCapture(Card);
	const bool checkCapture(vector<Card>);

	/* *********************************************************************
Function Name: getSymbol()
Purpose: Return the symbol on the card
Parameters:
			none
Return Value: a const char representing the symbol on the card. 2-9 or A,X,J,Q,K
Local Variables:
			none
Algorithm:
			1) Get the symbol
Assistance Received: none
********************************************************************* */
	const virtual char getSymbol() const{
		return symbol;
	}



	/* *********************************************************************
Function Name: getNumericValue()
Purpose: To retrieve the numeric value the card represent
Parameters:
			none
Return Value: a const int, which will be betwee 1-13. Aces always return 1
Local Variables:
			none
Algorithm:
			1)Return the Numeric Value
Assistance Received: none
********************************************************************* */

	//Aces return 1
	const virtual int getNumericValue() const{
		return numericValue;
	}

	/* *********************************************************************
Function Name: isInvalid()
Purpose: Check if the card is set to the invalid card
Parameters:
			none
Return Value: a const bool. True means the card is set to the invaild card, false is any other card
Local Variables:
			none
Algorithm:
			1) Check if card's suit matches the invalid suit
Assistance Received: none
********************************************************************* */
	const bool isInvalid() const{
		return suit == invalidSymbol;
	}
	/* *********************************************************************
Function Name: getSuit()
Purpose: Get the suit on the card face
Parameters:
			none
Return Value: A const char reprsenting the suit. S,Q,C,D are the valid values for non-error states 
Local Variables:
			temp[], an integer array used to sort the grades
Algorithm:
			1) Add all the grades
			2) Divide the sum by the number of students in class to calculate the average
Assistance Received: none
********************************************************************* */
	const char getSuit() const{
		return suit;
	}

	/* *********************************************************************
Function Name: setOwner
Purpose: Set the owner of the card
Parameters:
			name, a string representing the name of the owner
Return Value: bool, for if it completed. True if value set
Local Variables:
			none
Algorithm:
			1) set the membervariable to match the new name
Assistance Received: none
********************************************************************* */
	const bool setOwner(string name){
		ownerName = name;
		return true;
	}

	/* *********************************************************************
Function Name: getOwner
Purpose: To get the owner of the card
Parameters:
			none
Return Value: a const string representing the owner of the card
Local Variables:
			none
Algorithm:
			1) Retrieve the owner of the card
Assistance Received: none
********************************************************************* */
	const string getOwner() const{
		return ownerName;
	}

protected:
	char suit;
	char symbol;
	string ownerName;

	void symbolToNumericValue();
private:
	int numericValue;

	const char invalidSymbol = 'N';

	void setInvalidCard() {
		suit = invalidSymbol;
		symbol = '0';
	}

	
};


#endif // !CARD_H


