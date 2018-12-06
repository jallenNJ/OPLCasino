
%=======================
%Functions to generate internal data
%=======================

%Get all numbers from 0 to Num
listAll(Num, []) :- 
	Num<0.

listAll(Num, Result):- 
	NewNum is Num-1, 
	listAll(NewNum, NewResult),
	Result=[Num|NewResult].

%=======================
%Functions to create and manipulate cards
%=======================

	
%Get all Suits	
getSuits(Val, []) :-
		Val >=4.
		
getSuits(Val, Result) :-
	NewVal is Val+1, 
	getSuits(NewVal, NewResult),
	numToSuit(Val, Suit),
	Result=[Suit|NewResult].
	
	
%Suit to nums	
numToSuit(0, Output) :- Output = "s".	
numToSuit(1, Output) :- Output = "c".	
numToSuit(2, Output) :- Output = "d".	
numToSuit(3, Output) :- Output = "h".	

%Creates card in form [SUIT SYMBOL]
createCard(Suit, Val, [Suit | Val]).



isBuild([FirstCard|Rest]) :-
	FirstCard = [Suit | Symbol],
	not(Suit=[]),
	not(Symbol=[]),
	not(Rest=[]).
%Displays card to the screen 

%End Build Recursion
displayBuild([]).

displayBuild(Build) :-
	Build = [BuildCard | RestOfBuild],
	displayCard(BuildCard),
	write(" "),
	displayBuild(RestOfBuild).

%If inputted card is a build, display every card enclosed in []
displayCard(Build) :- 
	
	isBuild(Build),
	write("[ "),
	displayBuild(Build),
	write("]").


%Display a single card
displayCard([Suit|Card]) :-
	write(Suit), 
	write(Card).	



getBuildCards(Build, CardOutput) :-
	isBuild(Build),
	flatten(Build, RawAtoms),
	rebuildAtomListToCards(RawAtoms, CardOutput).
	


getBuildCards(Build, Build).

rebuildAtomListToCards([],[]).
rebuildAtomListToCards([[Suit | [Sym | RestAtoms]]], RebuiltCards) :-
	rebuildAtomListToCards(RestAtoms, RestCards),
	createCard(Suit, Sym, ThisCard),
	RebuiltCards=[ThisCard | RestCards].


getCardSymbol(Build, Sym) :-
	isBuild(Build),
	sumCardList(Build, Val),
	allNumsToFaceLetters([Val], Sym).

getCardSymbol([_|Sym], Sym).


getCardVal(Build, Val):-
	isBuild(Build),
	sumCardList(Build, Val).

getCardVal(Card, Val) :-
	getCardSymbol(Card, Sym),
	convertSymbolToVal(Sym, Val).

convertSymbolToVal("K", 13).
convertSymbolToVal("Q", 12).
convertSymbolToVal("J", 11).
convertSymbolToVal("X", 10).
convertSymbolToVal("A", 1).
convertSymbolToVal(Val, Val) :-
	integer(Val).	

%Creates all cards for a suit, from K to A in a list
createCardsForSuit(_, [_|CurrentVal], []) :-	CurrentVal = [].	

createCardsForSuit(Suit, [Current|Rest], SuitCards) :- 
	createCard(Suit, Current, CreatedCard), 
	%displayCard(CreatedCard), 
	createCardsForSuit(Suit, Rest, NewSuitCards), 	
	SuitCards = [CreatedCard | NewSuitCards].
	
%Megers list in order [List one List two]
mergeLists(ListOne, ListTwo, Result) :- append(ListOne, ListTwo, Result).

%Converts all numeric values to symbol represntation
allNumsToFaceLetters(Input, Output) :-
	replace(Input, 13, "K", KAdded),
	replace(KAdded, 12, "Q", QAdded),
	replace(QAdded, 11, "J", JAdded),
	replace(JAdded, 10, "X", XAdded),
	replace(XAdded, 1, "A", Output).

createDeck(Result) :-
		listAll(13, RawNums),
		allNumsToFaceLetters(RawNums, Nums),
		createCardsForSuit("S", Nums, Spades),
		createCardsForSuit("C", Nums, Clubs),
		createCardsForSuit("D", Nums, Diamonds),
		createCardsForSuit("H", Nums, Hearts),
		mergeLists(Spades, Clubs, MergeOne),
		mergeLists(MergeOne, Diamonds, MergeTwo),
		mergeLists(MergeTwo, Hearts, RawDeck),
		random_permutation(RawDeck, Result).


%Replaces first occurance of key with value		
replace([], _, _, []).	
replace([Key | Rest], Key, Value, [Value | Rest]).
replace([First|Rest], Key, Value, [First | NewRest]) :- replace(Rest, Key,Value,NewRest).	

%removeAtIndex(InputList, Index, ResultingList) :-
% End of list before finding target index
removeAtIndex([], _, [], _).
%Current element is the one to be removed, end recursion
removeAtIndex([Current | Rest], 0, ResultingList, RemovedElement) :-	
	ResultingList = Rest,
	RemovedElement = Current.

%Recursive case
removeAtIndex([Current | Rest], Index, ResultingList, RemovedElement) :-
	Index > 0,
	NewIndex is Index-1,
	removeAtIndex(Rest, NewIndex, NewResultingList, RemovedElement),
	ResultingList = [Current | NewResultingList].

%base case, remaining list is what is the resulting list
removeAllIndices([], RemaingCards, RemaingCards, []).
%Recursive Case
removeAllIndices([CurrentIndex | Rest], InputList, ResultingList, RemovedElements) :-
	removeAtIndex(InputList, CurrentIndex, RemainingCards, RemovedCard),
	removeAllIndices(Rest, RemainingCards, ResultingList, RestRemovedCards),
	RemovedElements = [RemovedCard | RestRemovedCards].

drawFourCards(InputDeck, OutputDeck, DrawnCards):-
	nth0(0, InputDeck, FirstCard),
	nth0(1, InputDeck, SecondCard),
	nth0(2, InputDeck, ThridCard),
	nth0(3, InputDeck, FourthCard),
	removeNCards(4, InputDeck, OutputDeck),
	mergeLists([FirstCard], [SecondCard], TwoMerged),
	mergeLists(TwoMerged, [ThridCard], ThreeMerged),
	mergeLists(ThreeMerged,[FourthCard], DrawnCards).

removeNCards(Amount, Rem, Out) :- 
	Amount =< 0,
	Out = Rem.

removeNCards(Amount, InputList, Output) :-
	NewAmount is Amount-1,
	InputList = [_ | Rest],
	removeNCards(NewAmount, Rest, NewOutput),
	Output = NewOutput.

removeMatchingSymbols([], _, [],[]).

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	getCardSymbol(Currentcard, CurrentcardVal),
	CurrentcardVal = Value,
	removeMatchingSymbols(Rest, Value, RemainingCards, NewRemoved),
	RemovedCards = [Currentcard | NewRemoved].

removeMatchingSymbols([Currentcard | Rest], Value, RemainingCards, RemovedCards) :-
	removeMatchingSymbols(Rest, Value, NewRemainingCards, RemovedCards),
	RemainingCards = [Currentcard | NewRemainingCards].


findMatchingSymbolsIndices([], _, _,[]).

findMatchingSymbolsIndices([Currentcard | Rest], Value, Index, Indices) :-
	NextIndex is Index+1,
	getCardSymbol(Currentcard, CurrentcardVal),
	CurrentcardVal = Value,
	findMatchingSymbolsIndices(Rest, Value, NextIndex,  NewIndices),
	Indices = [Index | NewIndices].

findMatchingSymbolsIndices([ _ | Rest], Value, Index, Indices) :-
	NextIndex is Index+1,
	findMatchingSymbolsIndices(Rest, Value, NextIndex, Indices).



sumSelectedCards([], _, 0).


sumSelectedCards([CurrentIndex | RestIndicies], Table, Sum) :-
	nth0(CurrentIndex, Table, SelectedCard),
	sumSelectedCards(RestIndicies, Table, RemainingSum),
	getCardVal(SelectedCard, Val),
	Sum is RemainingSum+Val.


sumCardList([], 0).
sumCardList([Current | Rest], Sum) :-
	getCardVal(Current, ThisVal),
	sumCardList(Rest, RestVal),
	Sum is ThisVal+RestVal.
	

makeBuild([], BuildCard, [BuildCard]).

makeBuild([CurrentCard | Rest], BuildCard, Build) :-
	makeBuild(Rest, BuildCard, NewBuild),
	Build = [CurrentCard | NewBuild].

%=======================
%Functions to create and manipulate the player data structure
%=======================
%Wrapper to createPlayer
createNewPlayer(Id, StartingCards, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], CreatedPlayer).

%Creates a playerList from the given parameters
createPlayer(Id, StartingCards, StartingPile, StartingReserved, CreatedPlayer) :-
	mergeLists([Id], [StartingCards], MergeOne),
	mergeLists(MergeOne, [StartingPile],MergeTwo),
	mergeLists(MergeTwo, [StartingReserved], CreatedPlayer).


getId(PlayerList, PlayerId) :-
	nth0(0, PlayerList, PlayerId).

getHand(PlayerList, PlayerHand) :-
	nth0(1, PlayerList, PlayerHand).

getPile(PlayerList, PlayerPile) :-
	nth0(2, PlayerList, PlayerPile).

getReserved(PlayerList, PlayerReserved) :-
	nth0(3, PlayerList, PlayerReserved).

getPlayerComponents(PlayerList, Id, Hand, Pile, Reserved) :-
	getId(PlayerList, Id),
	getHand(PlayerList, Hand),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved).



isHuman(PlayerList) :-
	getId(PlayerList, Id),
	Id = 0.

getPlayerName(PlayerList, Name) :-
	isHuman(PlayerList),
	Name = "Human".

getPlayerName(_, "Computer").

listContainsValue([], _, _, -1).

listContainsValue([CurrentCard|_], TargetVal, Index, FoundIndex) :-
	getCardVal(CurrentCard, CardVal),
	CardVal = TargetVal,
	FoundIndex = Index.

listContainsValue([_|Rest], TargetVal, Index, FoundIndex) :-
	NextIndex is Index+1,
	listContainsValue(Rest, TargetVal, NextIndex, FoundIndex).

addToPile(NewCards, [], NewCards).

addToPile(NewCards, [FirstCard | Rest], PileWithAddedCards) :-	
	isBuild(FirstCard),
	getBuildCards(FirstCard, ListOfCards),
	addToPile(NewCards, Rest, RestPile),
	mergeLists(RestPile, ListOfCards, PileWithAddedCards).

addToPile(NewCards, [Card| Rest], PileWithAddedCards) :-
	addToPile(NewCards, Rest, NewPile),
	PileWithAddedCards = [ Card | NewPile].

%=======================
%Functions to run a round
%=======================
startNewRound() :- playRound(0, [],[],[],[], _).


%id, deck, table, p0Info, p1Info, retVal
playRound(FirstId,[],[],[],[],_) :- 
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(FirstId, Deck, TableCards, HumanPlayer, ComputerPlayer, _).

%Deal new hands when hands are empty
playRound(FirstId, Deck, Table, P0Info, P1Info, _) :-
	getHand(P0Info, P0Hand),
	length(P0Hand, 0),
	getHand(P1Info, P1Hand),
	length(P1Hand, 0),
	length(Deck, CardsInDeck),
	CardsInDeck >= 8,
	drawFourCards(Deck, AfterOneDraw, P0Cards),
	drawFourCards(AfterOneDraw, AfterTwoDraws, P1Cards),
	getPlayerComponents(P0Info, P0Id, _, P0Pile, P0Reserved),
	createPlayer(P0Id, P0Cards, P0Pile, P0Reserved, NewP0),
	getPlayerComponents(P1Info, P1Id, _, P1Pile, P1Reserved),
	createPlayer(P1Id, P1Cards, P1Pile, P1Reserved, NewP1),
	playRound(FirstId, AfterTwoDraws, Table, NewP0, NewP1, _).
	

%Round end rule, deck is empty, player infos have null hands
%TODO, make sure to print table
%playRound(_,[])	

%Main loop
playRound(FirstId, Deck, Table, P0Info, P1Info, _) :-
	printFullTable(P0Info, Table, P1Info, Deck),
	getActionMenuChoice(P0Info, MenuChoice),
	handleMenuChoice(MenuChoice),
	doPlayerMove(P0Info, Table, P0AfterMove, TableAfterP0),
	printFullTable(P0AfterMove, TableAfterP0, P1Info, Deck),
	getActionMenuChoice(P1Info, MenuChoice2),
	handleMenuChoice(MenuChoice2),
	doPlayerMove(P1Info, TableAfterP0, P1AfterMove, TableAfterP1),
	playRound(FirstId, Deck, TableAfterP1, P0AfterMove, P1AfterMove, _).



%Save and quit
handleMenuChoice(1) :-
	writeln("Implement Saving").

%Do nothing.
handleMenuChoice(2).

%Ask for help
handleMenuChoice(3) :-
	writeln("Implement Help").

%Quit without saving
handleMenuChoice(4) :-
	writeln("Thanks for playing Casino in Prolog :D"),
	halt(0).	

%=======================
%Functions for round to call, resolves to human or computer
%=======================

%Human Player
doPlayerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	isHuman(PlayerList),
	getActionChoice(MoveChoice),
	integer(MoveChoice),
	doHumanMove(MoveChoice, PlayerList, Table, PlayerAfterMove, TableAfterMove).

%Comp
doPlayerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	doComputerMove( PlayerList, Table, PlayerAfterMove, TableAfterMove).


%=======================
%Functions for human to make a move
%=======================
doHumanMove(0, PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Capture with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, CaptureCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doCapture(PlayerList, Table, CaptureCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove).

doHumanMove(1, PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to Build with?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, PlayedCardIndex),
	length(Table, TableCardAmount),
	TableAllowedIndices is TableCardAmount-1,
	getMultipleNumericInput(TableAllowedIndices, SelectedCardIndices),
	doBuild(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove).

doHumanMove(2, PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	prompt1("Which card would you like to trail?"),
	length(Hand, CardsInHandPlusOne),
	CardsInHand is CardsInHandPlusOne-1,
	getNumericInput(0, CardsInHand, TrailedCardIndex),
	doTrail(PlayerList, Table, TrailedCardIndex, PlayerAfterMove, TableAfterMove).


%=======================
%Functions for computer to make a move
%=======================
doComputerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove):-
	getHand(PlayerList, Hand),
	checkSetCapture(Hand, Table, HandAfterMove, TableAfterMove,CapturedCards),
	getPlayerComponents(PlayerList, Id, _, StartingPile, Reserved),
	writeln("Compt got: "),
	printCards(CapturedCards),
	%addToPile(StartingPile, CapturedCards, NewPile),
	addToPile(CapturedCards, StartingPile, NewPile),
	createPlayer(Id, HandAfterMove, NewPile, Reserved, PlayerAfterMove).


%	not(MatchedCardsIndices=[]),
%	write("DEBEEEEEBUG AI CAPTURE"),
	%doCapture(PlayerList, Table, PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, TableAfterMove).



doComputerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove):-
	getHand(PlayerList, Hand),
	checkForMatchingCaptures(Hand, Table, 0, PlayedCardIndex,MatchedCardsIndices),
	not(MatchedCardsIndices=[]),
	write("DEBEEEEEBUG AI CAPTURE"),
	doCapture(PlayerList, Table, PlayedCardIndex, MatchedCardsIndices, PlayerAfterMove, TableAfterMove).

	


%doComputerMove(1, PlayerList, Table, PlayerAfterMove, TableAfterMove)
doComputerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	doTrail(PlayerList, Table, 0, PlayerAfterMove, TableAfterMove),
	displayComputerMove(PlayerList, 0).
	


checkForMatchingCaptures([], _, [], []).

checkForMatchingCaptures([CurrentCard | _], Table, Index, CardWithMatches, MatchedCards):-
	getCardSymbol(CurrentCard, CardSym),
	findMatchingSymbolsIndices(Table, CardSym, 0, MatchingCards),
	length(MatchingCards, CardsThatMatched),
	CardsThatMatched > 0,
	CardWithMatches = Index,
	MatchedCards = MatchingCards.


checkForMatchingCaptures([_ | RestHand], Table, Index, CardWithMatches, MatchedCards) :-
	NextIndex is Index+1,
	checkForMatchingCaptures(RestHand, Table, NextIndex, CardWithMatches, MatchedCards).


%=======================
%Functions to validate and execute the choosen move (for both players)
%=======================
doCapture(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove) :-
	%Get the hand and ensure the Played index is instaniated
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, CaptureCard),
	getCardVal(CaptureCard, CaptureVal),
	%Sum all selected cards to make sure the capture is valid
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	ModResult is mod(Sum, CaptureVal),
	ModResult = 0,
	%Remove the cards
	removeAllIndices(SelectedCardIndices, Table, TableAfterMove, CaputuredCards),
	%Ensure atleast one card is selected
	length(CaputuredCards, CapturedAmounts),
	CapturedAmounts > 0,
	%Ensure all required cards are taken
	getCardSymbol(CaptureCard, CaptureSym),
	removeMatchingSymbols(TableAfterMove, CaptureSym, _, Matching),
	length(Matching, MatchingSize),
	MatchingSize =0,
	%Make the updated player
	getPlayerComponents(PlayerList, Id, _, StartingPile, Reserved),
	mergeLists(StartingPile, [CaptureCard], PilewithCapCard),
	addToPile( CaputuredCards, PilewithCapCard,AllPileCards),
	createPlayer(Id, ResultingHand, AllPileCards, Reserved, PlayerAfterMove).


doBuild(PlayerList, Table, PlayedCardIndex, SelectedCardIndices, PlayerAfterMove, TableAfterMove) :-	
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	%Remove played card from the hand and get its value
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, PlayedCard),
	getCardVal(PlayedCard, PlayedVal),
	sumSelectedCards(SelectedCardIndices, Table, Sum),
	BuildValue is Sum+PlayedVal,
	BuildValue =< 14,
	listContainsValue(ResultingHand, BuildValue, 0, ReserveCardIndex),
	ReserveCardIndex >=0,
	getReserved(PlayerList, StartingReserved),
	NewReserved = [BuildValue | StartingReserved],
	removeAllIndices(SelectedCardIndices, Table, TableAfterCardsRemoved, TableBuildCards),
	makeBuild(TableBuildCards, PlayedCard, NewBuild),
	TableAfterMove = [NewBuild | TableAfterCardsRemoved],
	getPlayerComponents(PlayerList, Id, _, Pile, _),
	createPlayer(Id, ResultingHand, Pile, NewReserved, PlayerAfterMove).

doTrail(PlayerList, Table, PlayedCardIndex, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	integer(PlayedCardIndex),
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, TrailedCard),
	mergeLists(Table, [TrailedCard], TableAfterMove),
	getId(PlayerList, Id),
	getPile(PlayerList, Pile),
	getReserved(PlayerList, Reserved),
	createPlayer(Id, ResultingHand, Pile, Reserved, PlayerAfterMove).

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
	concat("Enter a number between ", Lower, Str1),
	concat(Str1, " and ", Str2),
	concat(Str2, Upper, Str3),
	concat(Str3, ". Enter a negative number to stop: ", OutputStr),
	prompt1(OutputStr).

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

%Bound back function to swap orders
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

displayComputerMove(PlayerList, PlayedCardIndex) :-
	getHand(PlayerList, Hand),
	nth0(PlayedCardIndex, Hand, PlayedCard),
	write("Computer trailed with "),
	displayCard(PlayedCard),
	writeln(" as there were no other moves available").




%=======================
%Functions to print formatted data
%=======================	

load :-
	prompt1("What file would you like to load?"),
	read(File),
	readFile(File, Data),
	parseSaveToRound(Data).

readFile(FileName, FileData) :-
	open(FileName, read, FileStream),
	read(FileStream, FileData).

parseSaveToRound(RawData) :-

	nth0(2, RawData, CompHand),

	nth0(3, RawData, CompPile),

	nth0(5, RawData, HumanHand),
	nth0(6, RawData,HumanPile),

	nth0(7, RawData, Table),

	length(RawData, Terms),
	DeckIndex is Terms-2,
	nth0(DeckIndex, RawData, Deck),


	createPlayer(0, HumanHand, HumanPile, [], Human),
	createPlayer(1, CompHand, CompPile, [], Comp),

	playRound(0, Deck, Table, Human, Comp, _).




checkSetCapture([], _, [], [],[]).

checkSetCapture([Card|HandAfterMove], Table, HandAfterMove, TableAfterMove, CapturedCards) :-
	getCardVal(Card, Target),
	findSet(Table, Target, TableAfterMove, CapturedCardsFromTable),
	length(CapturedCardsFromTable, AmountCapped),
	AmountCapped > 1,
	CapturedCards = [Card|CapturedCardsFromTable].

checkSetCapture([_| Rest], Table, HandAfterMove, TableAfterMove, CapturedCards):-
checkSetCapture(Rest, Table, HandAfterMove, TableAfterMove, CapturedCards).



%findSet([], , [], []).
findSet([FirstCard| Rest], Target, TableAfterMove, Captured) :-
	getCardVal(FirstCard, ThisVal),
	NextTarget is Target-ThisVal,
	NextTarget >=0,
	findSet(Rest, NextTarget, TableAfterMove, ThatCaptured),
	Captured = [FirstCard | ThatCaptured].

findSet(Table, 0, Table, []).


findSet([First | Rest], Target, TableAfterMove, Captured) :-
	findSet(Rest, Target, Table, Captured),
	TableAfterMove=[First|Table].