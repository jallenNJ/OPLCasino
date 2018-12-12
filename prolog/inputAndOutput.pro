

/* *********************************************************************
Function Name: handleMenuChoice
Purpose: To Handle which choice the user choice on the action menu
Parameters:
            The Action Choosen (1 is save, 2 is move, 3 is Help, 4 is quit)
			The Current Round Num
			The Id of who is going first
			The current Deck on the table
			The current Human Player
			The Current Computer Player
			The ID of who captured last
Return Value: 
			None
Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
%Save and quit
handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Human, Computer, LastCap) :-
	%Make sure human is first
	isHuman(Human),
	%Change ids into atoms to be written to the save file
	denumerateId(FirstId, FirstIdAtom),
	denumerateId(LastCap, LastCapAtom),
	%Get all parts of the players
	getPlayerComponents(Computer, _, CompHand, CompPile, CompReserved, CompScore),
	getPlayerComponents(Human, _, HumanHand, HumanPile, HumanReserved, HumanScore),
	%Call the format function to parse the list correctly
	formatSaveData(RoundNum, CompScore, CompHand, CompPile, HumanScore, HumanHand, HumanPile, Table, CompReserved, HumanReserved,  LastCapAtom, Deck, FirstIdAtom, Formatted),
	%Ask the use for the name of the save file
	prompt1("Enter saveFile name: "),
	read(SaveFileName),
	%Save the game to disk then quit the program
	saveFile(SaveFileName, Formatted),
	quitGame.

%Swap players
handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Human, Computer, LastCap) :-
	handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Computer, Human, LastCap).
%Do nothing (Make a move).
handleMenuChoice(2,_, _, _,_,_,_,_).

%Ask for help 
handleMenuChoice(3, _,_, _,Table,Human,Computer,LastCap) :-
	isHuman(Human),
	writeln("Move Recomenndation: "),
	doComputerMove( Human, Computer, Table, LastCap, "recommending to play ", _,_, _, _).
%Swap players
handleMenuChoice(3, _,_, _,Table,Human,Comp,LastCap) :-
	handleMenuChoice(3, _, _, Table, Comp, Human, LastCap).

%Quit without saving
handleMenuChoice(4, _,_, _,_,_,_,_) :-
	quitGame.


/* *********************************************************************
Function Name: quitGame
Purpose: Output a closing message than close the game
Parameters:
            none
Return Value: 
            none
Algorithm:
            1) Thank the user for playing
			2) Close
Assistance Received: none
********************************************************************* */
quitGame() :-
	writeln("Thanks for playing Casino in Prolog :D"),
	halt(0).

%=======================
%Functions to get and validate input from the user
%=======================

/* *********************************************************************
Function Name: getActionChoice
Purpose: To print the action menu and get the choice
Parameters:
           None
Return Value: 
            The Selected action
Algorithm:
            1) Get Input
			2)Validate
Assistance Received: none
********************************************************************* */
getActionChoice(MoveChoice) :-
	%Prompt and get the input from the user
	prompt1("Would you like to (C)apture, (B)uild, or (T)rail: "),
	getMoveChar(InputChar),
	nl,
	%Make sure its a valid move, and then map to correct atom
	validateMoveChar(InputChar, Validated),
	convertMoveCharToNum(Validated, OptionChoice),
	MoveChoice = OptionChoice.

%Read in a char, could have been done inline
getMoveChar(Input) :- read(Input).

validateMoveChar(RawInput, Input) :-
	var(RawInput),
	prompt1("Please enter move as lowercase: "),
	getMoveChar(Raw),
	validateMoveChar(Raw, Input).


%Rules to validate input
validateMoveChar(c, c).
validateMoveChar(t, t).
validateMoveChar(b, b).
%If user entered a capital, it becomes a free var, so ask for them to enter a lowercase number
validateMoveChar(_, Output) :-
	prompt1("Invalid Move, Try again: "),
	getMoveChar(RetryInput),
	validateMoveChar(RetryInput, Validated),
	Output = Validated.

%Rules to map the move chars to nums	
convertMoveCharToNum(c, 0).
convertMoveCharToNum(b, 1).
convertMoveCharToNum(t, 2).	


/* *********************************************************************
Function Name: getNumericInput
Purpose: To get bounded numeric input from the user
Parameters:
            The Lower allowed bound (inclusive)
			The Upper allowed bound (inclusive)
Return Value: 
            The Validated result
Algorithm:
            1) Check if in bounds
			2) If it is, return, else reprompt
Assistance Received: none
********************************************************************* */
getNumericInput(Lower, Upper, Result) :-
	printLowerUpperBoundPrompt(Lower, Upper),
	read(Input),
	validateNumericInput(Lower, Upper, Input, Result).


/* *********************************************************************
Function Name: validateNumericInput
Purpose: Make sure input is within bounds
Parameters:
            The lower accepted bound (inclusive)
			The upper accepted bound (inclusive)
			The Value to check
Return Value: 
            Validated answer
Algorithm:
            1) Make sure it is a number
			2) Is in bounds
Assistance Received: none
********************************************************************* */
validateNumericInput(Lower, Upper, Check, Result) :-
	integer(Check),
	Check >= Lower,
	Check =< Upper,
	Result = Check.

%Invalid input
%This should actually fail, but didn't know prolog too well when I was here
validateNumericInput(_, _, _, _) :-
	write("Invalid input, try again: ").
		

/* *********************************************************************
Function Name: promptForMultipleNumericInput
Purpose: To display the correct prompt for input
Parameters:
            The lower inclusive bound
			The Upper inclusve bound
Return Value: 
            None
Algorithm:
            1) Concat string with parameters,
			2) Output the string
Assistance Received: none
********************************************************************* */
promptForMultipleNumericInput(Lower, Upper) :-
	concat("Select table indices between ", Lower, Str1),
	concat(Str1, " and ", Str2),
	concat(Str2, Upper, Str3),
	concat(Str3, ". Enter a -1 to stop: ", OutputStr),
	writeln(OutputStr).


/* *********************************************************************
Function Name: getMultipleNumberInput
Purpose: To have the user enter multiple numeric inputs for the same input
Parameters:
            The Inclusive upper bound
Return Value: 
            The resulting list
Algorithm:
            1) Output prompt
			2) Get the input,
			3) See if user wants another input
Assistance Received: none
********************************************************************* */
getMultipleNumericInput(Upper, Result) :-
	promptForMultipleNumericInput(-1, Upper),
	getNumericInput(-1, Upper, InputtedNumber),
	handleMultipleInputs( Upper,InputtedNumber, Result).




/* *********************************************************************
Function Name: handleMultipleInputs
Purpose: To see if the user wants to enter another input, or end the input
Parameters:
            The inclusive upper bound which is allowed
			The number the user entered
Return Value: 
            The List of numbers the user entereed
Algorithm:
            1) Check if input < 0
			2) If it is, end input, else get next
Assistance Received: none
********************************************************************* */
%User wants to end the list
handleMultipleInputs(_, InputtedNumber, []) :-
	integer(InputtedNumber),
	InputtedNumber < 0.	
%User wants to enter more
handleMultipleInputs( Upper, InputtedNumber, Result) :-
	integer(InputtedNumber),
	getMultipleNumericInput(Upper, PreviousResults),
	%Get the raw result of everything after this input
	UnsortedResult = [InputtedNumber | PreviousResults],
	%@> Is descending, remove dupes
	sort(0, @>, UnsortedResult, Result).



/* *********************************************************************
Function Name: getActionMenuChoice
Purpose: To get ther user's choice on the action menu
Parameters:
            Current Player -- The Playerlist currently making the move
Return Value: 
            The input the user entered
Algorithm:
            1) Display the correct prompt
			2) Get the input and validate
Assistance Received: none
********************************************************************* */
getActionMenuChoice(CurrentPlayer, UserInput) :-
	displayActionMenu(CurrentPlayer),
	getActionMenuInput(CurrentPlayer, UserInput),
	integer(UserInput).

/* *********************************************************************
Function Name: getActionMenuInput
Purpose: Get input for the actionMenu prompt
Parameters:
            The list of the current player
Return Value: 
            The Input the user entered
Algorithm:
            1) Make sure it is bound
			2) If computer's turn, map 3->4
Assistance Received: none
********************************************************************* */
%Human
getActionMenuInput(CurrentPlayer, Input) :-
	isHuman(CurrentPlayer),
	getNumericInput(1,4, Input).
%Comp
getActionMenuInput(CurrentPlayer, Input) :-
	getNumericInput(1,3, RawInput),
	mapCompActionMenuToHuman(CurrentPlayer, RawInput, Input).	


/* *********************************************************************
Function Name: mapCompActionMenuToHuman
Purpose: To map 3->4 when comp is the current player
Parameters:
            The Current Player
			The input they entered
Return Value: 
            The formatted input
Algorithm:
            1) If Comp, 3->4
Assistance Received: none
********************************************************************* */
%If human, skip over
mapCompActionMenuToHuman(CurrentPlayer, Input, Input) :-
	isHuman(CurrentPlayer).

%If comp and 3
mapCompActionMenuToHuman(_, Input, FormattedInput) :-
	Input = 3,
	FormattedInput = 4.

%If comp not three, skip over
mapCompActionMenuToHuman(_, Input, Input).		
	
%=======================
%Functions to print formatted data
%=======================

/* *********************************************************************
Function Name: printLoweUpperBoundPrompt
Purpose: To print a generic prompt for Upper and Lower Bound input (Inclusve)
Parameters:
            The inclusive lower bound
			The Inclusive upper bound
Return Value: 
            None, outputs
Algorithm:
            1) Concat string
			2) Then output
Assistance Received: none
********************************************************************* */
printLowerUpperBoundPrompt(Lower, Upper) :-
	concat("Enter a number between ", Lower, Str1),
	concat(Str1, " and ", Str2),
	concat(Str2, Upper, Str3),
	concat(Str3, ": ", OutputStr),
	prompt1(OutputStr).


/* *********************************************************************
Function Name: printCards
Purpose: Properly print out a list of cards
Parameters:
           The list of cards to prints
Return Value: 
            None, prints to screen
Algorithm:
            1) Print first of list
			2) Recursive call on rest of list
Assistance Received: none
********************************************************************* */
printCards([]) :- writeln(" ").

printCards([Card | Rest]) :-
	displayCard(Card),
	write(" "),
	printCards(Rest).


/* *********************************************************************
Function Name: printHand / printPile / printReserved
Purpose: Wrapper functions to print specific lists of cards
Parameters:
            The player list which to get the selected list fro
Return Value: 
            None
Algorithm:
            1) Get the required list
			2) Print the list(call printCards if needed)
Assistance Received: none
********************************************************************* */	
printHand(PlayerList) :-
	getHand(PlayerList, Hand),
	printCards(Hand).

printPile(PlayerList) :-
	getPile(PlayerList, Pile),
	printCards(Pile).
	
printReserved(PlayerList) :-
	getReserved(PlayerList, Reserved),
	write(Reserved).

/* *********************************************************************
Function Name: printPlayer
Purpose: Prints out a formatted player
Parameters:
           The Playerlist to print
Return Value: 
          None. Prints out in form:
		  NAME:  HAND
		  Pile: PILE
		  BuildSums: RESERVED
Algorithm:
            1) Go through each component
			2) Print it out
Assistance Received: none
********************************************************************* */
printPlayer(PlayerList) :-
	%Output name with a colon, and then the hand
	getPlayerName(PlayerList, Name),
	write(Name),
	write(": "),
	printHand(PlayerList),
	nl,
	%Then the pile
	write("Pile: "),
	printPile(PlayerList),
	nl,
	%Lastly the builds
	write("Build Sums: "),
	printReserved(PlayerList).		


/* *********************************************************************
Function Name: printFullTable
Purpose: To print out the current state of the table
Parameters:
            The Human Player
			The current table cards
			The current Computer Player
			The current state of deck
Return Value: 
            None, prints to console in form:
			====
			COMPUTER
			----
			Table: TABLE
			---
			Human
			---
			Deck
			=====
Algorithm:
            1) Print each piece piece-wise, and seperate with lines
Assistance Received: none
********************************************************************* */
printFullTable(HumanPlayer, Table, ComputerPlayer, Deck) :-
	isHuman(HumanPlayer),
	writeln("==========================="),
	printPlayer(ComputerPlayer),
	nl,
	writeln("-------------------------"),
	write("Table: "),
	printCards(Table),
	nl,
	writeln("-------------------------"),
	printPlayer(HumanPlayer),
	nl,
	writeln("-------------------------"),
	write("Deck: "),
	printCards(Deck),
	nl,
	writeln("===========================").

%Bounce back function to swap orders
printFullTable(ComputerPlayer, Table, HumanPlayer, Deck) :-
	printFullTable(HumanPlayer, Table, ComputerPlayer, Deck).


/* *********************************************************************
Function Name: displayActionMenu
Purpose: Display the action menu between turns
Parameters:
            The current player
Return Value: 
            None, prints to console
Algorithm:
            1)If human, output four options, else print out three
Assistance Received: none
********************************************************************* */	
%For Human
displayActionMenu(CurrentPlayer) :-
	isHuman(CurrentPlayer),
	writeln("1) Save the game."),
	writeln("2) Make a move (Human)."),
	writeln("3) Ask for help."),
	writeln("4) Quit the game.").

%For computer		
displayActionMenu(_) :-
	writeln("1) Save the game."),
	writeln("2) Make a move (Computer)."),
	writeln("3) Quit the game.").


/* *********************************************************************
Function Name: displayComputerMove\5
Purpose: To output a computer move with cards on the table
Parameters:
            The Card played on the table
			The cards on the table
			The move verb (Build/ Capture)
			The action (create a build / capture a set etc)
			The Reason (Why did the ai choose this)
Return Value: 
            None Prints in format
			********
			Computer is MOVE_VERB PLAYEDCARD to ACTION on these TABLE_CARDS
			The AI choose thise action to: REASON
Algorithm:
            1) Output the paremeters formatted in a sentence
Assistance Received: none
********************************************************************* */
displayComputerMove(PlayedCard, TableCards, MoveVerb, Action, Reason) :-
	writeln("***************************"),
	write("Computer is "),
	write(MoveVerb),
	write(" "),
	displayCard(PlayedCard),
	write(" to "),
	write(Action),
	write( " on these cards: "),
	displayCard(TableCards),
	nl,
	write("The AI Choose this action to: "),
	writeln(Reason),
	writeln("***************************").


/* *********************************************************************
Function Name: displayComputerMove\3
Purpose: Display a move without cards on the table
Parameters:
            The Player making the move
            The Moveverb (Trail),
			The index of the played card
Return Value: 
            None, The format
			Computer MOVE_VERB PLAYED_CARD "as a Trail as tehre were no other moves available"
			Algorithm:
            1) Check if selected cards are valid
            2) If it is, do the move, otherwise reprompt
Assistance Received: none
********************************************************************* */
displayComputerMove(PlayerList, MoveVerb, PlayedCardIndex) :-
	getHand(PlayerList, Hand),
	nth0(PlayedCardIndex, Hand, PlayedCard),
	write("Computer "),
	write(MoveVerb),
	write(" "),
	displayCard(PlayedCard),
	writeln(" as a Trail as there were no other moves available").
