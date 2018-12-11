
%Save and quit
handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Human, Computer, LastCap) :-
	isHuman(Human),
	denumerateId(FirstId, FirstIdAtom),
	denumerateId(LastCap, LastCapAtom),
	getPlayerComponents(Computer, _, CompHand, CompPile, CompReserved, CompScore),
	getPlayerComponents(Human, _, HumanHand, HumanPile, HumanReserved, HumanScore),
	formatSaveData(RoundNum, CompScore, CompHand, CompPile, HumanScore, HumanHand, HumanPile, Table, CompReserved, HumanReserved,  LastCapAtom, Deck, FirstIdAtom, Formatted),
	prompt1("Enter saveFile name"),
	read(SaveFileName),
	saveFile(SaveFileName, Formatted),
	quitGame.

handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Human, Computer, LastCap) :-
	handleMenuChoice(1,RoundNum, FirstId, Deck, Table, Computer, Human, LastCap).
%Do nothing.
handleMenuChoice(2,_, _, _,_,_,_,_).

%Ask for help
handleMenuChoice(3, _,_, _,Table,Human,Computer,LastCap) :-
	isHuman(Human),
	writeln("Move Recomenndation: "),
	doComputerMove( Human, Computer, Table, LastCap, "recommending to play ", _,_, _, _).

handleMenuChoice(3, _,_, _,Table,Human,Comp,LastCap) :-
	handleMenuChoice(3, _, _, Table, Comp, Human, LastCap).

%Quit without saving
handleMenuChoice(4, _,_, _,_,_,_,_) :-
	quitGame.



quitGame() :-
	writeln("Thanks for playing Casino in Prolog :D"),
	halt(0).

%=======================
%Functions to get and validate input from the user
%=======================

getActionChoice(MoveChoice) :-
	prompt1("Would you like to (C)apture, (B)uild, or (T)rail: "),
	getMoveChar(InputChar),
	nl,
	validateMoveChar(InputChar, Validated),
	convertMoveCharToNum(Validated, OptionChoice),
	MoveChoice = OptionChoice.


getMoveChar(Input) :- read(Input).

validateMoveChar(RawInput, Input) :-
	var(RawInput),
	prompt1("Please enter move as lowercase: "),
	getMoveChar(Raw),
	validateMoveChar(Raw, Input).

validateMoveChar(c, c).
validateMoveChar(t, t).
validateMoveChar(b, b).
validateMoveChar(_, Output) :-
	prompt1("Invalid Move, Try again: "),
	getMoveChar(RetryInput),
	validateMoveChar(RetryInput, Validated),
	Output = Validated.
	
convertMoveCharToNum(c, 0).
convertMoveCharToNum(b, 1).
convertMoveCharToNum(t, 2).	


getNumericInput(Lower, Upper, Result) :-
	printLowerUpperBoundPrompt(Lower, Upper),
	read(Input),
	validateNumericInput(Lower, Upper, Input, Result).


validateNumericInput(Lower, Upper, Check, Result) :-
	integer(Check),
	Check >= Lower,
	Check =< Upper,
	Result = Check.

validateNumericInput(_, _, _, _) :-
	write("Invalid input, try again: ").
	%sgetNumericInput(Lower, Upper, Result).	

promptForMultipleNumericInput(Lower, Upper) :-
	concat("Select table indices between ", Lower, Str1),
	concat(Str1, " and ", Str2),
	concat(Str2, Upper, Str3),
	concat(Str3, ". Enter a -1 to stop: ", OutputStr),
	writeln(OutputStr).

getMultipleNumericInput(Upper, Result) :-
	promptForMultipleNumericInput(-1, Upper),
	getNumericInput(-1, Upper, InputtedNumber),
	handleMultipleInputs( Upper,InputtedNumber, Result).

handleMultipleInputs(_, InputtedNumber, []) :-
	integer(InputtedNumber),
	InputtedNumber < 0.	

handleMultipleInputs( Upper, InputtedNumber, Result) :-
	integer(InputtedNumber),
	getMultipleNumericInput(Upper, PreviousResults),
	UnsortedResult = [InputtedNumber | PreviousResults],
	%@> Is descending, remove dupes
	sort(0, @>, UnsortedResult, Result).
	
	getActionMenuChoice(CurrentPlayer, UserInput) :-
	displayActionMenu(CurrentPlayer),
	getActionMenuInput(CurrentPlayer, UserInput),
	integer(UserInput).


getActionMenuInput(CurrentPlayer, Input) :-
	isHuman(CurrentPlayer),
	getNumericInput(1,4, Input).

getActionMenuInput(CurrentPlayer, Input) :-
	getNumericInput(1,3, RawInput),
	mapCompActionMenuToHuman(CurrentPlayer, RawInput, Input).	

mapCompActionMenuToHuman(CurrentPlayer, Input, Input) :-
	isHuman(CurrentPlayer).

mapCompActionMenuToHuman(_, Input, FormattedInput) :-
	Input = 3,
	FormattedInput = 4.

mapCompActionMenuToHuman(_, Input, Input).		
	
%=======================
%Functions to print formatted data
%=======================	
printLowerUpperBoundPrompt(Lower, Upper) :-
	concat("Enter a number between ", Lower, Str1),
	concat(Str1, " and ", Str2),
	concat(Str2, Upper, Str3),
	concat(Str3, ": ", OutputStr),
	prompt1(OutputStr).

printCards([]) :- writeln(" ").

printCards([Card | Rest]) :-
	displayCard(Card),
	write(" "),
	printCards(Rest).

	
printHand(PlayerList) :-
	getHand(PlayerList, Hand),
	printCards(Hand).

printPile(PlayerList) :-
	getPile(PlayerList, Pile),
	printCards(Pile).
	
printReserved(PlayerList) :-
	getReserved(PlayerList, Reserved),
	write(Reserved).

printPlayer(PlayerList) :-
	getPlayerName(PlayerList, Name),
	write(Name),
	write(": "),
	printHand(PlayerList),
	nl,
	write("Pile: "),
	printPile(PlayerList),
	nl,
	write("Build Sums: "),
	printReserved(PlayerList).		

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


displayComputerMove(PlayerList, MoveVerb, PlayedCardIndex) :-
	getHand(PlayerList, Hand),
	nth0(PlayedCardIndex, Hand, PlayedCard),
	write("Computer "),
	write(MoveVerb),
	write(" "),
	displayCard(PlayedCard),
	writeln(" as a Trail as there were no other moves available").
