%Get all numbers from 0 to Num
listAll(Num, []) :- 
	Num<0.

listAll(Num, Result):- 
	NewNum is Num-1, 
	listAll(NewNum, NewResult),
	Result=[Num|NewResult].
	
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

%Displays card to the screen 
displayCard([Suit|Card]) :- write(Suit), write(Card).

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

%Wrapper to createPlayer
createNewPlayer(Id, StartingCards, CreatedPlayer) :-
	createPlayer(Id, StartingCards, [], [], CreatedPlayer).

%Creates a playerList from the given parameters
createPlayer(Id, StartingCards, StartingPile, StartingReserved, CreatedPlayer) :-
	mergeLists([Id], [StartingCards], MergeOne),
	mergeLists(MergeOne, [StartingPile],MergeTwo),
	mergeLists(MergeTwo, [StartingReserved], CreatedPlayer).


getHand(PlayerList, PlayerHand) :-
	nth0(1, PlayerList, PlayerHand).

getId(PlayerList, PlayerId) :-
	nth0(0, PlayerList, PlayerId).

isHuman(PlayerList) :-
	getId(PlayerList, Id),
	Id = 0.

startNewRound() :- playRound(0, [],[],[],[], _).


%id, deck, table, p0Info, p1Info, retVal
playRound(FirstId,[],[],[],[],_) :- 
		createDeck(RawDeck),
		drawFourCards(RawDeck, AfterOneDraw, HumanCards),
		drawFourCards(AfterOneDraw, AfterTwoDraws, CompCards),
		drawFourCards(AfterTwoDraws, Deck, TableCards),
		createNewPlayer(0, HumanCards, HumanPlayer),
		createNewPlayer(1, CompCards, ComputerPlayer),
		playRound(0, Deck, TableCards, HumanPlayer, ComputerPlayer, _).



%Round end rule, deck is empty, player infos have null hands
%playRound(_,[])	

%Main loop
playRound(FirstId, Deck, Table, P0Info, P1Info, _) :-
	printFullTable(P0Info, Table, P1Info),
	doPlayerMove(P0Info, Table, P0AfterMove, TableAfterP0),
	printFullTable(P0AfterMove, TableAfterP0, P1Info).


%Human Player
doPlayerMove(PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	isHuman(PlayerList),
	getActionChoice(MoveChoice),
	doHumanMove(MoveChoice, PlayerList, Table, PlayerAfterMove, TableAfterMove).
	%TODO: Continue working from here


	%Hard coded trail
	%removeAtIndex(Hand, 1 , NewHand, PlayedCard),
	%mergeLists(Table, [PlayedCard], TableAfterMove),
	%createNewPlayer(0, NewHand, PlayerAfterMove).


%doHumanMove(0, PlayerList, Table, PlayerAfterMove, TableAfterMove)
%doHumanMove(1, PlayerList, Table, PlayerAfterMove, TableAfterMove)

doHumanMove(2, PlayerList, Table, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	writeln("Which card would you like to trail?"),
	length(Hand, CardsInHand),
	getNumericInput(0, CardsInHand, TrailedCardIndex),
	doTrail(PlayerList, Table, TrailedCardIndex, PlayerAfterMove, TableAfterMove).





doTrail(PlayerList, Table, PlayedCardIndex, PlayerAfterMove, TableAfterMove) :-
	getHand(PlayerList, Hand),
	removeAtIndex(Hand, PlayedCardIndex, ResultingHand, TrailedCard),
	mergeLists(Table, [TrailedCard], TableAfterMove),
	createNewPlayer(0, ResultingHand, PlayerAfterMove).


getActionChoice(MoveChoice) :-
	write("Would you like to (C)apture, (B)uild, or (T)rail: "),
	getMoveChar(InputChar),
	nl,
	validateMoveChar(InputChar, Validated),
	convertMoveCharToNum(Validated, OptionChoice),
	MoveChoice = OptionChoice.


getMoveChar(Input) :- read(Input).

validateMoveChar(c, c).
validateMoveChar(t, t).
validateMoveChar(b, b).
validateMoveChar(Input, Output) :-
	writeln("Invalid move char."),
	getMoveChar(RetryInput),
	validateMoveChar(RetryInput, Validated),
	Output = Validated.
	
convertMoveCharToNum(c, 0).
convertMoveCharToNum(b, 1).
convertMoveCharToNum(_, 2).	


getNumericInput(Lower, Upper, Result) :-
	write("Enter a number between "),
	write(Lower),
	write(" and "),
	write(Upper),
	writeln(": "),
	read(Input),
	validateNumericInput(Lower, Upper, Input, Result).

validateNumericInput(Lower, Upper, Check, Result) :-
	integer(Check),
	Check >= Lower,
	Check =< Upper,
	Result = Check.

validateNumericInput(Lower, Upper, _, Result) :-
	write("Invalid input, try again: "),
	getNumericInput(Lower, Upper, Result).	
	
printCards([]) :- writeln(" ").

printCards([Card | Rest]) :-
	displayCard(Card),
	write(" "),
	printCards(Rest).

	
printHand(PlayerList) :-
	getHand(PlayerList, Hand),
	printCards(Hand).

printFullTable(HumanPlayer, Table, ComputerPlayer) :-
	isHuman(HumanPlayer),
	write("Comp:  "),
	printHand(ComputerPlayer),
	writeln(" "),
	write("Table: "),
	printCards(Table),
	writeln(" "),
	write("Human: "),
	printHand(HumanPlayer),
	writeln(" ").

%Bound back function to swap orders
printFullTable(ComputerPlayer, Table, HumanPlayer) :-
	printFullTable(HumanPlayer, Table, ComputerPlayer).
